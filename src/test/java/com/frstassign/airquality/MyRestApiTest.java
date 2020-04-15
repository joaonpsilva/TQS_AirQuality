package com.frstassign.airquality;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyRestApiTest {

    @LocalServerPort
    private int port;
    
    @Test
    public void getNewRegionData() {

        given().port(port).get("/cityAirQual/porto").then().statusCode(200).assertThat()
        .body("data.aqi", allOf(greaterThanOrEqualTo((float)0.0), lessThanOrEqualTo((float)100.0)))
        .body("status", equalTo("OK"));
    } 

    @Test
    public void getBadRegionData() {

        given().port(port).get("/cityAirQual/portoasdasdaf").then().statusCode(404);
    } 

    @Test
    public void getBadCacheStats() {

        given().port(port).get("/cacheInfo/sadsadb").then().statusCode(404);
    } 

    @Test
    public void cacheStats() {
        given().port(port).get("/cityAirQual/porto");
        given().port(port).get("/cityAirQual/lisbon");
        given().port(port).get("/cityAirQual/porto");
        given().port(port).get("/cityAirQual/madrid");
        given().port(port).get("/cityAirQual/barcelona");
        given().port(port).get("/cityAirQual/lisbon");


        given().port(port).get("/cacheInfo/requests").then().statusCode(200).assertThat()
        .body("data.requests", equalTo(6))
        .body("status", equalTo("OK"));

        given().port(port).get("/cacheInfo/hits").then().statusCode(200).assertThat()
        .body("data.hits", equalTo(1))
        .body("status", equalTo("OK"));

        given().port(port).get("/cacheInfo/misses").then().statusCode(200).assertThat()
        .body("data.misses", equalTo(5))
        .body("status", equalTo("OK"));

        given().port(port).get("/cacheInfo").then().statusCode(200).assertThat()
        .body("data.requests", equalTo(6))
        .body("data.hits", equalTo(1))
        .body("data.misses", equalTo(5))
        .body("status", equalTo("OK"));

    } 
}