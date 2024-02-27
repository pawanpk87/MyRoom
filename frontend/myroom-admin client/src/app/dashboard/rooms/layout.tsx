import Link from "next/link";
import "./rooms.css";

export default function RoomsLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="rooms">
      <div className="header">
        <div>
          <h3>Rooms</h3>
        </div>
        <div>
          <Link href={`/dashboard/rooms/manage?type=Add Room`}>Add Room</Link>
        </div>
      </div>
      {children}
    </div>
  );
}
