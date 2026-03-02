package com.coupon.mgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CouponManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponManagementServiceApplication.class, args);
	}

}
