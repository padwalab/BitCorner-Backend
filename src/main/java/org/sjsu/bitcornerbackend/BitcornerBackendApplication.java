package org.sjsu.bitcornerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BitcornerBackendApplication {

	public static void main(String[] args) {
		System.out.println("test reload abc");
		SpringApplication.run(BitcornerBackendApplication.class, args);
	}

}
