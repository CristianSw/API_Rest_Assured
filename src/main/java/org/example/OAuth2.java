package org.example;

import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.example.pojo.GetCourses;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static io.restassured.RestAssured.*;

public class OAuth2 {
    public static void main(String[] args) throws InterruptedException {

        WebDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=verifyfjdss");
        driver.findElement(By.id("identifierId")).sendKeys("foroneplusone.a00001");
        driver.findElement(By.id("identifierId")).sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys("qazwsxgh32umym");
        driver.findElement(By.cssSelector(".whsOnd")).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.urlContains("rahulshettyacademy.com/getCourse.php"));

        String url = driver.getCurrentUrl();
        driver.close();
        String[] splitCode = url.split("code=");
        String actualCode = splitCode[1].split("&scope=")[0];
        System.out.println(actualCode);



        String getAuthTokenResponse = given()
                .urlEncodingEnabled(false)
                .queryParam("code", actualCode)
                .queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .queryParam("grant_type", "authorization_code")
                .when().post("https://www.googleapis.com/oauth2/v4/token")
                .then().assertThat().statusCode(200).extract().response().asString();

        String accessToken = UtilitiesMain.getValueFromJsonString(getAuthTokenResponse,"access_token");

        GetCourses getBookResponse = given()
                .queryParam("access_token", accessToken)
                .expect().defaultParser(Parser.JSON)
                .when().get("https://rahulshettyacademy.com/getCourse.php")
                .as(GetCourses.class);
//        System.out.println(getBookResponse);
        System.out.println(getBookResponse.getExpertise());
        System.out.println(getBookResponse.getInstructor());
        System.out.println(getBookResponse.getLinkedIn());
    }
}
