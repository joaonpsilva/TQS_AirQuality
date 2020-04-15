package com.frstassign.airquality;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AirqualityController {

    @Autowired
    private MyCache cache;

    @Autowired
    private ApiCaller apiCaller;

    @GetMapping("/")
    public String airQualityIndex(Model model) {
        model.addAttribute("region", new Region());
        return "airqualityindex";
    }

    @GetMapping("/setRegion")
    public String setRegion(@ModelAttribute("region") Region region){

        Region rInCache = this.cache.searchInCache(region.getName());
        if (rInCache != null){
            this.cache.sendToEnd(region.getName());
            region.setAirQual(rInCache.getAirQual());
        }
        else{
            Map<String, Double> airvalues = apiCaller.getRegionInfo(region);
            region.setAirQual(airvalues);

            if (airvalues.get("aqi") != -1.0)
                this.cache.addToCache(region);
        }

        return "airqualityindex";
    }

}