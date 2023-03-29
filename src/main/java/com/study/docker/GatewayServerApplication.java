package com.study.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayServerApplication {

    public static void main(String[] args) {
        testMethods();
        SpringApplication.run(GatewayServerApplication.class, args);
    }

    public static void testMethods() {
        System.out.println("ENTRANDO A UN METODO DE PRUEBA");
    }

}
