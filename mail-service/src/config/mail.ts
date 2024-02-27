import logger from "./logger";
import { IMailOptions } from "./types";

const nodemailer = require("nodemailer");

export const transporter = nodemailer.createTransport({
  host: "smtp.gmail.com",
  port: 587,
  secure: false,
  auth: {
    user: process.env.MAIL_USER,
    pass: process.env.MAIL_PASSWORD,
  },
});

export async function sendMail(mailOptions: IMailOptions): Promise<any> {
  return await transporter.sendMail(mailOptions);
}
