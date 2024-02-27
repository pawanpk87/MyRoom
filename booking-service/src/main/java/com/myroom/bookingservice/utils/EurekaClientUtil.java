package com.myroom.bookingservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class EurekaClientUtil {
    @Autowired
    DiscoveryClient discoveryClient;

    public List<ServiceInstance> getServiceInstances(String serviceName) {
        return discoveryClient.getInstances(serviceName);
    }

    public ServiceInstance getServiceInstance(String serviceName) {
        List<ServiceInstance> instances = getServiceInstances(serviceName);
        return instances.get(0);
    }

    public String getServiceInstanceUri(ServiceInstance serviceInstance) {
        return serviceInstance.getUri().toString();
    }

    public String getServiceUri(String serviceName) {
        log.info("fetching {} URI from discovery server", serviceName);
        ServiceInstance serviceInstance = getServiceInstance(serviceName);
        String URI = getServiceInstanceUri(serviceInstance);
        log.info("fetched {} URI: {}", serviceName, URI);
        return URI;
    }
}