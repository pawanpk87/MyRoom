"use client";
import { bookings } from "@/typings";
import { UsergroupAddOutlined } from "@ant-design/icons";
import { Popover } from "antd";

export default function BookingGuests({
  guests,
}: {
  guests: bookings.IGuestList;
}) {
  return (
    <Popover
      content={
        <div className="bookingGuest">
          <div>
            <span>Adults: </span>
            <span>{guests.adults}</span>
          </div>

          <div>
            <span>Children: </span>
            <span>{guests.children}</span>
          </div>
        </div>
      }
      title="Guests"
      trigger="hover"
      className="type"
    >
      <div>
        <UsergroupAddOutlined />
        <span>Guests</span>
      </div>
      <div>
        <span>{guests.adults + guests.children}</span>
      </div>
    </Popover>
  );
}
