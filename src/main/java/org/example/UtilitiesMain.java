package org.example;

import io.restassured.path.json.JsonPath;

public class UtilitiesMain {
    public static JsonPath stringToJson(String response) {
        return new JsonPath(response);
    }

    public static String getValueFromJsonString(String response, String key) {
        JsonPath jsonPath = new JsonPath(response);
        return jsonPath.getString(key);
    }
    public static int getValueFromJsonStringInt(String response, String key) {
        JsonPath jsonPath = new JsonPath(response);
        return jsonPath.getInt(key);
    }
    public static String getValueFromJsonStringInt(String response, String key, boolean intToString) {
        JsonPath jsonPath = new JsonPath(response);
        if (intToString) {
            return jsonPath.get(key).toString();
        }else
            return jsonPath.get(key);
    }

    public static String getValueFromJsonPath(JsonPath jsonPath, String key) {
        return jsonPath.getString(key);
    }

    public static String coursesJson() {
        return "{\r\n" +
                "  \"dashboard\": {\r\n" +
                "    \"purchaseAmount\": 1162,\r\n" +
                "    \"website\": \"rahulshettyacademy.com\"\r\n" +
                "  },\r\n" +
                "  \"courses\": [\r\n" +
                "    {\r\n" +
                "      \"title\": \"Selenium Python\",\r\n" +
                "      \"price\": 50,\r\n" +
                "      \"copies\": 6\r\n" +
                "    },\r\n" +
                "    {\r\n" +
                "      \"title\": \"Cypress\",\r\n" +
                "      \"price\": 40,\r\n" +
                "      \"copies\": 4\r\n" +
                "    },\r\n" +
                "    {\r\n" +
                "      \"title\": \"RPA\",\r\n" +
                "      \"price\": 45,\r\n" +
                "      \"copies\": 10\r\n" +
                "    },\r\n" +
                "     {\r\n" +
                "      \"title\": \"Appium\",\r\n" +
                "      \"price\": 36,\r\n" +
                "      \"copies\": 7\r\n" +
                "    }\r\n" +
                "    \r\n" +
                "    \r\n" +
                "    \r\n" +
                "  ]\r\n" +
                "}\r\n";
    }

    public static String addBook(String aisle, String isbn) {
        return "{\n" +
                "    \"name\": \"Some book\",\n" +
                "    \"isbn\": \"" + isbn + "\",\n" +
                "    \"aisle\": \"" + aisle + "\",\n" +
                "    \"author\": \"CD\"\n" +
                "}";
    }
}
