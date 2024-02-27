"use client";
import React, { useEffect, useState } from "react";
import {
  Image as AntImage,
  Avatar,
  Rate,
  Table,
  Tooltip,
  notification,
} from "antd";
import type {
  ColumnsType,
  TableProps,
  TablePaginationConfig,
} from "antd/es/table";
import type { FilterValue, SorterResult } from "antd/es/table/interface";
import { bookings, rooms } from "@/typings";
import type {} from "antd/es/table";
import { InfoCircleOutlined } from "@ant-design/icons";
import Link from "next/link";
import { reviews } from "@/typings/reviews";
import reviewService from "@/services/myRoom/review/reviewService";
import User from "./User/User";

interface DataType extends reviews.IReviewData {}

const columns: ColumnsType<DataType> = [
  {
    title: "Reviw Id",
    dataIndex: "id",
    key: "id",
    width: "30px",
    ellipsis: true,
    render: (id) => {
      return <Link href={`reviews/${id}`}>{id}</Link>;
    },
  },

  {
    title: "User",
    dataIndex: "uid",
    key: "uid",
    width: "70px",
    ellipsis: true,
    render: (uid) => {
      return <User uid={uid} />;
    },
  },

  {
    title: "Rating",
    dataIndex: "rating",
    key: "rating",
    width: "80px",
    render: (rating) => {
      return <Rate disabled defaultValue={rating} />;
    },
  },

  {
    title: "Review Text",
    dataIndex: "reviewText",
    key: "reviewText",
    width: "100px",
    ellipsis: {
      showTitle: false,
    },
    render: (reviewText) => {
      return (
        <Tooltip placement="topLeft" title={reviewText}>
          {reviewText}
        </Tooltip>
      );
    },
  },

  {
    title: "Room Id",
    dataIndex: "roomId",
    key: "roomId",
    width: "80px",
    ellipsis: true,
    render: (roomId) => <span>#{roomId}</span>,
  },
];

interface TableParams {
  pagination?: TablePaginationConfig;
  sortField?: string;
  sortOrder?: string;
  filters?: Record<string, FilterValue>;
}

export default function ReviewsTable() {
  const [toastApi, toastContextHolder] = notification.useNotification();
  const [loading, setLoading] = React.useState<boolean>(false);
  const [tableParams, setTableParams] = React.useState<TableParams>({
    pagination: {
      current: 1,
      pageSize: 10,
    },
  });
  const [data, setData] = React.useState<reviews.IReviewQueryData | null>(null);

  const fetchData = () => {
    setLoading(true);
    const query: reviews.IGetReviewsQuery = {};

    if (tableParams.pagination) {
      query.page = tableParams.pagination.current;
      query.limit = tableParams.pagination.pageSize;
    }

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

    reviewService
      .getReviews(query)
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

  return (
    data && (
      <div>
        <Table
          columns={columns}
          dataSource={data.reviews}
          scroll={{ x: 1000, y: 480 }}
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
