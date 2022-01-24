/*
 * Since we are using fiegn client with api gateway where api-gateway is doing the loadBalancer
 * then we don't need this loadBalancer class
 * */


/*
package com.miaoubich.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;

import feign.Feign;

@LoadBalancerClient(value = "address-service")
public class AddressServiceLoadBalancerConfig {

	@LoadBalanced
	@Bean
	public Feign.Builder feignBuilder(){
		return Feign.builder();
	}
}
*/
