import React from "react";
import { Flex, Rate } from "antd";

export default function BookingRatingInput() {
  return (
    <Flex gap="middle">
      <Rate defaultValue={3} />
    </Flex>
  );
}
