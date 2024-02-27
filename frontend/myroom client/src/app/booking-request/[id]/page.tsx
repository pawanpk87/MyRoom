"use client";
import React, { useEffect, useState } from "react";
import BookingRequestSteps from "@/components/Booking/BookingRequestSteps/BookingRequestSteps";
import { bookings } from "@/typings";
import { FileDoneOutlined, UserOutlined } from "@ant-design/icons";
import { BsStripe } from "react-icons/bs";
import bookingService from "@/services/myRoom/booking/bookingService";
import { notFound, usePathname, useRouter } from "next/navigation";
import BookingCard from "@/components/Booking/BookingCard/BookingCard";
import { useUserAuth } from "@/firebase/auth/authProvider";
import "./bookingRequest.css";

const steps = [
  {
    title: "Guest Details",
    icon: <UserOutlined />,
  },
  {
    title: "Payment",
    icon: <BsStripe />,
  },
  {
    title: "Booking Receipt",
    icon: <FileDoneOutlined />,
  },
];

export default function Room({
  params,
}: {
  params: {
    id: string;
  };
}) {
  const bookingRequestId = params.id;

  const router = useRouter();
  const pathname = usePathname();

  const { user } = useUserAuth();

  const [bookingRequestData, setBookingRequestData] =
    React.useState<bookings.IBookingRequestData | null>(null);

  const [currentStep, setCurrentStep] = useState<number>(0);

  useEffect(() => {
    bookingService
      .getBookingRequest(bookingRequestId)
      .then((data) => {
        setBookingRequestData(data.data);
        if (data.data.status === bookings.BookingRequestStatus.BOOKED) {
          setCurrentStep(2);
        }
      })
      .catch((error) => {
        notFound();
      });
  }, [bookingRequestId]);

  const next = () => {
    setCurrentStep(currentStep + 1);
  };

  const prev = () => {
    setCurrentStep(currentStep - 1);
  };

  return bookingRequestData && user ? (
    <div className="bookingRequest">
      <div className="steps">
        <BookingRequestSteps
          bookingRequestData={bookingRequestData}
          setBookingRequestData={setBookingRequestData}
          user={user}
          currentStep={currentStep}
          steps={steps}
          prev={prev}
          next={next}
        />
        <div className="bookingData">
          <BookingCard bookingRequestData={bookingRequestData} />
        </div>
      </div>
    </div>
  ) : null;
}
