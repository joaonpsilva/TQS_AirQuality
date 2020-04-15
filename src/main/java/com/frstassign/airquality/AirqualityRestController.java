package com.frstassign.airquality;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AirqualityRestController {

    @Autowired
    private MyCache cache;

    @Autowired
    private ApiCaller apiCaller;

    @GetMapping("/cityAirQual/{name}")
    public ObjectNode getAqi(@PathVariable("name") String name) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode returnJson = mapper.createObjectNode();

        Region rInCache = this.cache.searchInCache(name);
        //Check cache
        if (rInCache != null){
            this.cache.sendToEnd(name);
            returnJson.put("status", "OK");
            ObjectNode data = mapper.convertValue(rInCache.getAirQual(), ObjectNode.class);
            returnJson.set("data", data);
            return returnJson;
        }

        Region r = new Region(name);
        Map<String, Double> result = apiCaller.getRegionInfo(r);
        //Bad API Call
        if ( result.get("aqi") == -1.0 ){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "city not found");
        }
        //Good api call
        r.setAirQual(result);
        cache.addToCache(r);
        returnJson.put("status", "OK");
        ObjectNode data = mapper.convertValue(r.getAirQual(), ObjectNode.class);
        returnJson.set("data", data);
        return returnJson;
    }

    @GetMapping("cacheInfo/{info}")
    public ObjectNode getCacheInfo(@PathVariable("info") String info){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode returnJson = mapper.createObjectNode();
        HashMap<String, Integer> cachedata = new HashMap<>();
        returnJson.put("status", "OK");

        if (info.equals("requests")){
            cachedata.put("requests", this.cache.getNrequests());
        }
        else if (info.equals("hits")){
            cachedata.put("hits", this.cache.getHits());
        }
        else if (info.equals("misses")){
            cachedata.put("misses", this.cache.getMisses());
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "info not existent");
        }
        ObjectNode data = mapper.convertValue(cachedata, ObjectNode.class);

        returnJson.set("data", data);
        return returnJson;
    }

    @GetMapping("cacheInfo")
    public ObjectNode getCacheInfo(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode returnJson = mapper.createObjectNode();
        returnJson.put("status", "OK");

        HashMap<String, Integer> cachedata = new HashMap<>();
        cachedata.put("requests", this.cache.getNrequests());
        cachedata.put("hits", this.cache.getHits());
        cachedata.put("misses", this.cache.getMisses());
        ObjectNode data = mapper.convertValue(cachedata, ObjectNode.class);

        returnJson.set("data", data);
        return returnJson;

    }

}