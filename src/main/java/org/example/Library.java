package org.example;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;
import static org.example.UtilitiesMain.addBook;
import static org.example.UtilitiesMain.getValueFromJsonString;

public class Library {
    public static void main(String[] args) {
        RestAssured.baseURI = "http://216.10.245.166";
        String response = given().header("Content-Type", "application/json")
                .body(addBook("a1b2c3d4", "xcvbnm"))
                .when().post("/Library/Addbook.php")
                .then().assertThat().statusCode(200)
                .extract().response().asString();
        String id = getValueFromJsonString(response, "ID");
        System.out.println(id);

        //delete book
    }

}
