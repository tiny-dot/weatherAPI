package SSF_practice.weatherAPI_practice.Services;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;



import SSF_practice.weatherAPI_practice.Repositories.WeatherRepository;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import SSF_practice.weatherAPI_practice.Models.*;

@Service
public class WeatherService {
    @Autowired WeatherRepository weatherRepo;

    
    public static final String WEATHER_URL="https://api.openweathermap.org/data/2.5/weather";

    @Value("${weather.api.key}")
    private String apiKey;

    //read API
    public List<Weather> weatherList(Search s){
        //create url request
        String url = UriComponentsBuilder.fromUriString(WEATHER_URL)
                                        .queryParam("q", s.city())
                                        .queryParam("appid", apiKey)
                                        .toUriString();
        
        RequestEntity<Void> req = RequestEntity.get(url)
                                                .accept(MediaType.APPLICATION_JSON)
                                                .build();

        RestTemplate template = new RestTemplate();

        //build response

        ResponseEntity<String> resp;
        List<Weather> weather = new LinkedList<>();

        try{
            resp=template.exchange(req, String.class);
            String payload = resp.getBody();
            //read
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject result = reader.readObject();
            JsonArray jArray = result.getJsonArray("weather");


            //loop - id, main, description, icon
            for(int i=0;i<jArray.size();i++){
                String id  = jArray.getJsonObject(i).getString("id");
                String description = jArray.getJsonObject(i).getString("description");
                String main = jArray.getJsonObject(i).getString("main");
                String icon = jArray.getJsonObject(i).getString("icon");

                //add each one to the article list
                Weather w = new Weather();
                w.setId(id);
                w.setDescription(description);
                w.setIcon(icon);
                w.setMain(main);

                weather.add(w);
            }

        } catch (Exception e){
            e.printStackTrace();
            List.of();

        }
        return weather;
    }

    //caching - if hv data in cache, get the data, if not, save the data 
    
    public List<Weather> getWeatherData(String city, List<Weather> list){
        //get the cached data - i.e. data that alr exists
        List<Weather> weather = weatherRepo.getWeather(city);

        //if data exists, return the list of data
        if(weather!=null){
            return weather;
        }
        else{ //if not, save the data and return it 
            //List<Weather> wList = new LinkedList<>();
            //save data in redis
            //this list is the one from API call
            weatherRepo.saveSearch(city, list);

            return list;
        }
        
    }


    
    
    
}
