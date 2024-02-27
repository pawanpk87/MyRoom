import React from "react";
import Card from "../Card/Card";
import { LoginOutlined } from "@ant-design/icons";

export default function TotalCheckIn({ count }: { count: number }) {
  return (
    <Card
      data={{
        title: "Check-Ins (Today)",
        number: count,
        icon: (
          <LoginOutlined
            style={{
              color: "blue",
              fontSize: "25px",
            }}
          />
        ),
      }}
    />
  );
}
