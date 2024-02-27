import { AddIcon, MinusIcon } from "@chakra-ui/icons";
import { bookings } from "@/typings";
import "./Guests.css";

export default function Guests({
  guestsList,
  setGuestsList,
}: {
  guestsList: bookings.IGuestList;
  setGuestsList: React.Dispatch<React.SetStateAction<bookings.IGuestList>>;
}) {
  return (
    <div className="guestslist">
      <div className="adults">
        <div>
          <section>Adults</section>
          <small>Ages 13 or above</small>
        </div>
        <div className="count">
          <AddIcon
            className="icon"
            boxSize={5}
            onClick={() => {
              setGuestsList({
                ...guestsList,
                adults: guestsList.adults + 1,
              });
            }}
          />
          {guestsList.adults}
          <MinusIcon
            className="icon"
            boxSize={5}
            onClick={() => {
              setGuestsList({
                ...guestsList,
                adults: guestsList.adults - 1,
              });
            }}
          />
        </div>
      </div>
      <div className="adults">
        <div>
          <section>Children</section>
          <small>Ages 2–12</small>
        </div>
        <div className="count">
          <AddIcon
            className="icon"
            boxSize={5}
            onClick={() => {
              setGuestsList({
                ...guestsList,
                children: guestsList.children + 1,
              });
            }}
          />
          {guestsList.children}
          <MinusIcon
            className="icon"
            boxSize={5}
            onClick={() => {
              setGuestsList({
                ...guestsList,
                children: guestsList.children - 1,
              });
            }}
          />
        </div>
      </div>
    </div>
  );
}
