package org.example;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.util.ArrayList;

import static org.example.Utilities.coursesJson;
import static org.example.Utilities.stringToJson;

public class NestedJsonParse {
    public static void main(String[] args) {
        JsonPath jsonPath = stringToJson(coursesJson());
        int coursesSize = jsonPath.getInt("courses.size()");
        Assert.assertEquals(coursesSize, 4);
        int purchaseAmount = jsonPath.getInt("dashboard.purchaseAmount");
        Assert.assertEquals(purchaseAmount, 1162);
        String firstCourseTitle = jsonPath.get("courses[0].title");
        Assert.assertEquals(firstCourseTitle, "Selenium Python");
        ArrayList<String> coursesTitles = new ArrayList<>();
        ArrayList<Integer> coursesPrice = new ArrayList<>();
        ArrayList<Integer> coursesCopies = new ArrayList<>();
        for (int i = 0; i < coursesSize; i++) {
            coursesTitles.add(jsonPath.get("courses[" + i + "].title"));
            coursesPrice.add(jsonPath.getInt("courses[" + i +"].price"));
            coursesCopies.add(jsonPath.getInt("courses["+ i+"].copies"));
            if (coursesTitles.get(i).equalsIgnoreCase("rpa")){
                System.out.println(coursesCopies.get(i));
            }
        }
//        coursesTitles.forEach(System.out::println);
//        coursesPrice.forEach(System.out::println);
//        coursesCopies.forEach(System.out::println);
        int coursesSum = 0;
        for (int i = 0; i < coursesSize; i++) {
            coursesSum += (coursesPrice.get(i) * coursesCopies.get(i));
        }
        Assert.assertEquals(coursesSum,purchaseAmount);



    }


}
