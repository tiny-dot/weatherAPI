package SSF_practice.weatherAPI_practice.Repositories;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import SSF_practice.weatherAPI_practice.Models.*;

@Repository
public class WeatherRepository {
    @Autowired @Qualifier("redis-0")
    private RedisTemplate<String, Object> template;

    
    //set timer for cache
    private static final Long cache = 300l; //5 min

    //method for caching - makes sure it expires aft 5 min
    
    //method to save data into redis - list<Weather>
    //redis-cli: set city List<Weather> + how long the data lasts in redis
    public void saveSearch(String city, List<Weather> wList){
        String timer= city;
        template.opsForValue().set(timer,wList, Duration.ofSeconds(cache));

    }

    //method to get data from redis - list<Weather>
    //redis-cli: keys *
    public List<Weather> getWeather(String city){
        return (List<Weather>)template.opsForValue().get(city);
    }



    
    
}
