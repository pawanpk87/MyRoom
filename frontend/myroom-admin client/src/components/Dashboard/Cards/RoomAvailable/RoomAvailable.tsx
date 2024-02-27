import React, { useEffect, useState } from "react";
import Card from "../Card/Card";
import { HomeOutlined } from "@ant-design/icons";
import { organizations, rooms } from "@/typings";
import roomService from "@/services/myRoom/room/roomService";

export default function RoomAvailable({
  organization,
  error,
}: {
  organization: organizations.IOrganization;
  error: (message: string) => void;
}) {
  const [todayData, setTodayData] =
    React.useState<rooms.IGetRoomsCountData | null>(null);

  useEffect(() => {
    const query: rooms.IGetRoomsCountQuery = {
      organizationId: organization.id,
      bookingStatus: rooms.BookingStatus.AVAILABLE,
    };

    roomService
      .getRoomsCount(query)
      .then((data) => {
        setTodayData(data.data);
      })
      .catch((err) => {
        console.log(error);
        error(err.message);
      });
  }, [organization.id]);

  return (
    todayData && (
      <Card
        data={{
          title: "Rooms Available (Today)",
          number: todayData.count,
          icon: (
            <HomeOutlined
              style={{
                color: "green",
                fontSize: "25px",
              }}
            />
          ),
        }}
      />
    )
  );
}
