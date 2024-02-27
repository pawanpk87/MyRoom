import Link from "next/link";
import "./bookings.css";

export default function BookingsLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="bookings">
      <div className="header">
        <div>
          <h3>Bookings</h3>
        </div>
      </div>
      {children}
    </div>
  );
}
