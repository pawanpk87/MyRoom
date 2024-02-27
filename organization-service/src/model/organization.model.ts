import mongoose, { Schema, model } from "mongoose";
import { Organization } from "./types";

export interface OrganizationDocument extends Organization, mongoose.Document {
  createdAt: Date;
  updatedAt: Date;
}

const organizationSchema = new Schema<Organization>(
  {
    name: {
      type: String,
      required: true,
      unique: true,
    },

    email: {
      type: String,
      required: true,
      unique: true,
    },

    description: {
      type: String,
      minLength: 10,
      maxLength: 1000,
    },

    phone: {
      type: String,
      required: true,
      unique: true,
      minLength: 13,
      maxLength: 13,
    },

    businessProfile: {
      _id: false,
      type: {
        businessType: {
          type: String,
          required: true,
        },

        category: {
          type: String,
          required: true,
        },

        subcategory: {
          type: String,
          required: true,
        },

        addresses: {
          _id: false,
          type: {
            street1: {
              type: String,
              required: true,
            },

            street2: {
              type: String,
            },

            city: {
              type: String,
              required: true,
            },

            state: {
              type: String,
              required: true,
            },

            postalCode: {
              type: String,
              required: true,
            },

            country: {
              type: String,
              default: "IN",
              required: true,
            },
          },
          required: true,
        },
      },
    },

    superAdmin: {
      _id: false,
      type: {
        uid: String,
        adminType: String,
      },
      required: true,
    },

    admins: {
      type: [
        {
          _id: false,
          uid: String,
          adminType: String,
        },
      ],
      default: [],
      required: false,
    },
  },
  {
    timestamps: true,
  }
);

organizationSchema.pre("save", async function (next) {
  if (this.superAdmin === null || this.superAdmin === undefined) {
    throw new Error("Invalid superAdmin value");
  }
  next();
});

const OrganizationModel = model<Organization>(
  "Organization",
  organizationSchema
);

export default OrganizationModel;
