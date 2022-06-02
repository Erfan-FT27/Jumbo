package com.jumbo.customerpanel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.jumbo.map", "com.jumbo.customerpanel"})
public class CustomerPanelApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerPanelApplication.class, args);
    }

}
