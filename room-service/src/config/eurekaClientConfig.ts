import AppConstants from "../constants/AppConstants";

const Eureka = require("eureka-js-client").Eureka;

const eurekaClient = new Eureka({
  instance: {
    app: AppConstants.ROOM_SERVICE,
    hostName: "localhost",
    ipAddr: "127.0.0.1",
    status: "UP",
    port: {
      $: 8086,
      "@enabled": true,
    },
    vipAddress: AppConstants.ROOM_SERVICE,
    dataCenterInfo: {
      "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
      name: "MyOwn",
    },
  },
  eureka: {
    host: "localhost",
    port: 8761,
    servicePath: "/eureka/apps",
  },
});

export default eurekaClient;
