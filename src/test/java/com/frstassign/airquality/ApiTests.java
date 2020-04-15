package com.frstassign.airquality;

import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ApiTests {

    @Spy
    private ApiCaller apiCallerMock;

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void returnsCorrectDict() {

        String stringToParse = "{\"status\":\"ok\",\"data\":{\"aqi\":26,\"idx\":8373,\"attributions\":[{\"url\":\"http://qualar.apambiente.pt/\",\"name\":\"Portugal -Agencia Portuguesa do Ambiente - Qualidade do Ar\",\"logo\":\"portugal-qualar.png\"},{\"url\":\"http://www.eea.europa.eu/themes/air/\",\"name\":\"European Environment Agency\",\"logo\":\"Europe-EEA.png\"},{\"url\":\"https://waqi.info/\",\"name\":\"World Air Quality Index Project\"}],\"city\":{\"geo\":[41.1475,-8.6588888888889],\"name\":\"Sobreiras-Lordelo do Ouro, Porto, Portugal\",\"url\":\"https://aqicn.org/city/portugal/porto/sobreiras-lordelo-do-ouro\"},\"dominentpol\":\"o3\",\"iaqi\":{\"h\":{\"v\":72},\"o3\":{\"v\":25.6},\"p\":{\"v\":1021.8},\"t\":{\"v\":19.4},\"w\":{\"v\":1.3},\"wg\":{\"v\":5}},\"time\":{\"s\":\"2020-04-08 11:00:00\",\"tz\":\"+01:00\",\"v\":1586343600},\"debug\":{\"sync\":\"2020-04-08T23:20:52+09:00\"}}}";
        JSONObject json = null;

        try {
            json = new JSONObject(stringToParse);
       }catch (JSONException err){
            assertTrue(false);
       }

        when(apiCallerMock.callApi("porto")).thenReturn(json);

        Region porto = new Region("porto");
        Map<String, Double> answer = apiCallerMock.getRegionInfo(porto);

        HashMap<String, Double> correct = new HashMap<>();
        correct.put("aqi",26.0);
        correct.put("p",1021.8);
        correct.put("wg",5.0);
        correct.put("o3",25.6);
        correct.put("t",19.4);
        correct.put("w",1.3);
        correct.put("h",72.0);

        assertEquals(correct, answer);
    }

    @Test
    void badRegion(){

        String stringToParse = "{\"status\":\"error\",\"data\":\"Unknown station\"}";
        JSONObject json = null;

        try {
            json = new JSONObject(stringToParse);
        }catch (JSONException err){
            assertTrue(false);
        }
        when(apiCallerMock.callApi("portoooo")).thenReturn(json);

        Region porto = new Region("portoooo");
        Map<String, Double> answer = apiCallerMock.getRegionInfo(porto);
        HashMap<String, Double> correct = new HashMap<>();
        correct.put("aqi", -1.0);
        assertEquals(correct, answer);
    }

    @Test
    void nullJson(){

        when(apiCallerMock.callApi("BadJson")).thenReturn(null);

        Region porto = new Region("BadJson");
        Map<String, Double> answer = apiCallerMock.getRegionInfo(porto);
        HashMap<String, Double> correct = new HashMap<>();
        correct.put("aqi", -1.0);
        assertEquals(correct, answer);
    }

    @Test
    void readFromUrlthrowsException() throws IOException {

        String url = "https://api.waqi.info/feed/" + "badURL" + "/?token=" + "10eb0c4ae034aecf47c33eafee747ae473e0ec2a";
        when(apiCallerMock.readJsonFromUrl(url)).thenThrow(new IOException());

        assertNull(apiCallerMock.callApi("badURL"));

    }

}