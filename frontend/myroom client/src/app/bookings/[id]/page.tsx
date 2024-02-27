import bookingService from "@/services/myRoom/booking/bookingService";
import { notFound } from "next/navigation";
import { LoginOutlined, LogoutOutlined } from "@ant-design/icons";
import { bookings } from "@/typings";
import BookingStatus from "@/components/Booking/BookingStatus/BookingStatus";
import { formatShortDate } from "@/utils/utils";
import BookingGuests from "@/components/Booking/BookingGuests/BookingGuests";
import BookingContactDetailsCard from "@/components/Booking/BookingContactDetailsCard/BookingContactDetailsCard";
import BookingDetailsCard from "@/components/Booking/BookingDetailsCard/BookingDetailsCard";
import BookingExtraDetailsList from "@/components/Booking/BookingExtraDetailsList/BookingExtraDetailsList";
import "./booking.css";

async function fetchBooking(
  id: string
): Promise<bookings.IBookingData | undefined> {
  try {
    const data = await bookingService.getBookingDataById(id);
    return data.data;
  } catch (error: any) {
    return undefined;
  }
}

export default async function Booking({
  params,
}: {
  params: {
    id: string;
  };
}) {
  const booking: bookings.IBookingData | undefined = await fetchBooking(
    params.id
  );

  if (!booking) {
    notFound();
  }

  return (
    <div className="bookingData">
      <div className="bookingDetails">
        <div className="checkInOutTypeDiv">
          <div className="type">
            <div>
              <LoginOutlined />
              <span>Check-In</span>
            </div>
            <div>{formatShortDate(new Date(booking.checkIn))}</div>
          </div>

          <div className="type">
            <div>
              <LogoutOutlined />
              <span>Check-Out</span>
            </div>
            <div>{formatShortDate(new Date(booking.checkOut))}</div>
          </div>
          <BookingGuests guests={booking.guests} />
        </div>

        <div className="statusAndContactDetaisDiv">
          <BookingContactDetailsCard contactDetails={booking.contactDetails} />
          <div>
            <div>Status</div>
            <div>
              <BookingStatus status={booking.status} />
            </div>
          </div>
        </div>

        <BookingDetailsCard booking={booking} />

        <BookingExtraDetailsList booking={booking} />
      </div>
    </div>
  );
}
