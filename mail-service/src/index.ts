import express from "express";
import { AddressInfo } from "net";
import "dotenv/config";
import logger from "./config/logger";
import eurekaClient from "./config/eurekaClientConfig";
import BookingMailConsumer from "./config/kafka/BookingMailConsumer";
import { transporter } from "./config/mail";
const cors = require("cors");

const app = express();

app.use(express.json());
app.use(cors());

const PORT = process.env.PORT;

const server = app.listen(PORT, async () => {
  const serverAddress = server.address() as AddressInfo;
  const serverPort = serverAddress.port;

  // Instantiate the Kafka consumer
  const bookingMailConsumer = new BookingMailConsumer({});
  // start the Kafka consumer
  try {
    await bookingMailConsumer.startConsumer();
    logger.info(`Kafka consumer started successfully`);
  } catch (error: any) {
    logger.error(`Error occured during starting Kafka consumer: ${error}`);
  }

  // Start the mail server
  transporter.verify(function (error: any, success: any) {
    if (error) {
      logger.error(`Error occurred while starting the mail server: `, error);
    } else {
      logger.info("Server is ready to take our messages!");
    }
  });

  // register the service to eureka server
  eurekaClient.start((error: any) => {
    if (error) {
      logger.error(`Error occured during starting the eureka client: ${error}`);
    }
  });

  logger.info(`mail-service is running at http://localhost:${serverPort}`);
});
