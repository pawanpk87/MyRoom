import Searchbox from "@/components/TopMenubar/Searchbox";
import Image from "next/image";
import "./page.css";

export default function Home() {
  return (
    <main>
      <div className="searchRoomBox">
        <Searchbox />
      </div>
      <br />
      <div className="homeMain">
        <div className="image">
          <Image
            src={"/images/home.jpg"}
            alt={"login"}
            height={800}
            width={800}
            style={{
              borderRadius: "30px",
            }}
          />
        </div>
        <div className="myRoomAcount">
          <div className="aboutText">
            <p>
              MyRoom is a hotel room booking application that makes it easy and
              convenient for users to find and book accommodations for their
              travels.
            </p>
            <p>
              Our platform offers a wide range of options, from budget-friendly
              rooms to luxurious suites, ensuring that every traveler can find
              the perfect place to stay.
            </p>
            <p>
              With MyRoom, you can browse through various hotels, view photos
              and details of rooms, check availability, and make reservations
              securely.
            </p>
            <p>
              Whether you're planning a business trip, a family vacation, or a
              romantic getaway, MyRoom is your go-to destination for hassle-free
              hotel bookings.
            </p>
          </div>
        </div>
      </div>
    </main>
  );
}
