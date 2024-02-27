import { z } from "zod";

const bookingStatusEnum = z.enum([
  "AVAILABLE",
  "BOOKED",
  "OCCUPIED",
  "MAINTENANCE",
]);

const currencyEnum = z.enum(["inr"]);

const checkInTypeEnum = z.enum(["ONLINE_PAYMENT", "PAY_AT_HOTEL"]);

const queryRelationalOperatorEnum = z.enum([
  "EQUAL",
  "NOT_EQUAL",
  "GREATER_THAN",
  "LESS_THAN",
  "GREATER_THAN_OR_EQUAL",
  "LESS_THAN_OR_EQUAL",
  "IN",
]);

export const createRoomSchema = z.object({
  body: z
    .object({
      organizationId: z.string({
        required_error: "Organization id is required",
      }),

      title: z.string({
        required_error: "Title is required",
      }),

      description: z
        .string({
          required_error: "Organization description is required",
        })
        .min(10)
        .max(1000),

      availableDates: z.object({
        startDate: z.coerce.date({
          required_error: "start date is required",
        }),

        endDate: z.coerce.date({
          required_error: "end date is required",
        }),
      }),

      prices: z.object({
        price: z.number({
          required_error: "price is required",
        }),

        cleaningFee: z.number({
          required_error: "cleaning fee is required",
        }),

        roomService: z.number({
          required_error: "room service fee is required",
        }),

        currency: currencyEnum,
      }),

      uid: z.string({
        required_error: "adminId is required",
      }),

      note: z.string(),

      capacity: z.number({
        required_error: "capacity is required",
      }),

      bookingStatus: bookingStatusEnum,

      amenities: z.array(z.string()).default([]),

      mainImage: z.string({
        required_error: "mainImage is required",
      }),

      images: z.array(z.string()).default([]),

      features: z.array(z.string()).default([]),

      city: z.string({
        required_error: "city is required",
      }),

      address: z.string({
        required_error: "address is required",
      }),

      checkInTypes: z.array(checkInTypeEnum),
    })
    .strict(),
});

export const updateRoomSchema = z.object({
  body: z
    .object({
      id: z.string({
        required_error: "room id is required",
      }),

      uid: z.string({
        required_error: "user id is required",
      }),

      title: z.optional(z.string()),

      description: z.optional(z.string().min(10).max(1000)),

      availableDates: z.optional(
        z.object({
          startDate: z.coerce.date(),

          endDate: z.coerce.date(),
        })
      ),

      prices: z.optional(
        z.object({
          price: z.number(),

          cleaningFee: z.number(),

          roomService: z.number(),

          currency: currencyEnum,
        })
      ),

      note: z.optional(z.string()),

      capacity: z.optional(z.number()),

      bookingStatus: z.optional(bookingStatusEnum),

      amenities: z.optional(z.array(z.string())),

      mainImage: z.optional(z.string()),

      images: z.optional(z.array(z.string())),

      features: z.optional(z.array(z.string())),

      city: z.optional(z.string()),

      address: z.optional(z.string()),

      checkInTypes: z.optional(z.array(checkInTypeEnum)),
    })
    .strict(),
});

export const deleteRoomSchema = z.object({
  body: z
    .object({
      id: z.string({
        required_error: "room id is required",
      }),

      uid: z.string({
        required_error: "adminId is required",
      }),
    })
    .strict(),
});

export const roomAvailabilitySchema = z.object({
  body: z
    .object({
      id: z.optional(z.string()), // roomId

      checkIn: z.coerce.date({
        required_error: "checkIn date is required",
      }),

      checkOut: z.coerce.date({
        required_error: "checkOut date is required",
      }),

      guests: z.number({
        required_error: "guest is required",
      }),
    })
    .strict(),
});

export const getAllRoomQuerySchema = z.object({
  query: z
    .object({
      organizationId: z.optional(z.string()),

      title: z.optional(z.string()),

      availableDates: z.optional(
        z.object({
          startDate: z.optional(
            z.object({
              operator: queryRelationalOperatorEnum,
              startDate: z.coerce.date(),
            })
          ),

          endDate: z.optional(
            z.object({
              operator: queryRelationalOperatorEnum,
              endDate: z.coerce.date(),
            })
          ),
        })
      ),

      cost: z.optional(
        z.object({
          operator: queryRelationalOperatorEnum,
          cost: z.number(),
        })
      ),

      rating: z.optional(
        z.object({
          rating: z.optional(
            z.object({
              operator: queryRelationalOperatorEnum,
              rating: z.number(),
            })
          ),

          outOf: z.optional(
            z.object({
              operator: queryRelationalOperatorEnum,
              rating: z.number(),
            })
          ),
        })
      ),

      capacity: z.optional(
        z.object({
          operator: queryRelationalOperatorEnum,
          capacity: z.number(),
        })
      ),

      bookingStatus: z.optional(bookingStatusEnum),

      amenities: z.optional(z.string()),

      features: z.optional(z.string()),

      city: z.optional(z.string()),

      checkInTypes: z.optional(z.array(checkInTypeEnum)),

      sorting: z.optional(
        z.array(
          z.object({
            operator: z.number().min(-1).max(1),
            field: z.string(),
          })
        )
      ),

      page: z.optional(z.number()),

      limit: z.optional(z.number()),
    })
    .strip(),
});

export const deleteAllRoomSchema = z.object({
  body: z
    .object({
      ids: z.array(
        z.string({
          required_error: "id is required",
        })
      ),

      uid: z.string({
        required_error: "adminId is required",
      }),
    })
    .strict(),
});

export const createReviewSchema = z.object({
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

export const getRoomsCountByStatusSchema = z.object({
  query: z
    .object({
      organizationId: z.optional(z.string()),

      bookingStatus: z.optional(bookingStatusEnum),
    })
    .strip(),
});

export const updateRoomSchemaForBooking = z.object({
  body: z
    .object({
      id: z.string({
        required_error: "room id is required",
      }),

      uid: z.string({
        required_error: "user id is required",
      }),

      bookingStatus: z.optional(bookingStatusEnum),
    })
    .strict(),
});

export const getTopPerformersRoomsQuerySchema = z.object({
  query: z
    .object({
      organizationId: z.optional(z.string()),
    })
    .strip(),
});
