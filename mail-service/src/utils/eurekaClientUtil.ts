import eurekaClient from "../config/eurekaClientConfig";
import logger from "../config/logger";
import ApiConstants from "../constants/ApiConstants";

export function getInstanceURI(serviceName: string): string | null {
  logger.info(`fetching ${serviceName} URI from discovery server`);

  const instances: [any] = eurekaClient.getInstancesByAppId(serviceName);
  let URI: string | null = null;

  if (instances && instances.length > 0) {
    const instance = instances[0];

    const isSecure = instance.securePort["@enabled"] === "true";
    const protocol = isSecure ? "https" : "http";
    const ipAddr = instance.ipAddr;
    const port = instance.port.$;

    URI = `${protocol}://${ipAddr}:${port}`;
    // Alternatively, we can use the instance's homePageUri
    // URI = instance["homePageUrI"];
  } else {
    logger.error(`Service ${serviceName} is not available.`);

    throw {
      name: "AppError",
      errorCode: ApiConstants.SERVICE_UNAVAILABLE,
      message: `Service ${serviceName} is not available.`,
      details: "Not registered on the Eureka server",
      status: 500,
    };
  }
  logger.info(`fetched ${serviceName} URI: ${URI}`);
  return URI;
}
