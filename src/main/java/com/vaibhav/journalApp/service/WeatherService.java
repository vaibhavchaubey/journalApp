package com.vaibhav.journalApp.service;

import com.vaibhav.journalApp.api_response.WeatherResponse;
import com.vaibhav.journalApp.cache.AppCache;
import com.vaibhav.journalApp.constants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;
            
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city){
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);

        if(weatherResponse != null){
            return weatherResponse;
        }
        else{
            String finalAPI = appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.API_KEY, apiKey).replace(Placeholders.CITY, city);


            /* The process of converting a JSON object into its corresponding Java object is called deserialization or parsing. Essentially, it involves taking a JSON representation of data and recreating it as a Java object, allowing you to work with the data using Java's object-oriented features. */

            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);

            WeatherResponse body = response.getBody();
            if(body != null){
                redisService.set("weather_of_" + city, body, 300l);
            }
            else{
                return null;
            }
            return body;
        }




    }

    

    

    
}
