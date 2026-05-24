package com.zode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages ={"com.zode","com.jfinal","com.common",})
public class ZodeWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZodeWebsiteApplication.class, args);
        System.out.println("启动成功(●'◡'●)");
    }

}
