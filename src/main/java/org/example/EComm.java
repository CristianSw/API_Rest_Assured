package org.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.pojo.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class EComm {
    public static void main(String[] args) {

        // Login
        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).build();
        EcomLogin ecomLogin = new EcomLogin();
        ecomLogin.setUserEmail("cdl@gmail.com");
        ecomLogin.setUserPassword("Qazwsx_gh32umym");
        RequestSpecification loginRequest = given().spec(spec).body(ecomLogin);

        LoginResponse response = loginRequest.when()
                .post("/api/ecom/auth/login")
                .then().extract().response().as(LoginResponse.class);

        System.out.println(response.getToken());
        System.out.println(response.getUserId());
        System.out.println(response.getMessage());

        // Add product
        RequestSpecification addProductBaseURLRequest = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", response.getToken())
                .build();

        File productPhoto = new File("/home/cristian/Pictures/Screenshots/photo1.jpg");
        if (!productPhoto.exists()) {
            System.out.println("File not found!");
        }
        RequestSpecification addProductSpecificationRequest = given().spec(addProductBaseURLRequest)
                .param("productName", "cdl-product")
                .param("productAddedBy", response.getUserId())
                .param("productCategory", "fashion")
                .param("productSubCategory", "shirts")
                .param("productPrice", "123")
                .param("productDescription", "description")
                .param("productFor", "men")
                .multiPart("productImage", productPhoto);
        AddProductResponse addProductResponse = addProductSpecificationRequest.when().post("/api/ecom/product/add-product")
                .then().log().all().extract().response().as(AddProductResponse.class);

        System.out.println(addProductResponse.getProductId());
        System.out.println(addProductResponse.getMessage());


//        Create Order
        RequestSpecification createOrderBaseURLRequest = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", response.getToken())
                .setContentType(ContentType.JSON)
                .build();

        Order order = new Order();
        order.setProductOrderedId(addProductResponse.getProductId());
        order.setCountry("Germany");

        List<Order> orders = new ArrayList<>();
        orders.add(order);

        CreateOrder createOrder = new CreateOrder();
        createOrder.setOrders(orders);
        RequestSpecification createOrderSpecificationRequest = given().spec(createOrderBaseURLRequest).body(createOrder);

        CreatedOrderResponse createOrderResponse = createOrderSpecificationRequest.when()
                .post("/api/ecom/order/create-order")
                .then().log().all().extract().response()
                .as(CreatedOrderResponse.class);

        System.out.println(createOrderResponse.getMessage());
        System.out.println(createOrderResponse.getOrders());
        System.out.println(createOrderResponse.getProductOrderId());

//        Get Order Details
        RequestSpecification getOrderDetailsBaseURLRequest = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", response.getToken())
                .build();
        RequestSpecification getOrderDetailsSpecificationRequest = given().spec(getOrderDetailsBaseURLRequest);
        String getOrderDetailsStr = getOrderDetailsSpecificationRequest.when()
                .get("/api/ecom/order/get-orders-details" + "?id=" + createOrderResponse.getOrders().get(0))
                .then().log().all().extract().response().asString();
        System.out.println(getOrderDetailsStr);

        //Delete test data
        RequestSpecification deleteTestDataBaseURLRequest = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization", response.getToken())
                .build();

        RequestSpecification deleteTestDataSpecificationRequest = given().spec(deleteTestDataBaseURLRequest);
        DeleteTestDataResponse deleteMessageResponse = deleteTestDataSpecificationRequest.when()
                .delete("/api/ecom/product/delete-product/" + addProductResponse.getProductId())
                .then().log().all().extract().response().as(DeleteTestDataResponse.class);
        System.out.println(deleteMessageResponse.getMessage());
    }
}
