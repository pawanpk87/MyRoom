"use client";
import { bookings } from "@/typings";
import {
  Table,
  TableContainer,
  Tbody,
  Td,
  Tfoot,
  Th,
  Thead,
  Tr,
} from "@chakra-ui/react";
import { Descriptions } from "antd";
import type { DescriptionsProps } from "antd";
import "./RoomDetailsCard.css";

export default function RoomDetailsCard({
  roomMetaData,
  totalAmount,
}: {
  roomMetaData: bookings.IRoomMetaData;
  totalAmount: string;
}): JSX.Element {
  const items: DescriptionsProps["items"] = [
    {
      key: "1",
      label: "Room Id",
      children: roomMetaData.id,
    },
    {
      key: "2",
      label: "Organization Id",
      children: roomMetaData.organizationId,
    },
  ];

  return (
    <div className="roomDetailsCard">
      <div className="roomDetailsCardHeader">
        <h2>{roomMetaData.title}</h2>
      </div>
      <div>
        <Descriptions title="Room Details" items={items} />
      </div>
      <div>
        <TableContainer width={300}>
          <Table size="sm">
            <Thead>
              <Tr>
                <Th>To convert</Th>
                <Th isNumeric>multiply by</Th>
              </Tr>
            </Thead>
            <Tbody>
              <Tr>
                <Td>price</Td>
                <Td isNumeric>{roomMetaData.prices.price}</Td>
              </Tr>
              <Tr>
                <Td>cleaningFee</Td>
                <Td isNumeric>{roomMetaData.prices.cleaningFee}</Td>
              </Tr>
              <Tr>
                <Td>roomService</Td>
                <Td isNumeric>{roomMetaData.prices.roomService}</Td>
              </Tr>
            </Tbody>
            <Tfoot>
              <Tr>
                <Th>totla</Th>
                <Th isNumeric>{totalAmount}</Th>
              </Tr>
            </Tfoot>
          </Table>
        </TableContainer>
      </div>
    </div>
  );
}
