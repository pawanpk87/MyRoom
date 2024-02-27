import React from "react";
import Card from "../Card/Card";
import { BookOutlined } from "@ant-design/icons";

export default function NewBookings() {
  return (
    <Card
      data={{
        title: "New Bookings",
        number: 203,
        icon: (
          <BookOutlined
            style={{
              color: "orange",
              fontSize: "30px",
            }}
          />
        ),
      }}
    />
  );
}
