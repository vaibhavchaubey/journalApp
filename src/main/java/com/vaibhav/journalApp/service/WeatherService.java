package com.vaibhav.journalApp.service;

import com.vaibhav.journalApp.api_response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    private static final String apiKey = "109f78d4896544bbfc4ad0a8b5af064d";

    private static final String API = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

            
    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city){
        String finalAPI = API.replace("API_KEY", apiKey).replace("CITY", city);


        /* The process of converting a JSON object into its corresponding Java object is called deserialization or parsing. Essentially, it involves taking a JSON representation of data and recreating it as a Java object, allowing you to work with the data using Java's object-oriented features. */

         ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);

        WeatherResponse body = response.getBody();

        return body;
    }

    

    

    
}
