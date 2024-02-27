import BookingDetailCard from "@/components/Bookings/BookingDetailCard";
import bookingService from "@/services/myRoom/booking/bookingService";
import { bookings } from "@/typings";
import { notFound } from "next/navigation";
import "./booking.css";

async function getBooking(id: string): Promise<bookings.IBooking> {
  try {
    const booking: bookings.IBooking = (await bookingService.getBooking(id))
      .data;
    return booking;
  } catch (error: any) {
    notFound();
  }
}

export default async function Booking({
  params,
}: {
  params: {
    id: string;
  };
}) {
  const id = params.id;

  if (!id) {
    notFound();
  }

  const booking: bookings.IBooking = await getBooking(id);

  return (
    <div className="bookingdata">
      <BookingDetailCard booking={booking} />
    </div>
  );
}
