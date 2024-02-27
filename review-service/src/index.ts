import express from "express";
import { AddressInfo } from "net";
import "dotenv/config";
import logger from "./config/logger";
import routes from "./routes/routes";
import eurekaClient from "./config/eurekaClientConfig";
import connect from "./config/mongoDBConnect";
const cors = require("cors");

const app = express();

app.use(express.json());
app.use(cors());

const PORT = process.env.PORT;

const server = app.listen(PORT, async () => {
  const serverAddress = server.address() as AddressInfo;
  const serverPort = serverAddress.port;

  routes(app);

  // connect to mongoDB
  await connect();

  // register the service to eureka server
  eurekaClient.start((error: any) => {
    if (error) {
      logger.error(`Error occured during starting the eureka client: ${error}`);
    }
  });

  logger.info(`review-service is running at http://localhost:${serverPort}`);
});
