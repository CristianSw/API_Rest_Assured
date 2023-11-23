import io.restassured.RestAssured;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;


public class LibraryTest {
    @Test(dataProvider = "getData")
    public void addBook(String bookName, String aisle, String isbn) {
        RestAssured.baseURI = "http://216.10.245.166";
        String response = given().header("Content-Type", "application/json")
                .body(Utilities.addBook(bookName,aisle, isbn))
                .when().post("/Library/Addbook.php")
                .then().assertThat().statusCode(200)
                .extract().response().asString();
        String id = Utilities.getValueFromJsonString(response, "ID");
        System.out.println(id);

        //delete book

    }
    @Test
    public void addBookJson() throws IOException {
        RestAssured.baseURI = "http://216.10.245.166";
        String response = given().log().all().header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get("/home/cristian/AquaProjects/API_Rest_Assured/src/test/java/jsons/books.json"))))
                .when().post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
        String msg = Utilities.getValueFromJsonString(response, "Msg");
        String id = Utilities.getValueFromJsonString(response, "ID");
        System.out.println(msg);
        System.out.println(id);

        //delete book

    }

    @DataProvider
    public Object[][] getData() {
        Object[][] objects = new Object[][]{ {"Test1","1","1"},{"Test2","2","2"},{"Test3","3","3"}};
        return objects;
    }
}
