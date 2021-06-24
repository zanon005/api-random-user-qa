package org.tests;

import org.apache.http.HttpStatus;
import org.core.BaseTests;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.matcher.RestAssuredMatchers.*;

public class RandomUsersTests extends BaseTests {

    @Test
    public void testReturningRandomUser(){
        given()
        .when()
                .get("/api")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("results", describedAs("Expected to get one RandomUser", hasSize(equalTo(1))))
                .body("results[0]", describedAs("Expected RandomUser to have a 'id'", hasKey("id")))
                .body("results[0]", describedAs("Expected RandomUser to have a 'login'", hasKey("login")))
                .body("results[0]", describedAs("Expected RandomUser to have a 'name'", hasKey("name")))
        ;
    }

    @Test
    public void testReturningListOfMultipleRandomUsers(){
        int size = 100;
        int user_index = new Random().nextInt(size);
        String user = "results["+user_index+"]";

        given()
                .params("results", size)
        .when()
                .get("/api")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("results", describedAs("Expected to get at least one random user", hasSize(greaterThan(1))))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a key 'id'", hasKey("id")))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a key 'login'", hasKey("login")))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a key 'name'", hasKey("name")))
        ;
    }

    @Test
    public void testReturningRandomFemaleUser(){
        String gender_requested = "female";
        int user_index = 0;
        String user = "results["+user_index+"]";

        given()
                .params("gender", gender_requested)
        .when()
                .get("/api")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("results", describedAs("Expected to get one RandomUser", hasSize(equalTo(1))))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a key 'id'", hasKey("id")))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a key 'name'", hasKey("name")))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a key 'login'", hasKey("login")))
                .body(user+".gender", describedAs("Expected RandomUser["+user_index+"] to have a key 'gender' == 'female' ",
                        equalToIgnoringCase(gender_requested)))
        ;
    }

    @Test
    public void testReturningRandomBrazilianUser(){
        String nat_requested = "br";
        int user_index = 0;
        String user = "results["+user_index+"]";

        given()
                .params("nat", nat_requested)
        .when()
                .get("/api")
        .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.SC_OK)
                .body("results", describedAs("Expected to get one RandomUser", hasSize(equalTo(1))))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a key 'id'", hasKey("id")))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a key 'name'", hasKey("name")))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a key 'login'", hasKey("login")))
                .body(user+".nat", describedAs(
                        "Expected RandomUser to have a 'nat' == 'br'",
                        equalToIgnoringCase("br"))
                )
        ;
    }

    @Test
    public void testReturningRandomBraziliansOrSpanishUser(){
        List<String> nat_requested = new ArrayList<>(Arrays.asList("BR","ES"));
        int user_index = 0;
        String user = "results["+user_index+"]";

        given()
                .params("nat", nat_requested)
        .when()
                .get("/api")
        .then()
                //.log().body()
                .statusCode(HttpStatus.SC_OK)
                .body("results", describedAs("Expected to get one RandomUser", hasSize(equalTo(1))))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a key 'id'", hasKey("id")))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a key 'name'", hasKey("name")))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a key 'login'", hasKey("login")))
                .body(user+".nat", describedAs(
                        "Expected RandomUser to have a 'nat' == 'br'",
                        oneOf("BR", "ES"))
                )
        ;
    }

    @Test
    public void testReturningRandomUserWithOnlyNameEmailPhone(){
        //ArrayList<String> keys_requested = new ArrayList<>(Arrays.asList("name","email","phone"));
        int user_index = 0;
        String user = "results["+user_index+"]";

        given()
                //.queryParam("inc", "name", "email", "phone")
                //.params("inc", keys_requested)
                //.queryParam("inc", keys_requested)
        .when()
                .get("/api/?inc=name,email,phone")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("results", describedAs("Expected to get one RandomUser", hasSize(equalTo(1))))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a 'name'", hasKey("name")))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a 'email'", hasKey("email")))
                .body(user, describedAs("Expected RandomUser["+user_index+"] to have a 'phone'", hasKey("phone")))
                .body("results[0]", describedAs("Expected RandomUser["+user_index+"] to not have a 'login'", not(hasKey("login"))))
                .body("results[0]", describedAs("Expected RandomUser["+user_index+"] to not have a 'id'", not(hasKey("id"))))
        ;
    }
}
