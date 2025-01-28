package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GreetingResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .header("Origin", "*")
          .when().get("/hello")
          .then()
             .statusCode(200)
             .header("Access-Control-Allow-Origin", "*")
             .body(is("Hello from Quarkus REST"));
    }

    @Test
    void testHelloMessageEndpoint() {
        given()
          .body("{\"name\":\"Johnaa\"}")
          .header("Content-Type", "application/json")
          .when().post("/hellomessage")
          .then()
             .statusCode(200)
             .header("Access-Control-Allow-Origin", "*")
             .body("message", is("Hello Johnaa"));
    }
}