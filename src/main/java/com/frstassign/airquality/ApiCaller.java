package com.frstassign.airquality;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class ApiCaller {
    
    private String apikey = "10eb0c4ae034aecf47c33eafee747ae473e0ec2a";

    public JSONObject callApi(String name){
        String url = "https://api.waqi.info/feed/" + name + "/?token=" + apikey;

		JSONObject json;
        try {
            json = readJsonFromUrl(url);
        } catch (Exception e) {
            return null;
        }

        return json;
    }

    public Map<String, Double> getRegionInfo(Region region){
        
        Map<String, Double> airvalues = new HashMap<>();

        JSONObject json = callApi(region.getName());
        if (json == null || json.get("status").equals("error")){
            airvalues.put("aqi", -1.0);
            return airvalues;
        }
        
        JSONObject data = (JSONObject) json.get("data");
        JSONObject iaqi = (JSONObject) data.get("iaqi");

        airvalues.put("aqi", Double.parseDouble(data.get("aqi").toString()));

        Iterator<String> keys = iaqi.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            if (iaqi.get(key) instanceof JSONObject) {
                JSONObject temp = (JSONObject) iaqi.get(key);
                airvalues.put(key, Double.parseDouble(temp.get("v").toString()));
            }
        }

        return airvalues;
    }

    
	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
		  sb.append((char) cp);
		}
		return sb.toString();
	  }
	
    public JSONObject readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } finally {
            is.close();
        }
    }

}

