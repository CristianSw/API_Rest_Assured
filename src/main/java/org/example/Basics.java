package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class Basics {
    public static void main(String[] args) {
        //validate add place API is working as expected
        //given - all input details
        //when - Submit the API
        //then - validate response
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"location\": {\n" +
                        "        \"lat\": -38.383494,\n" +
                        "        \"lng\": 33.427362\n" +
                        "    },\n" +
                        "    \"accuracy\": 50,\n" +
                        "    \"name\": \"Cristian\",\n" +
                        "    \"phone_number\": \"(+373) 79356922\",\n" +
                        "    \"address\": \"Chisinau\",\n" +
                        "    \"types\": [\n" +
                        "        \"shoe park\",\n" +
                        "        \"shop\"\n" +
                        "    ],\n" +
                        "    \"website\": \"https://rahulshettyacademy.com\",\n" +
                        "    \"language\": \"French-IN\"\n" +
                        "}")
                .when().post("maps/api/place/add/json")
                .then().statusCode(200).body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
        JsonPath jsonPath = new JsonPath(response);
        String placeId = jsonPath.getString("place_id");
        String address = jsonPath.getString("address");
        //update place
        String updatedAddress = "Chisinau, Hristo Botev, ap.27";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"place_id\": \"" + placeId + "\",\n" +
                        "    \"address\": \"" + updatedAddress + "\",\n" +
                        "    \"key\": \"qaclick123\"\n" +
                        "}")
                .when().put("maps/api/place/update/json")
                .then().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
        //get place
        String responseUpdatedAddress = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
                .when().get("maps/api/place/get/json")
                .then().assertThat().statusCode(200).extract().response().asString();
        jsonPath = new JsonPath(responseUpdatedAddress);
        String updatedAddressResponse = jsonPath.getString("address");
        Assert.assertNotEquals(updatedAddressResponse,address);
        Assert.assertEquals(updatedAddressResponse,updatedAddress);

    }
}
