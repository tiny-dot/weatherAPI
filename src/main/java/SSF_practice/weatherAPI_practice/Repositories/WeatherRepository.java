package SSF_practice.weatherAPI_practice.Repositories;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import SSF_practice.weatherAPI_practice.Models.*;

@Repository
public class WeatherRepository {
    @Autowired @Qualifier("redis-0")
    private RedisTemplate<String, Object> template;


    //what is the structure to store the data? hashmap - HashMap<String, Object> 
    //HashMap<City,WeatherObject> 


    //1. check for existing search data
    public Boolean checkKeyExistence(String city){
        return template.hasKey(city);
    }

    //2. set a timer for the payload - info kept in for 5 min only 
    public void setValue(String city, List<Weather> list, Long cache){
        template.opsForValue().set(city, list, cache);
    }


    //3. get the data
    public List<Weather> getValue(String city){
        return (List<Weather>) template.opsForValue().get(city); 
    }

    
    // 

    // //method for caching - makes sure it expires aft 5 min
    
    // //method to save data into redis - list<Weather>
    // //redis-cli: set city List<Weather> + how long the data lasts in redis
    // public void saveSearch(String city, List<Weather> wList){
    //     String timer= city;
    //     template.opsForValue().set(timer,wList, Duration.ofSeconds(cache));

    // }

    // //method to get data from redis - list<Weather>
    // //redis-cli: keys *
    // public List<Weather> getWeather(String city){
    //     return (List<Weather>)template.opsForValue().get(city);
    // }



    
    
}
