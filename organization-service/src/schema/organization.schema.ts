import { z } from "zod";

export const createOrganizationSchema = z.object({
  body: z
    .object({
      name: z.string({
        required_error: "Name is required",
      }),

      email: z
        .string({
          required_error: "Email is required",
        })
        .email("Not valid email"),

      description: z
        .string({
          required_error: "Organization description is required",
        })
        .min(10)
        .max(1000),

      phone: z
        .string({
          required_error: "Phone is required",
        })
        .min(13)
        .max(13),

      businessProfile: z.object({
        businessType: z
          .string({
            required_error: "business type is required",
          })
          .default("individual"),

        category: z
          .string({
            required_error: "business category type is required",
          })
          .default("housing"),

        subcategory: z
          .string({
            required_error: "business subcategory type is required",
          })
          .default("space_rental"),

        addresses: z.object({
          street1: z.string({
            required_error: "street1 is required",
          }),

          street2: z.string().default(""),

          city: z.string({
            required_error: "city is required",
          }),

          state: z.string({
            required_error: "state is required",
          }),

          postalCode: z.string({
            required_error: "postalCode is required",
          }),

          country: z
            .string({
              required_error: "Country is required",
            })
            .default("IN"),
        }),
      }),

      uid: z.string({
        required_error: "User uid is required",
      }),
    })
    .strict(),
});

export const updateOrganizationSchema = z.object({
  body: z
    .object({
      id: z.string({
        required_error: "organization id is required",
      }),

      name: z.optional(
        z.string({
          required_error: "Name is required",
        })
      ),

      description: z.optional(
        z
          .string({
            required_error: "Organization description is required",
          })
          .min(10)
          .max(1000)
      ),

      phone: z.optional(
        z
          .string({
            required_error: "Phone is required",
          })
          .min(10)
          .max(13)
      ),

      businessProfile: z.optional(
        z.object({
          addresses: z.object({
            street1: z.optional(
              z.string({
                required_error: "street1 is required",
              })
            ),

            street2: z.optional(z.string()),

            city: z.optional(
              z.string({
                required_error: "city is required",
              })
            ),

            state: z.optional(
              z.string({
                required_error: "state is required",
              })
            ),

            postalCode: z.optional(
              z.string({
                required_error: "postalCode is required",
              })
            ),

            country: z.optional(
              z
                .string({
                  required_error: "Country is required",
                })
                .default("IN")
            ),
          }),
        })
      ),

      uid: z.string({
        required_error: "User uid is required",
      }),
    })
    .strict(),
});

export const getAllOrganizationsQuerySchema = z.object({
  body: z
    .object({
      _id: z.optional(z.string()),

      name: z.optional(z.string()),

      page: z.optional(z.number()),

      limit: z.optional(z.number()),
    })
    .strict(),
});
