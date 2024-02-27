"use client";
import React from "react";
import { Button, Result } from "antd";
import { useRouter } from "next/navigation";
import "./unauthorized.css";

export default function UnAuthorized() {
  const router = useRouter();
  return (
    <div className="unauthorized">
      <Result
        status="403"
        title="403"
        subTitle="Sorry, you are not authorized to access this page."
        extra={
          <Button
            type="primary"
            onClick={() => {
              router.push("/");
            }}
          >
            Back Home
          </Button>
        }
      />
    </div>
  );
}
