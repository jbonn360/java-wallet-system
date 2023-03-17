package com.betting.javawalletsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class JavaWalletSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaWalletSystemApplication.class, args);
	}

}
