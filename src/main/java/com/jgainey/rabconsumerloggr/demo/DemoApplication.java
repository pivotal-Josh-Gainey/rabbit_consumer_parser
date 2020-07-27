package com.jgainey.rabconsumerloggr.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        MyMain myMain = new MyMain();
        myMain.start();

        try {
            Thread.sleep(15000);
            myMain.aggr();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
