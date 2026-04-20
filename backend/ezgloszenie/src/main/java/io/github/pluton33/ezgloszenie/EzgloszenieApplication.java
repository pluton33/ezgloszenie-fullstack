package io.github.pluton33.ezgloszenie;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class EzgloszenieApplication {

    public static void main(String[] args) {
        SpringApplication.run(io.github.pluton33.ezgloszenie.EzgloszenieApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name", defaultValue = "world") String name) {
        return "Hello" + name;
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }


}
