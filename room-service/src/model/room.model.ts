import mongoose, { Schema, model } from "mongoose";
import { BookingStatus, CheckInType, Room } from "./types";

export interface RoomDocument extends Room, mongoose.Document {
  createdAt: Date;
  updatedAt: Date;
}

const roomSchema = new Schema<Room>(
  {
    organizationId: {
      type: String,
      required: true,
    },

    title: {
      type: String,
      required: true,
      unique: true,
    },

    description: {
      type: String,
      minLength: 10,
      maxLength: 1000,
    },

    availableDates: {
      _id: false,
      type: {
        startDate: Date,
        endDate: Date,
      },
      required: true,
    },

    prices: {
      _id: false,
      type: {
        price: Number,
        cleaningFee: Number,
        roomService: Number,
        currency: String,
      },
      required: true,
    },

    cost: {
      type: Number,
    },

    createdBy: {
      type: String,
      required: true,
    },

    updatedBy: {
      type: String,
      required: true,
    },

    note: {
      type: String,
    },

    rating: {
      _id: false,
      type: {
        rating: {
          type: Number,
          default: null,
        },
        outOf: Number,
      },
      default: {
        rating: null,
        outOf: 5,
      },
    },

    capacity: {
      type: Number,
      required: true,
    },

    bookingStatus: {
      type: String,
      enum: Object.values(BookingStatus),
      default: BookingStatus.AVAILABLE,
    },

    reviewId: {
      type: String,
      required: false,
    },

    amenities: {
      type: [String],
      default: [],
    },

    mainImage: {
      type: String,
      required: true,
    },

    images: {
      type: [String],
      default: [],
    },

    features: {
      type: [String],
      default: [],
    },

    city: {
      type: String,
      required: true,
    },

    address: {
      type: String,
      required: true,
    },

    checkInTypes: {
      type: [
        {
          type: String,
          enum: Object.values(CheckInType),
        },
      ],
      required: true,
    },

    bookingCount: {
      type: Number,
      required: false,
    },
  },
  {
    timestamps: true,
  }
);

roomSchema.pre("save", function (next) {
  const room = this as Room;
  room.cost = calculateAndAssignRent(room);
  next();
});

roomSchema.pre("findOneAndUpdate", async function () {
  const room = (await this.model.findOne(this.getQuery())) as Room;
  this.set({ cost: calculateAndAssignRent(room) });
});

function calculateAndAssignRent(room: Room) {
  const { price, cleaningFee, roomService } = room.prices;
  const cost = Number(price) + Number(cleaningFee) + Number(roomService);
  return cost;
}

const RoomModel = model<Room>("Room", roomSchema);

export default RoomModel;
