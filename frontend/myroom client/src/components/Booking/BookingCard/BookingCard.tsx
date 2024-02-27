import { bookings, rooms } from "@/typings";
import { formatDayjsDate } from "@/utils/utils";
import { DatePicker, Tag } from "antd";
import React, { useEffect } from "react";
const dateFormat = "YYYY-MM-DD";
import roomService from "@/services/myRoom/rooms/roomService";
import "./BookingCard.css";

export default function BookingCard({
  bookingRequestData,
}: {
  bookingRequestData: bookings.IBookingRequestData;
}) {
  const [room, setRoom] = React.useState<rooms.IRoomData | null>(null);
  const [error, setError] = React.useState<string | null>(null);

  useEffect(() => {
    roomService
      .getRoom(bookingRequestData.roomId)
      .then((data) => {
        setRoom(data.data);
      })
      .catch((error) => {
        setError(error.response.data.message);
      });
  }, [bookingRequestData]);

  return (
    <div className="bookingCard">
      {room && (
        <>
          <div className="title">
            <h3>{room.title}</h3>
          </div>

          <div className="cost">
            <h1>₹{room.cost}</h1>
          </div>

          <div className="dates">
            <div className="dates_header">
              <h3>Dates</h3>
            </div>
            <DatePicker.RangePicker
              style={{ width: "100%", height: "50px" }}
              value={[
                formatDayjsDate(bookingRequestData.checkIn, dateFormat),
                formatDayjsDate(bookingRequestData.checkOut, dateFormat),
              ]}
              disabled={true}
            />
          </div>

          <GuestList guestsList={bookingRequestData.guests} />

          <div className="priceDetails">
            <div className="priceDetails_header">
              <h3>Price Details</h3>
            </div>
            <div className="prices">
              <div className="item_prices">
                <span>Price</span>
                <strong>
                  <Tag color="#2db7f5"> ₹{room.prices.price}</Tag>
                </strong>
              </div>

              <div className="item_prices">
                <span>Cleaning Fee</span>
                <strong>
                  <Tag color="#87d068"> ₹{room.prices.cleaningFee}</Tag>
                </strong>
              </div>

              <div className="item_prices">
                <span>Room Service</span>
                <strong>
                  <Tag color="#108ee9"> ₹{room.prices.roomService}</Tag>
                </strong>
              </div>
            </div>
          </div>
        </>
      )}
      {room == null && error && (
        <>
          <h1 style={{ color: "red" }}>{error}</h1>
        </>
      )}
    </div>
  );
}

function GuestList({
  guestsList,
}: {
  guestsList: bookings.IGuestList;
}): JSX.Element {
  return (
    <div className="guests">
      <strong> {guestsList.adults + guestsList.children} Guests</strong>
    </div>
  );
}
