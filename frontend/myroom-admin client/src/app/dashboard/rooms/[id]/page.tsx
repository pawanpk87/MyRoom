import roomService from "@/services/myRoom/room/roomService";
import { rooms } from "@/typings";
import { notFound } from "next/navigation";
import { Alert, Divider, Image, Rate, Tag } from "antd";
import ReviewListItem from "@/components/Reviews/ReviewListItem";
import AdminCard from "@/components/Admin/AdminCard/AdminCard";
import Link from "antd/es/typography/Link";
import { formatShortDate } from "@/utils/utils";
import "./roomdata.css";
import RoomReviews from "@/components/Rooms/RoomReviews/RoomReviews";

async function getRoom(id: string): Promise<rooms.IRoomData> {
  try {
    const room: rooms.IRoomData = (await roomService.getRoom(id)).data;
    return room;
  } catch (error: any) {
    notFound();
  }
}

export default async function Room({
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

  const room: rooms.IRoomData = await getRoom(id);

  return (
    <div className="roomdata">
      <div className="header">
        <Image
          className="img"
          alt={room.title}
          width={600}
          height={380}
          src={room.mainImage}
          fallback="data:image/png;base64,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"
        />
        <div className="details">
          <AdminCard id={room.createdBy} />

          <div className="id">
            <span>Id: </span>
            <span>{room.id}</span>
          </div>

          <div className="title">
            <h2>{room.title}</h2>
          </div>

          <div className="address">
            <h3>{room.address}</h3>
          </div>

          <div className="action">
            <Link href={`/dashboard/rooms/manage?id=${id}&type=Edit Room`}>
              Edit Room
            </Link>
            <Link href={`/dashboard/rooms/manage?id=${id}&type=Delete Room`}>
              Delete Room
            </Link>
          </div>
        </div>
      </div>

      <Divider />

      <div className="description">
        <div className="d1">
          <div className="room_description">
            <div>
              <h2>Description</h2>
            </div>
            <div>{room.description}</div>
          </div>

          <Divider />

          <div className="desCard">
            <div>
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

          <div className="desCard">
            <div>
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
              <div className="desCard">
                <div>
                  <h2>Note</h2>
                </div>
                <div>{room.note}</div>
              </div>
            </>
          )}

          <RoomReviews room={room} />
        </div>

        <div className="d2">
          <div className="cost">
            <h1>₹{room.cost}</h1>
          </div>

          <div className="availableDates">
            <div className="availableDates_header">
              <h3>Available Dates</h3>
            </div>
            <div className="dates">
              <div>
                <Tag color="success">
                  {formatShortDate(room.availableDates.startDate)}
                </Tag>
              </div>
              To
              <div>
                <Tag color="success">
                  {formatShortDate(room.availableDates.endDate)}
                </Tag>
              </div>
            </div>
          </div>

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

          <div className="capacity">
            <div className="capacity_header">
              <h3>Capacity</h3>
            </div>
            <div className="capacity_details">
              <span>Capacity</span>
              <span>
                <Tag color="#f50">{room.capacity}</Tag>
              </span>
            </div>
          </div>
        </div>
      </div>

      <Divider />
    </div>
  );
}
