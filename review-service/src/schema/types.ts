import { z } from "zod";
import { addReviewSchema, getAllReviewSchema } from "./review.schema";

type reviewAddSchemaType = z.infer<typeof addReviewSchema>["body"];
type reviewGetAllReviewType = z.infer<typeof getAllReviewSchema>["query"];

export type addReviewInput = reviewAddSchemaType;
export type getAllReviewQuery = reviewGetAllReviewType;
