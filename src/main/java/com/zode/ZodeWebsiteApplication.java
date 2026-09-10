package com.zode;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages ={"com.zode","com.jfinal","com.db","com.common","com.file"})
public class ZodeWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ZodeWebsiteApplication.class);
        //app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
        System.out.println("启动成功(●'◡'●)");
    }

}
