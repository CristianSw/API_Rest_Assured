package org.example;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.*;

public class JiraAPITests {
    public static void main(String[] args) {
        RestAssured.baseURI = "http://localhost:8080";
        //login session
        SessionFilter session = new SessionFilter();
        given().log().all().header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"cristian.dolinta\",\n" +
                        "    \"password\": \"qazwsxgh32umym\"\n" +
                        "}")
                .filter(session)
                .when().post("/rest/auth/1/session")
                .then().log().all().assertThat().statusCode(200);

//add comment
        String wantedComment = "Hi its a test comment form Rest API.";
        String commentResponse = given().pathParam("issueKey", "RSA-4").log().all()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"body\": \"" + wantedComment + "\",\n" +
                        "    \"visibility\": {\n" +
                        "        \"type\": \"role\",\n" +
                        "        \"value\": \"Administrators\"\n" +
                        "    }\n" +
                        "}")
                .filter(session)
                .when().post("/rest/api/2/issue/{issueKey}/comment")
                .then().log().all().assertThat().statusCode(201).extract().response().asString();

        //add attachment

        given().pathParam("issueKey", "RSA-4")
                .header("X-Atlassian-Token", "no-check")
                .header("Content-Type", "multipart/form-data")
                .filter(session)
                .multiPart("file", new File("jira.txt"))
                .when().post("/rest/api/2/issue/{issueKey}/attachments")
                .then().log().all().assertThat().statusCode(200);

        //get issue
        String issueResponse = given().filter(session)
                .pathParam("issueKey", "RSA-4")
                .queryParam("fields", "comment")
                .when().get("/rest/api/2/issue/{issueKey}")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        String commentId = UtilitiesMain.getValueFromJsonString(commentResponse, "id");
        int commentsSize = UtilitiesMain.getValueFromJsonStringInt(issueResponse, "fields.comment.comments.size()");
        String returnedCommentId = "";
        String returnedCommentBody = "";
        for (int i = 0; i < commentsSize; i++) {
            returnedCommentId = UtilitiesMain.getValueFromJsonStringInt(issueResponse, "fields.comment.comments[" + i + "].id", true);

            if (returnedCommentId.equalsIgnoreCase(commentId)) {
                returnedCommentBody = UtilitiesMain.getValueFromJsonString(issueResponse, "fields.comment.comments[" + i + "].body");
                break;
            }
        }
        Assert.assertEquals(returnedCommentBody, wantedComment);

    }
}

