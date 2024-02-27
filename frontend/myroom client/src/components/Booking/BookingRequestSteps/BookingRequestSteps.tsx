import React, { useEffect, useState } from "react";
import {
  Button,
  Form,
  Input,
  Select,
  Steps,
  Segmented,
  message,
  Result,
} from "antd";
import { bookings } from "@/typings";
const { Option } = Select;
import { LeftOutlined, RightOutlined } from "@ant-design/icons";
import { SegmentedValue } from "antd/es/segmented";
import bookingService from "@/services/myRoom/booking/bookingService";
import { User } from "firebase/auth";
import "./BookingRequestSteps.css";
import { useRouter } from "next/navigation";

export default function BookingRequestSteps({
  bookingRequestData,
  setBookingRequestData,
  user,
  currentStep,
  steps,
  prev,
  next,
}: {
  bookingRequestData: bookings.IBookingRequestData;
  setBookingRequestData: React.Dispatch<
    React.SetStateAction<bookings.IBookingRequestData>
  >;
  user: User;
  currentStep: number;
  steps: any;
  prev: () => void;
  next: () => void;
}): JSX.Element {
  const [messageApi, contextHolder] = message.useMessage();

  const success = (message: string) => {
    messageApi.open({
      type: "success",
      content: message,
    });
  };

  const error = (message: string) => {
    messageApi.open({
      type: "error",
      content: message,
    });
  };

  const warning = (message: string) => {
    messageApi.open({
      type: "warning",
      content: message,
    });
  };

  return (
    <div className="steps-form">
      <Steps
        current={currentStep}
        items={steps}
        type="navigation"
        className="site-navigation-steps"
      />
      <div className="stepDiv">
        <div className="form">
          {currentStep === 0 && (
            <GuestDetailsStep
              bookingRequestData={bookingRequestData}
              setBookingRequestData={setBookingRequestData}
              user={user}
              prev={prev}
              next={next}
              success={success}
              error={error}
              warning={warning}
            />
          )}

          {currentStep === 1 && (
            <PaymentStep
              bookingRequestData={bookingRequestData}
              user={user}
              prev={prev}
              next={next}
              success={success}
              error={error}
              warning={warning}
            />
          )}

          {currentStep === 2 && (
            <BookingReceiptStep
              bookingRequestId={bookingRequestData.id}
              error={error}
            />
          )}
        </div>
      </div>
      {contextHolder}
    </div>
  );
}

function GuestDetailsStep({
  bookingRequestData,
  setBookingRequestData,
  user,
  prev,
  next,
  success,
  error,
  warning,
}: {
  bookingRequestData: bookings.IBookingRequestData;
  setBookingRequestData: React.Dispatch<
    React.SetStateAction<bookings.IBookingRequestData>
  >;
  user: User;
  prev: () => void;
  next: () => void;
  success: (message: string) => void;
  error: (message: string) => void;
  warning: (message: string) => void;
}): JSX.Element {
  const [fullName, setFullName] = useState<string>(user.displayName);
  const [emailId, setEmailId] = useState<string>(user.email);
  const [phoneNumber, setPhoneNumber] = useState<string>(
    bookingRequestData.contactDetails !== null
      ? bookingRequestData.contactDetails?.phoneNumber
      : ""
  );

  const [loading, setLoading] = useState<boolean>(false);

  const onUpdateContactDetails = () => {
    setLoading(true);

    const updateContactDetails: bookings.IUpdateContactDetailsRequest = {
      bookingRequestId: bookingRequestData.id,
      details: {
        fullName: fullName!,
        emailId: emailId!,
        phoneNumber: phoneNumber,
      },
      uid: user?.uid,
    };

    bookingService
      .updateBookingRequestContactDetails(updateContactDetails)
      .then((data) => {
        setBookingRequestData(data.data);
        setTimeout(() => {
          setLoading(false);
          success("Guest Details Updated!");
          next();
        }, 2000);
      })
      .catch((error) => {
        setLoading(false);
        error(error.response.data.message);
      });
  };

  return (
    <div>
      <Form style={{ maxWidth: 600 }} onFinish={onUpdateContactDetails}>
        <Form.Item name={"fullName"} label="Full Name">
          <Input defaultValue={user.displayName} disabled={true} />
        </Form.Item>

        <Form.Item name={"email"} label="Email">
          <Input defaultValue={user.email} disabled={true} />
        </Form.Item>

        <Form.Item
          name="phoneNumber"
          label="Phone Number"
          rules={[{ required: true, message: "phoneNumber is required" }]}
        >
          <Input
            addonBefore={prefixSelector}
            style={{ width: "100%" }}
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
          />
        </Form.Item>

        <div className="button">
          <Button type="primary" htmlType="submit" loading={loading}>
            Next <RightOutlined />
          </Button>
        </div>
      </Form>
    </div>
  );
}

function PaymentStep({
  bookingRequestData,
  user,
  prev,
  next,
  success,
  error,
  warning,
}: {
  bookingRequestData: bookings.IBookingRequestData;
  user: User;
  prev: () => void;
  next: () => void;
  success: (message: string) => void;
  error: (message: string) => void;
  warning: (message: string) => void;
}): JSX.Element {
  const [loading, setLoading] = useState<boolean>(false);

  const [paymentType, setPaymentType] = React.useState<bookings.PaymentType>(
    bookings.PaymentType.ONLINE_PAYMENT
  );

  const onChange = (value: SegmentedValue) => {
    setPaymentType(
      bookings.PaymentType[value as keyof typeof bookings.PaymentType]
    );
  };

  const onClick = () => {
    setLoading(true);

    const updatePaymentType: bookings.IUpdatePaymentTypeRequest = {
      bookingRequestId: bookingRequestData.id,
      uid: user?.uid,
      paymentType: paymentType,
    };

    bookingService
      .updateBookingRequestPaymentType(updatePaymentType)
      .then((data) => {
        bookingService
          .createBooking({
            bookingRequestId: bookingRequestData.id,
            uid: user.uid,
          })
          .then((response) => {
            const bookingOrderData: bookings.IBookingOrder = response.data;
            if (paymentType === bookings.PaymentType.ONLINE_PAYMENT) {
              window.location.href = bookingOrderData.url;
            } else {
              //
            }

            setTimeout(() => {
              setLoading(false);
              success("Room Booked!");
              next();
            }, 2000);
          })
          .catch((err) => {
            setLoading(false);
            error(err.response.data.message);
          });
      })
      .catch((err) => {
        setLoading(false);
        error(err.response.data.message);
      });
  };

  return (
    <div>
      <div>
        <Segmented
          options={["ONLINE_PAYMENT", "PAY_AT_HOTEL"]}
          block
          onChange={onChange}
        />

        <br />
        {paymentType === bookings.PaymentType.ONLINE_PAYMENT ? (
          <>
            <strong>Online Pay (Stripe):</strong> Securely pay online with your
            credit or debit card through Stripe. Enjoy instant confirmation of
            your booking and a seamless checkout experience. Your payment
            details are encrypted for maximum security.
          </>
        ) : (
          <>
            <strong>Pay at Hotel:</strong> Reserve now and pay later directly at
            the hotel. No upfront payment required. Experience flexibility with
            your payment and enjoy the convenience of settling your bill in
            person during check-in or check-out. Please note that availability
            of this option may vary depending on the hotel's policy.
          </>
        )}
      </div>
      <div className="button">
        <Button type="primary" onClick={prev}>
          <LeftOutlined />
          Back
        </Button>
        <Button type="primary" onClick={onClick} loading={loading}>
          {paymentType === bookings.PaymentType.ONLINE_PAYMENT
            ? "Pay Now"
            : "Book"}
          <RightOutlined />
        </Button>
      </div>
    </div>
  );
}

function BookingReceiptStep({
  bookingRequestId,
  error,
}: {
  bookingRequestId: string;
  error: (message: string) => void;
}): JSX.Element | null {
  const router = useRouter();

  const [bookingData, setBookingData] = useState<bookings.IBookingData | null>(
    null
  );

  useEffect(() => {
    bookingService
      .getBookingDataByBookingRequestId(bookingRequestId)
      .then((data) => {
        setBookingData(data.data);
      })
      .catch((err) => {
        error(err.response.data.message);
      });
  }, [bookingRequestId]);

  return (
    bookingData && (
      <div>
        <Result
          status="success"
          title={`Successfully Booked the Room(${bookingData.roomDetails.title})`}
          subTitle={`Booking Id: ${bookingData.id}, Booking status update my take 1-5 minutes, please wait.`}
          extra={[
            <Button
              type="primary"
              key="console"
              onClick={() => {
                router.push(`/bookings/${bookingData.id}`);
              }}
            >
              Go To Booking
            </Button>,
          ]}
        />
      </div>
    )
  );
}

const prefixSelector = (
  <Form.Item name="prefix" noStyle>
    <Select style={{ width: 70 }}>
      <Option value="91">+91</Option>
    </Select>
  </Form.Item>
);
