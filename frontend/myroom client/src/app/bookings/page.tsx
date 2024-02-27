"use client";
import { useUserAuth } from "@/firebase/auth/authProvider";
import React, { useEffect, useState } from "react";
import bookingService from "@/services/myRoom/booking/bookingService";
import { bookings } from "@/typings";
import "./bookings.css";
import Link from "next/link";

export default function Bookings() {
  const { user } = useUserAuth();
  const [bookings, setBookings] = React.useState<bookings.IBookings | null>(
    null
  );
  const [error, setError] = useState(null);

  useEffect(() => {
    if (user) {
      bookingService
        .getBookingsByUid(user?.uid)
        .then((data) => {
          setBookings(data.data);
        })
        .catch((error) => {
          console.log("error");
          setError(error.message);
        });
    }
  }, [user]);

  return (
    <div className="userBookingsMain">
      <div className="userBookingsMainHeader">
        <h1>Bookings</h1>
      </div>
      {bookings !== null ? (
        <div className="bookingListItems">
          {bookings.content.map((data: bookings.IBookingData) => (
            <BookingCard key={data.id} data={data} />
          ))}
        </div>
      ) : null}
    </div>
  );
}

function BookingCard({ data }: { data: bookings.IBookingData }): JSX.Element {
  return (
    <Link href={`http://localhost:3000/bookings/${data.id}`}>
      <div className="bookingListItem">
        <h2>
          <i className="fas fa-calendar-alt"></i> Booking for{" "}
          {data.roomDetails.title}
        </h2>
        <p>
          <strong>ID:</strong> {data.id}
        </p>
        <p>
          <strong>Amount:</strong> {data.amount}
        </p>
        <p>
          <strong>Check-In:</strong> {data.checkIn}
        </p>
        <p>
          <strong>Check-Out:</strong> {data.checkOut}
        </p>
      </div>
    </Link>
  );
}
