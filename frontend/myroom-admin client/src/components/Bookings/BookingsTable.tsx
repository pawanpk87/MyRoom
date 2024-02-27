"use client";
import React, { useEffect, useRef, useState } from "react";
import {
  InfoCircleOutlined,
  SearchOutlined,
  UserOutlined,
} from "@ant-design/icons";
import type { GetRef, TableColumnsType, TableColumnType } from "antd";
import {
  Avatar,
  Button,
  Input,
  Space,
  Table,
  Tag,
  Tooltip,
  notification,
} from "antd";
import type {
  FilterDropdownProps,
  FilterValue,
  SorterResult,
} from "antd/es/table/interface";
import { formatShortDate } from "@/utils/utils";
import Link from "next/link";
import { bookings } from "@/typings";
import { TablePaginationConfig, TableProps } from "antd/lib";
import bookingService from "@/services/myRoom/booking/bookingService";

interface DataType extends bookings.IBooking {}

type InputRef = GetRef<typeof Input>;

type DataIndex = keyof DataType;

type GetSingle<T> = T extends (infer U)[] ? U : never;
type Sorts = GetSingle<Parameters<OnChange>[2]>;

export default function BookingsTable() {
  const [toastApi, toastContextHolder] = notification.useNotification();
  const [searchText, setSearchText] = useState("");
  const [searchedColumn, setSearchedColumn] = useState("");
  const searchInput = useRef<InputRef>(null);
  const [sortedInfo, setSortedInfo] = useState<Sorts>({});
  const [loading, setLoading] = React.useState<boolean>(false);
  const [tableParams, setTableParams] = React.useState<TableParams>({
    pagination: {
      current: 1,
      pageSize: 10,
    },
  });
  const [data, setData] = React.useState<bookings.IBookings | null>(null);

  const fetchData = () => {
    setLoading(true);
    const query: bookings.IGetBookingsQuery = {};

    if (tableParams.pagination) {
      query.page = tableParams.pagination.current - 1;
      query.size = tableParams.pagination.pageSize;
    }

    if (tableParams.order) {
      const field = tableParams.field;
      const order =
        tableParams.order === "ascend"
          ? bookings.SortingType.ASC
          : bookings.SortingType.DESC;

      if (field === "bookingDate") {
        query.bookingDate = {
          sortingType: order,
        };
      }

      if (field === "checkInDate") {
        query.checkInDate = {
          sortingType: order,
        };
      }

      if (field === "checkOutDate") {
        query.checkOutDate = {
          sortingType: order,
        };
      }
    }

    bookingService
      .getBookings(query)
      .then((data) => {
        setData(data.data);
        setTableParams({
          ...tableParams,
          pagination: {
            ...tableParams.pagination,
            total: data.data.totalElements,
          },
        });
        setLoading(false);
      })
      .catch((error) => {
        toastApi.open({
          message: "Error",
          description: error.message,
          icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
        });
      });
  };

  useEffect(() => {
    fetchData();
  }, [JSON.stringify(tableParams)]);

  const handleTableChange: TableProps<DataType>["onChange"] = (
    pagination: TablePaginationConfig,
    filters: Record<string, FilterValue>,
    sorter: SorterResult<DataType>
  ) => {
    setTableParams({
      pagination,
      filters,
      ...sorter,
    });
  };

  const handleSearch = (
    selectedKeys: string[],
    confirm: FilterDropdownProps["confirm"],
    dataIndex: DataIndex
  ) => {
    confirm();
    setSearchText(selectedKeys[0]);
    setSearchedColumn(dataIndex);
  };

  const handleReset = (clearFilters: () => void) => {
    clearFilters();
    setSearchText("");
  };

  const getColumnSearchProps = (
    dataIndex: DataIndex
  ): TableColumnType<DataType> => ({
    filterDropdown: ({
      setSelectedKeys,
      selectedKeys,
      confirm,
      clearFilters,
      close,
    }) => (
      <div style={{ padding: 8 }} onKeyDown={(e) => e.stopPropagation()}>
        <Input
          ref={searchInput}
          placeholder={`Search ${dataIndex}`}
          value={selectedKeys[0]}
          onChange={(e) =>
            setSelectedKeys(e.target.value ? [e.target.value] : [])
          }
          onPressEnter={() =>
            handleSearch(selectedKeys as string[], confirm, dataIndex)
          }
          style={{ marginBottom: 8, display: "block" }}
        />
        <Space>
          <Button
            type="primary"
            onClick={() =>
              handleSearch(selectedKeys as string[], confirm, dataIndex)
            }
            icon={<SearchOutlined />}
            size="small"
            style={{ width: 90 }}
          >
            Search
          </Button>
          <Button
            onClick={() => clearFilters && handleReset(clearFilters)}
            size="small"
            style={{ width: 90 }}
          >
            Reset
          </Button>
          <Button
            type="link"
            size="small"
            onClick={() => {
              confirm({ closeDropdown: false });
              setSearchText((selectedKeys as string[])[0]);
              setSearchedColumn(dataIndex);
            }}
          >
            Filter
          </Button>
          <Button
            type="link"
            size="small"
            onClick={() => {
              close();
            }}
          >
            close
          </Button>
        </Space>
      </div>
    ),
    filterIcon: (filtered: boolean) => (
      <SearchOutlined style={{ color: filtered ? "#1677ff" : undefined }} />
    ),
    onFilter: (value, record) =>
      record[dataIndex]
        .toString()
        .toLowerCase()
        .includes((value as string).toLowerCase()),
    onFilterDropdownOpenChange: (visible) => {
      if (visible) {
        setTimeout(() => searchInput.current?.select(), 100);
      }
    },
    render: (id) => {
      return <Link href={`bookings/${id}`}>{id}</Link>;
    },
  });

  const columns: TableColumnsType<DataType> = [
    {
      title: "Id",
      dataIndex: "id",
      key: "id",
      width: "100px",
      ellipsis: true,
      ...getColumnSearchProps("id"),
    },

    {
      title: "Guest",
      dataIndex: "contactDetails",
      key: "contactDetails",
      width: "200px",
      ellipsis: true,
      render: (contactDetails) => {
        const details = contactDetails;
        return (
          <div
            style={{
              display: "flex",
              flexDirection: "row",
              gap: "5px",
              fontSize: "10px",
            }}
          >
            <Avatar
              style={{ backgroundColor: "#87d068" }}
              icon={<UserOutlined />}
            />

            <div
              style={{
                display: "flex",
                flexDirection: "column",
              }}
            >
              <span>{details.emailId}</span>
              <span>{details.fullName}</span>
            </div>
          </div>
        );
      },
    },

    {
      title: "Booking Date",
      dataIndex: "bookingDate",
      key: "bookingDate",
      render: (value: string) => formatShortDate(new Date(value)),
      sorter: (a, b) =>
        new Date(a.bookingDate).valueOf() - new Date(b.bookingDate).valueOf(),
    },

    {
      title: "Check In",
      dataIndex: "checkIn",
      key: "checkIn",
      render: (value: string) => formatShortDate(new Date(value)),
      sorter: (a, b) =>
        new Date(a.checkIn).valueOf() - new Date(b.checkIn).valueOf(),
    },

    {
      title: "Check Out",
      dataIndex: "checkOut",
      key: "checkOut",
      render: (value: string) => formatShortDate(new Date(value)),
      sorter: (a, b) =>
        new Date(a.checkOut).valueOf() - new Date(b.checkOut).valueOf(),
    },

    {
      title: "Status",
      key: "status",
      dataIndex: "status",
      width: "150px",
      filters: [
        {
          text: "Confirmed",
          value: bookings.BookingStatus.CONFIRMED,
        },

        {
          text: "Pending Payment",
          value: bookings.BookingStatus.PENDING_PAYMENT,
        },

        {
          text: "Pay at hotel",
          value: bookings.BookingStatus.PAY_AT_HOTEL,
        },

        {
          text: "CheckIn",
          value: bookings.BookingStatus.CHECKED_IN,
        },

        {
          text: "CheckOut",
          value: bookings.BookingStatus.CHECKED_OUT,
        },

        {
          text: "Cancelled",
          value: bookings.BookingStatus.CANCELLED,
        },

        {
          text: "Expired",
          value: bookings.BookingStatus.EXPIRED,
        },

        {
          text: "No Show",
          value: bookings.BookingStatus.NO_SHOW,
        },
      ],

      onFilter(value, record) {
        return record.status === value;
      },

      render: (_, data, index) => {
        switch (data.status) {
          case bookings.BookingStatus.CONFIRMED:
            return <Tag color="green">Confirmed</Tag>;

          case bookings.BookingStatus.PENDING_PAYMENT:
            return <Tag color="volcano">Pending Payment</Tag>;

          case bookings.BookingStatus.PAY_AT_HOTEL:
            return <Tag color="magenta">Pay at hotel</Tag>;

          case bookings.BookingStatus.CHECKED_IN:
            return <Tag color="blue">CheckIn</Tag>;

          case bookings.BookingStatus.CHECKED_OUT:
            return <Tag color="geekblue">CheckOut</Tag>;

          case bookings.BookingStatus.CANCELLED:
            return <Tag color="gold">Cancelled</Tag>;

          case bookings.BookingStatus.EXPIRED:
            return <Tag color="magenta">Expired</Tag>;

          case bookings.BookingStatus.NO_SHOW:
            return <Tag color="cyan">No Show</Tag>;
        }
      },
    },

    {
      title: "Amount",
      dataIndex: "amount",
      key: "amount",
      sorter: (a, b) => Number(a.amount) - Number(b.amount),
    },

    {
      title: "Room",
      dataIndex: "roomDetails",
      key: "roomDetails",
      width: "200px",
      ellipsis: {
        showTitle: false,
      },
      render: (roomDetails) => {
        const details = roomDetails;
        return (
          <Tooltip
            placement="topLeft"
            title={`${details.title} #${details.id}`}
          >
            {`${details.title}`}

            <br />

            <span style={{ fontSize: "12px", color: "gray" }}>
              #{details.id}
            </span>
          </Tooltip>
        );
      },
    },
  ];

  return (
    <div>
      <Table
        columns={columns}
        dataSource={data?.content}
        scroll={{ x: 800, y: 580 }}
        pagination={tableParams.pagination}
        loading={loading}
        onChange={handleTableChange}
      />
      {toastContextHolder}
    </div>
  );
}
