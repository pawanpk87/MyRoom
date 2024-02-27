import { bookings } from "@/typings";
import { ContactsOutlined } from "@ant-design/icons";
import "./BookingContactDetailsCard.css";

export default function BookingContactDetailsCard({
  contactDetails,
}: {
  contactDetails: bookings.IContactDetails;
}) {
  return (
    <div className="bookingContactDetails">
      <div className="bookingContactDetailsHeader">
        <ContactsOutlined />
        <span>Contact Details</span>
      </div>
      <div className="details">
        <div>
          <span>Email Id:</span>
          <span>{contactDetails.emailId}</span>
        </div>

        <div>
          <span>Name:</span>
          <span>{contactDetails.fullName}</span>
        </div>

        <div>
          <span>Phone Number:</span>
          <span>{contactDetails.phoneNumber}</span>
        </div>
      </div>
    </div>
  );
}
