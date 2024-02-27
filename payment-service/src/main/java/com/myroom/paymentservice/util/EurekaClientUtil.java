package com.myroom.paymentservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EurekaClientUtil {
    @Autowired
    DiscoveryClient discoveryClient;

    public List<ServiceInstance> getServiceInstances(String serviceName){
        return  discoveryClient.getInstances(serviceName);
    }

    public ServiceInstance getServiceInstance(String serviceName){
        List<ServiceInstance> instances= getServiceInstances(serviceName);
        return instances.get(0);
    }

    public String getServiceInstanceUri(ServiceInstance serviceInstance){
        return serviceInstance.getUri().toString();
    }

    public String getServiceUri(String serviceName){
        ServiceInstance serviceInstance = getServiceInstance(serviceName);
        return getServiceInstanceUri(serviceInstance);
    }
}
