import {
  Consumer,
  ConsumerSubscribeTopics,
  Kafka,
  EachMessagePayload,
} from "kafkajs";
import logger from "../logger";
import { sendBookingMailService } from "../../service";

export default class BookingMailConsumer {
  private kafkaConsumer: Consumer;
  private messageProcessor: any;

  public constructor(messageProcessor: any) {
    this.messageProcessor = messageProcessor;
    this.kafkaConsumer = this.createKafkaConsumer();
  }

  public async startConsumer(): Promise<void> {
    const topic: ConsumerSubscribeTopics = {
      topics: ["booking.mail"],
      fromBeginning: false,
    };
    try {
      await this.kafkaConsumer.connect();
      await this.kafkaConsumer.subscribe(topic);
      await this.kafkaConsumer.run({
        eachMessage: async (messagePayload: EachMessagePayload) => {
          logger.info(
            `Received booking mail event (kafka topic: 'booking.mail') with payload: `,
            messagePayload
          );

          const { topic, partition, message } = messagePayload;

          await sendBookingMailService(message);
        },
      });
    } catch (error) {
      logger.error("Error: ", error);
    }
  }

  public async shutdown(): Promise<void> {
    await this.kafkaConsumer.disconnect();
  }

  private createKafkaConsumer(): Consumer {
    const kafka = new Kafka({
      clientId: "booking.mail",
      brokers: ["localhost:9092"],
    });
    const consumer = kafka.consumer({ groupId: "booking.mail-group" });
    return consumer;
  }
}
