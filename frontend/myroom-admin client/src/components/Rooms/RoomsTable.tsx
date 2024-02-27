"use client";
import React, { useEffect, useState } from "react";
import {
  Image as AntImage,
  Button,
  Rate,
  Table,
  Tag,
  Tooltip,
  notification,
} from "antd";
import type {
  ColumnsType,
  TableProps,
  TablePaginationConfig,
} from "antd/es/table";
import type { FilterValue, SorterResult } from "antd/es/table/interface";
import { rooms } from "@/typings";
import type {} from "antd/es/table";
import roomService from "@/services/myRoom/room/roomService";
import {
  InfoCircleOutlined,
  SmileOutlined,
  WarningOutlined,
} from "@ant-design/icons";
import confirm from "antd/es/modal/confirm";
import Link from "next/link";
import { useUserAuth } from "@/firebase/auth/authProvider";

interface DataType extends rooms.IRoomData {}

const columns: ColumnsType<DataType> = [
  {
    title: "Id",
    dataIndex: "id",
    key: "id",
    width: "100px",
    ellipsis: true,
    render: (id) => {
      return <Link href={`rooms/${id}`}>{id}</Link>;
    },
  },

  {
    title: "Image",
    dataIndex: "mainImage",
    key: "mainImage",
    width: "100px",
    render: (mainImage) => {
      return (
        <AntImage
          width={30}
          height={30}
          src={mainImage}
          fallback="data:image/png;base64,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"
        />
      );
    },
  },

  {
    title: "Status",
    key: "status",
    dataIndex: "status",
    width: "120px",

    filters: [
      {
        text: "Available",
        value: rooms.BookingStatus.AVAILABLE,
      },
      {
        text: "Booked",
        value: rooms.BookingStatus.BOOKED,
      },

      {
        text: "Maintenance",
        value: rooms.BookingStatus.MAINTENANCE,
      },

      {
        text: "Occupied",
        value: rooms.BookingStatus.OCCUPIED,
      },
    ],

    onFilter(value, record) {
      return record.bookingStatus === value;
    },

    render: (_, data, index) => {
      switch (data.bookingStatus) {
        case rooms.BookingStatus.AVAILABLE:
          return <Tag color="green">Available</Tag>;

        case rooms.BookingStatus.BOOKED:
          return <Tag color="gold">Booked</Tag>;

        case rooms.BookingStatus.MAINTENANCE:
          return <Tag color="magenta">Maintenance</Tag>;

        case rooms.BookingStatus.OCCUPIED:
          return <Tag color="red">Occupied</Tag>;
      }
    },
  },

  {
    title: "Cost",
    dataIndex: "cost",
    key: "cost",
    width: "100px",
    sorter: (a, b) => a.cost - b.cost,
  },

  {
    title: "Capacity",
    dataIndex: "capacity",
    key: "capacity",
    width: "100px",
    sorter: (a, b) => a.capacity - b.capacity,
  },

  {
    title: "Available Dates",
    key: "availableDates",
    dataIndex: "availableDates",
    width: "250px",
    render: (_, data, index) => {
      return (
        <div
          style={{
            display: "flex",
            flexDirection: "row",
            justifyContent: "space-between",
          }}
        >
          <Tag bordered={false} color="processing">
            {new Date(data.availableDates.startDate).toLocaleDateString()}
          </Tag>

          <Tag bordered={false} color="processing">
            -
          </Tag>

          <Tag bordered={false} color="processing">
            {new Date(data.availableDates.endDate).toLocaleDateString()}
          </Tag>
        </div>
      );
    },
  },

  {
    title: "Title",
    dataIndex: "title",
    key: "title",
    ellipsis: {
      showTitle: false,
    },
    render: (title) => {
      return (
        <Tooltip placement="topLeft" title={title}>
          {title}
        </Tooltip>
      );
    },
  },

  {
    title: "Description",
    dataIndex: "description",
    key: "description",
    ellipsis: {
      showTitle: false,
    },
    render: (description) => {
      return (
        <Tooltip placement="topLeft" title={description}>
          {description}
        </Tooltip>
      );
    },
  },

  {
    title: "Note",
    dataIndex: "note",
    key: "note",
    ellipsis: {
      showTitle: false,
    },
    render: (note) => {
      return (
        <Tooltip placement="topLeft" title={note}>
          {note}
        </Tooltip>
      );
    },
  },

  {
    title: "Address",
    dataIndex: "address",
    key: "address",
    ellipsis: {
      showTitle: false,
    },
    render: (address) => {
      return (
        <Tooltip placement="topLeft" title={address}>
          {address}
        </Tooltip>
      );
    },
  },

  {
    title: "Rating",
    dataIndex: "rating.rating",
    key: "rating",
    width: "180px",
    render: (_, data, index) => {
      return <Rate disabled defaultValue={data.rating?.rating} />;
    },
  },

  {
    title: "Check In Type",
    dataIndex: "checkInType",
    key: "checkInType",
    render: (_, data, index) => {
      return data.checkInTypes[0] === rooms.CheckInType.ONLINE_PAYMENT ? (
        <Tag color="#2db7f5">{data.checkInTypes[0]}</Tag>
      ) : (
        <Tag color="#586df7">{data.checkInTypes[0]}</Tag>
      );
    },
  },

  {
    title: "Action",
    dataIndex: "id",
    key: "action",
    render: (id) => {
      return (
        <div
          style={{
            display: "flex",
            flexDirection: "row",
            justifyContent: "space-between",
          }}
        >
          <Link href={`rooms/manage?id=${id}&type=Edit Room`}>Edit Room</Link>
          <Link href={`rooms/manage?id=${id}&type=Delete Room`}>
            Delete Room
          </Link>
        </div>
      );
    },
  },
];

interface TableParams {
  pagination?: TablePaginationConfig;
  sortField?: string;
  sortOrder?: string;
  filters?: Record<string, FilterValue>;
}

export default function RoomsTableAnt() {
  const { user, organization } = useUserAuth();

  const [toastApi, toastContextHolder] = notification.useNotification();
  const [loading, setLoading] = React.useState<boolean>(false);
  const [tableParams, setTableParams] = React.useState<TableParams>({
    pagination: {
      current: 1,
      pageSize: 10,
    },
  });
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [data, setData] = React.useState<rooms.IQueryData | null>(null);

  const fetchData = () => {
    setLoading(true);
    const query: rooms.IGetRoomsQuery = {};

    if (tableParams.pagination) {
      query.page = tableParams.pagination.current;
      query.limit = tableParams.pagination.pageSize;
    }

    query.organizationId = organization?.id;

    // if (tableParams.order) {
    //   const field = tableParams.field;
    //   const order = tableParams.order === "ascend" ? 1 : -1;
    //   query.sorting = [
    //     {
    //       operator: order,
    //       field: field,
    //     },
    //   ];
    // }

    roomService
      .getRooms(query)
      .then((data) => {
        setData(data.data);
        setTableParams({
          ...tableParams,
          pagination: {
            ...tableParams.pagination,
            total: data.data.totalRecods,
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

  const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
    setSelectedRowKeys(newSelectedRowKeys);
  };

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };

  const hasSelected = selectedRowKeys.length > 0;

  const showDeleteConfirm = () => {
    confirm({
      title: "Do you want to delete these rooms?",

      icon: <WarningOutlined />,

      content: "Are you sure you want to proceed?",

      onOk() {
        const deleteData: rooms.IDeleteRooms = {
          uid: user?.uid!,
          ids: selectedRowKeys.map((value) => {
            return value.toString();
          }),
        };

        roomService
          .deleteRooms(deleteData)
          .then((response) => {
            toastApi.open({
              message: "Success",
              description: "Deleted",
              icon: <SmileOutlined style={{ color: "#108ee9" }} />,
            });
            fetchData();
          })
          .catch((error) => {
            toastApi.open({
              message: "Error",
              description: error.response.data.message,
              icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
            });
          });
      },
      onCancel() {},
    });
  };

  return (
    data && (
      <div>
        <div style={{ marginBottom: 16 }}>
          <Button
            onClick={showDeleteConfirm}
            type="primary"
            danger
            disabled={!hasSelected}
          >
            Delete
          </Button>
          <span style={{ marginLeft: 8 }}>
            {hasSelected ? `Selected ${selectedRowKeys.length} rooms` : ""}
          </span>
        </div>
        <Table
          columns={columns}
          dataSource={data.rooms}
          scroll={{ x: 2500, y: 480 }}
          rowSelection={rowSelection}
          expandable={{
            expandedRowRender: (data) => {
              return (
                <div
                  style={{
                    padding: "5px",
                    background: "white",
                    borderRadius: "5px",
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "flex-start",
                    gap: "5px",
                  }}
                >
                  <div style={{}}>
                    <div>
                      <h3>Prices</h3>
                    </div>
                    <div
                      style={{
                        width: "fit-content",
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "space-evenly",
                        gap: "2px",
                      }}
                    >
                      <Tag color="#108ee9">
                        Price {`${data.prices.price} ${data.prices.currency}`}
                      </Tag>

                      <Tag color="#87d068">
                        Clearning Fee{" "}
                        {`${data.prices.cleaningFee} ${data.prices.currency}`}
                      </Tag>

                      <Tag color="#2db7f5">
                        Room Service{" "}
                        {`${data.prices.roomService} ${data.prices.currency}`}
                      </Tag>
                    </div>
                  </div>

                  <div style={{}}>
                    <div>
                      <h3>Amenities</h3>
                    </div>
                    <div
                      style={{
                        width: "fit-content",
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "space-evenly",
                        gap: "2px",
                      }}
                    >
                      {data.amenities.map((amenitie, index) => (
                        <Tag key={index}>{amenitie}</Tag>
                      ))}
                    </div>
                  </div>

                  <div style={{}}>
                    <div>
                      <h3>Features</h3>
                    </div>
                    <div
                      style={{
                        width: "fit-content",
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "space-evenly",
                        gap: "2px",
                      }}
                    >
                      {data.features.map((feature, index) => (
                        <Tag key={index}>{feature}</Tag>
                      ))}
                    </div>
                  </div>
                </div>
              );
            },
          }}
          rowKey={(record) => record.id}
          pagination={tableParams.pagination}
          loading={loading}
          onChange={handleTableChange}
        />
        {toastContextHolder}
      </div>
    )
  );
}
