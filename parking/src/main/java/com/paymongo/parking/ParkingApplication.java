package com.paymongo.parking;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.util.TimeZone;

@SpringBootApplication
@ComponentScan(basePackages = {"com.paymongo.parking"})
public class ParkingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingApplication.class, args);
	}

	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Manila"));
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
