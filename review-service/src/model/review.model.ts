import mongoose, { Schema, model } from "mongoose";
import { Review } from "./types";

export interface RoomDocument extends Review, mongoose.Document {
  createdAt: Date;
  updatedAt: Date;
}

const reviewSchema = new Schema<Review>(
  {
    roomId: {
      type: String,
      required: true,
    },

    organizationId: {
      type: String,
      required: true,
    },

    uid: {
      type: String,
      required: true,
    },

    reviewText: {
      type: String,
      required: true,
    },

    rating: {
      type: Number,
      required: true,
    },
  },
  {
    timestamps: true,
  }
);

const ReviewModel = model<Review>("Review", reviewSchema);

export default ReviewModel;
