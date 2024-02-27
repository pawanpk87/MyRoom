import { Badge, Progress, Space, Tooltip } from "antd";
import "./TopBookedRoom.css";
import { useEffect, useState } from "react";
import { organizations, rooms } from "@/typings";
import roomService from "@/services/myRoom/room/roomService";

export default function TopBookedRoom({
  organization,
  error,
}: {
  organization: organizations.IOrganization;
  error: (message: string) => void;
}) {
  const [rooms, setRooms] = useState<rooms.IRoomData[] | null>(null);

  useEffect(() => {
    const query: rooms.IGetTopPerformersRoomsQuery = {
      organizationId: organization.id,
    };

    roomService
      .getTopPerformersRooms(query)
      .then((data) => {
        setRooms(data.data);
      })
      .catch((err) => {
        console.log(err);
        error(err.message);
      });
  }, []);

  return (
    rooms && (
      <div className="TopBookedRoomChart">
        <div className="header">Most Popular Room</div>
        <div className="content">
          <Space
            direction="vertical"
            size="middle"
            style={{
              width: "100%",
              display: "flex",
              flexDirection: "column",
              gap: "15px",
            }}
          >
            {rooms.map((data) => (
              <RoomCard
                key={data.id}
                name={data.title}
                count={data.bookingCount}
              />
            ))}
          </Space>
        </div>
      </div>
    )
  );
}

function RoomCard({
  name,
  count,
}: {
  name: string;
  count: number;
}): JSX.Element {
  return (
    <Tooltip title={name}>
      <div
        style={{
          padding: "8px",
          borderRadius: "5px",
          boxShadow: "0 3px 5px rgb(0 0 0 / 0.2)",
        }}
      >
        <Badge.Ribbon text={`${count ? count : 0} times`}>{name}</Badge.Ribbon>
      </div>
    </Tooltip>
  );
}
