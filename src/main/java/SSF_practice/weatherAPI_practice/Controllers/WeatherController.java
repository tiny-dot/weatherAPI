package SSF_practice.weatherAPI_practice.Controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import SSF_practice.weatherAPI_practice.Models.*;
import SSF_practice.weatherAPI_practice.Services.WeatherService;

@Controller
@RequestMapping
public class WeatherController {
    @Autowired
    private WeatherService weatherSvc;

    @GetMapping("/weather")
    private String getWeather(@RequestParam String city, Model model) {
        model.addAttribute("city", city);

        Search s= new Search(city);
        List<Weather> wList = weatherSvc.weatherList(s);
        //caching
        List<Weather> weatherList = weatherSvc.getWeatherData(city,wList);

        
        model.addAttribute("weatherList", weatherList);

        return "weather";

    }
    
}
