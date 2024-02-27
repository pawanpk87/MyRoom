"use client";
import roomService from "@/services/myRoom/rooms/roomService";
import { bookings, rooms } from "@/typings";
import {
  notFound,
  usePathname,
  useRouter,
  useSearchParams,
} from "next/navigation";
import {
  Button,
  Divider,
  Image,
  Tag,
  DatePicker,
  Popover,
  notification,
  Tooltip,
  Alert,
} from "antd";
import dayjs from "dayjs";
import ReviewListItem from "@/components/Rooms/ReviewListItem";
import AdminCard from "@/components/Admin/AdminCard/AdminCard";
import { addValuesToQueryString, formatDayjsDate } from "@/utils/utils";
import React, { useEffect, useState } from "react";
const dateFormat = "YYYY-MM-DD";
import Guests from "@/components/Guests/Guests";
import { addDays } from "date-fns";
import { useUserAuth } from "@/firebase/auth/authProvider";
import bookingService from "@/services/myRoom/booking/bookingService";
import { InfoCircleOutlined, SmileOutlined } from "@ant-design/icons";
import { NotificationInstance } from "antd/es/notification/interface";
import { RangePickerProps } from "antd/es/date-picker";
import OverallRatingBox from "@/components/OverallRatingBox/OverallRatingBox";
import reviewService from "@/services/myRoom/reviews/reviewService";
import { reviews } from "@/typings/review";
import "./roomdata.css";

export default function Room({
  params,
}: {
  params: {
    id: string;
  };
}) {
  const id = params.id;

  const [toastApi, toastContextHolder] = notification.useNotification();

  const [room, setRoom] = React.useState<rooms.IRoomData | null>(null);

  useEffect(() => {
    getRoom(id)
      .then((data) => {
        setRoom(data);
      })
      .catch((error: any) => {
        notFound();
      });
  }, [id]);

  return (
    room && (
      <div className="roomdata">
        <RoomHeader room={room} />

        <Divider />

        <Description room={room} toastApi={toastApi} />

        <Divider />

        {toastContextHolder}
      </div>
    )
  );
}

function RoomHeader({ room }: { room: rooms.IRoomData }): JSX.Element {
  return (
    <div className="roomdata_header">
      <Image
        className="img"
        alt={room.title}
        width={600}
        height={380}
        src={room.mainImage}
        fallback="data:image/png;base64,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"
      />
      <div className="details">
        <div className="title">
          <h2>{room.title}</h2>
        </div>

        <div className="city">
          <div>
            <svg
              fill="#000000"
              version="1.1"
              id="Capa_1"
              width="20px"
              height="20px"
              viewBox="0 0 395.71 395.71"
              xmlSpace="preserve"
            >
              <g>
                <path
                  d="M197.849,0C122.131,0,60.531,61.609,60.531,137.329c0,72.887,124.591,243.177,129.896,250.388l4.951,6.738
		c0.579,0.792,1.501,1.255,2.471,1.255c0.985,0,1.901-0.463,2.486-1.255l4.948-6.738c5.308-7.211,129.896-177.501,129.896-250.388
		C335.179,61.609,273.569,0,197.849,0z M197.849,88.138c27.13,0,49.191,22.062,49.191,49.191c0,27.115-22.062,49.191-49.191,49.191
		c-27.114,0-49.191-22.076-49.191-49.191C148.658,110.2,170.734,88.138,197.849,88.138z"
                />
              </g>
            </svg>
          </div>
          <div>{room.city}</div>
        </div>

        <div className="address">
          <h3>{room.address}</h3>
        </div>

        <Divider />

        <AdminCard uid={room.createdBy!} organizationId={room.organizationId} />
      </div>
    </div>
  );
}

function Description({
  room,
  toastApi,
}: {
  room: rooms.IRoomData;
  toastApi: NotificationInstance;
}): JSX.Element {
  return (
    <div className="description">
      <div className="d1">
        <div className="description_card">
          <div>{room.description}</div>
        </div>

        <Divider />

        <div className="description_card">
          <div className="description_title">
            <h2>Amenities</h2>
          </div>
          <div>
            {room.amenities.map((amenitie, index) => (
              <Tag
                key={index}
                style={{
                  margin: "10px",
                  fontSize: "medium",
                }}
              >
                {amenitie}
              </Tag>
            ))}
          </div>
        </div>

        <Divider />

        <div className="description_card">
          <div className="description_title">
            <h2>Features</h2>
          </div>
          <div>
            {room.features.map((feature, index) => (
              <Tag
                key={index}
                style={{
                  margin: "10px",
                  fontSize: "medium",
                }}
              >
                {feature}
              </Tag>
            ))}
          </div>
        </div>

        {room.note && (
          <>
            <Divider />
            <div className="description_card">
              <div>
                <Alert
                  message="Note"
                  description={room.note}
                  type="warning"
                  showIcon
                />
              </div>
            </div>
          </>
        )}

        <RoomReviews room={room} />
      </div>

      <BookingCard room={room} toastApi={toastApi} />
    </div>
  );
}

function BookingCard({
  room,
  toastApi,
}: {
  room: rooms.IRoomData;
  toastApi: NotificationInstance;
}): JSX.Element {
  const router = useRouter();
  const pathname = usePathname();
  const searchParam = useSearchParams();

  const { user } = useUserAuth();

  const [dates, setDates] = useState<Date[]>([
    new Date(),
    addDays(new Date(), 1),
  ]);
  const [guestsList, setGuestsList] = React.useState<bookings.IGuestList>({
    adults: 1,
    children: 0,
  });
  const [isLoading, setIsLoading] = React.useState<boolean>(false);

  useEffect(() => {
    if (searchParam.get("availableDates")) {
      const searchParamDates = JSON.parse(searchParam.get("availableDates")!);
      setDates([
        new Date(searchParamDates.startDate.startDate),
        new Date(searchParamDates.endDate.endDate),
      ]);
    }

    if (searchParam.get("adults")) {
      setGuestsList({
        ...guestsList,
        adults: Number(searchParam.get("adults")),
      });
    }

    if (searchParam.get("children")) {
      setGuestsList({
        ...guestsList,
        children: Number(searchParam.get("children")),
      });
    }
  }, [searchParam]);

  useEffect(() => {
    const query = addValuesToQueryString(searchParam, [
      {
        key: "availableDates",
        value: JSON.stringify({
          startDate: {
            operator: rooms.QueryRelationalOperatorEnum.GREATER_THAN_OR_EQUAL,
            startDate: dates[0],
          },
          endDate: {
            operator: rooms.QueryRelationalOperatorEnum.LESS_THAN_OR_EQUAL,
            endDate: dates[1],
          },
        }),
      },
      {
        key: "capacity",
        value: JSON.stringify({
          operator: rooms.QueryRelationalOperatorEnum.GREATER_THAN_OR_EQUAL,
          capacity: guestsList.adults + guestsList.children,
        }),
      },

      {
        key: "adults",
        value: guestsList.adults.toString(),
      },

      {
        key: "children",
        value: guestsList.children.toString(),
      },
    ]);

    router.push(pathname + "?" + query);
  }, [dates, guestsList]);

  const onClickBookNow = () => {
    if (room && user) {
      const createBookingRequestBody: bookings.ICreateBookingRequest = {
        roomId: room.id,
        checkIn: dates[0].toISOString(),
        checkOut: dates[1].toISOString(),
        guests: guestsList,
        uid: user.uid,
      };
      bookingService
        .createBookingRequest(createBookingRequestBody)
        .then((response) => {
          setIsLoading(true);
          router.push(`/booking-request/${response.data.id}`);
        })
        .catch((error) => {
          setIsLoading(false);
          toastApi.open({
            message: "Error",
            description: error.response.data.message,
            icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
          });
        });
    } else if (!user) {
      setIsLoading(false);
      toastApi.open({
        message: "Info",
        description: "Please Log in",
        icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
      });
    } else {
      setIsLoading(false);
      toastApi.open({
        message: "Info",
        description: "Something went wrong. Please refresh the page",
        icon: <InfoCircleOutlined style={{ color: "#108ee9" }} />,
      });
    }
  };

  const range = (start: number, end: number) => {
    const result = [];
    for (let i = start; i < end; i++) {
      result.push(i);
    }
    return result;
  };

  const disabledDate: RangePickerProps["disabledDate"] = (current) => {
    return current && current <= dayjs().endOf("day");
  };

  const disabledDateTime = () => ({
    disabledHours: () => range(0, 24).splice(4, 20),
    disabledMinutes: () => range(30, 60),
    disabledSeconds: () => [55, 56],
  });

  const disabledRangeTime: RangePickerProps["disabledTime"] = (_, type) => {
    if (type === "start") {
      return {
        disabledHours: () => range(0, 60).splice(4, 20),
        disabledMinutes: () => range(30, 60),
        disabledSeconds: () => [55, 56],
      };
    }
    return {
      disabledHours: () => range(0, 60).splice(20, 4),
      disabledMinutes: () => range(0, 31),
      disabledSeconds: () => [55, 56],
    };
  };

  return (
    <div className="d2">
      <div className="cost">
        <h1>₹{room.cost}</h1>
      </div>

      <div className="dates">
        <div className="dates_header">
          <h3>Dates</h3>
        </div>

        <DatePicker.RangePicker
          style={{ width: "100%", height: "50px" }}
          disabledDate={disabledDate}
          disabledTime={disabledRangeTime}
          value={[
            formatDayjsDate(dates[0].toISOString(), dateFormat),
            formatDayjsDate(dates[1].toISOString(), dateFormat),
          ]}
          onChange={(values) => {
            if (values && values.length > 0) {
              setDates([values[0]!.toDate(), values[1]!.toDate()]);
            }
          }}
        />
      </div>

      <GuestList guestsList={guestsList} setGuestsList={setGuestsList} />

      <div className="priceDetails">
        <div className="priceDetails_header">
          <h3>Price Details</h3>
        </div>
        <div className="prices">
          <div className="item_prices">
            <span>Price</span>
            <strong>
              <Tag color="#2db7f5"> ₹{room.prices.price}</Tag>
            </strong>
          </div>

          <div className="item_prices">
            <span>Cleaning Fee</span>
            <strong>
              <Tag color="#87d068"> ₹{room.prices.cleaningFee}</Tag>
            </strong>
          </div>

          <div className="item_prices">
            <span>Room Service</span>
            <strong>
              <Tag color="#108ee9"> ₹{room.prices.roomService}</Tag>
            </strong>
          </div>
        </div>
      </div>

      <div>
        <Tooltip
          title={
            room.bookingStatus !== rooms.BookingStatus.AVAILABLE
              ? "Room is currentyly unavailable!"
              : ""
          }
        >
          <Button
            type="primary"
            block
            onClick={onClickBookNow}
            loading={isLoading}
            disabled={room.bookingStatus !== rooms.BookingStatus.AVAILABLE}
          >
            Continue to Book
          </Button>
        </Tooltip>
      </div>
    </div>
  );
}

function GuestList({
  guestsList,
  setGuestsList,
}: {
  guestsList: bookings.IGuestList;
  setGuestsList: React.Dispatch<React.SetStateAction<bookings.IGuestList>>;
}): JSX.Element {
  const title: string = "Guests";

  return (
    <div className="guests">
      <Popover
        placement="bottomLeft"
        title={title}
        content={
          <Guests guestsList={guestsList} setGuestsList={setGuestsList} />
        }
        arrow={false}
        trigger="hover"
      >
        <strong> {guestsList.adults + guestsList.children} Guests</strong>
      </Popover>
    </div>
  );
}

function RoomReviews({ room }: { room: rooms.IRoomData }): JSX.Element | null {
  const [ratingData, setReivew] = React.useState<reviews.IOverallRating | null>(
    null
  );

  useEffect(() => {
    reviewService
      .getOverallRating(room.id)
      .then((data) => {
        setReivew(data.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [room.id]);

  return ratingData && ratingData.overall !== null ? (
    <div className="description_card">
      <div className="description_title">
        <h2>Ratings and reviews</h2>
      </div>
      <div>
        <div className="rating_reviews">
          <OverallRatingBox ratingData={ratingData} />
          <div>
            <ReviewListItem roomId={room.id} />
          </div>
        </div>
      </div>
    </div>
  ) : null;
}

async function getRoom(id: string): Promise<rooms.IRoomData> {
  try {
    const room: rooms.IRoomData = (await roomService.getRoom(id)).data;
    return room;
  } catch (error: any) {
    throw error;
  }
}
