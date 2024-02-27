import { z } from "zod";

const queryRelationalOperatorEnum = z.enum([
  "EQUAL",
  "NOT_EQUAL",
  "GREATER_THAN",
  "LESS_THAN",
  "GREATER_THAN_OR_EQUAL",
  "LESS_THAN_OR_EQUAL",
  "IN",
]);

export const addReviewSchema = z.object({
  body: z
    .object({
      roomId: z.string({
        required_error: "roomId is required",
      }),

      organizationId: z.string({
        required_error: "organizationId is required",
      }),

      uid: z.string({
        required_error: "uid is required",
      }),

      reviewText: z.string({
        required_error: "review text is required",
      }),

      rating: z.number({
        required_error: "rating is required",
      }),
    })
    .strict(),
});

export const getAllReviewSchema = z.object({
  query: z
    .object({
      roomId: z.optional(z.string()),

      organizationId: z.optional(z.string()),

      rating: z.optional(
        z.object({
          operator: queryRelationalOperatorEnum,
          rating: z.number(),
        })
      ),

      uid: z.optional(z.string()),

      sorting: z.optional(
        z.array(
          z.object({
            operator: z.number().min(-1).max(1),
            field: z.string(),
          })
        )
      ),

      page: z.optional(z.string()),

      limit: z.optional(z.string()),
    })
    .strip(),
});
