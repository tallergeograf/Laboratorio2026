package uy.edu.taller.sige.geo_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:geocoders.properties")
public class AppConfig {

}
