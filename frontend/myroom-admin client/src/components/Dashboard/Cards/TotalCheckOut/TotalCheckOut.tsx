import { LogoutOutlined } from "@ant-design/icons";
import React from "react";
import Card from "../Card/Card";

export default function TotalCheckOut({ count }: { count: number }) {
  return (
    <Card
      data={{
        title: "Check-Outs (Today)",
        number: count,
        icon: (
          <LogoutOutlined
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
