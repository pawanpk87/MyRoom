import { KafkaMessage } from "kafkajs";
import logger from "../config/logger";
import { IBookingData, IUser } from "./types";
import { getBookingDetails } from "./booking.service";
import { getUserDetails } from "./auth.service";
import { IMailOptions, sendMail } from "../config";
import { formatShortDate } from "../utils";

export async function sendBookingMailService(message: KafkaMessage) {
  logger.info("Handling booking mail event");
  const payload = JSON.parse(message.value?.toString()!);

  const bookingData: IBookingData = await getBookingDetails(payload.bookingId);
  const userData: IUser = await getUserDetails(bookingData.uid);

  const bookingMessage = `
  <div>
    <h1>Booking Confirmation</h1>
    <p>Dear ${userData.name},</p>
    <p>Your booking with ID ${bookingData.id} has been confirmed!</p>
    <p>Booking Details:</p>
    <ul>
      <li>Check-in Date: ${formatShortDate(new Date(bookingData.checkIn))}</li>
      <li>Check-out Date: ${formatShortDate(
        new Date(bookingData.checkOut)
      )}</li>
      <li>Room Details:
        <ul>
          <li>Room ID: ${bookingData.roomDetails.id}</li>
          <li>Room Title: ${bookingData.roomDetails.title}</li>
          <li>Price: ${bookingData.amount}${
    bookingData.roomDetails.prices.currency
  }</li>
        </ul>
      </li>
    </ul>
    <p>Thank you for choosing our service. We look forward to hosting you!</p>
    <p>Best regards,</p>
    <p>The MyRoom Team</p>
  </div>
  `;

  const mailData: IMailOptions = {
    from: {
      name: "MyRoom",
      address: process.env.MAIL_USER!,
    },
    to: userData.email,
    subject: `Booking Confirmed for ${bookingData.roomDetails.title}`,
    html: bookingMessage,
  };

  logger.info(`Sending a mail`);
  await sendMail(mailData);
  logger.info(`Message sent successfully!`);
}
