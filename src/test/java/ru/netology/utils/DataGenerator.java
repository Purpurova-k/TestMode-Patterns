package ru.netology.utils;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;
import ru.netology.entities.RegistrationInfo;

import java.util.Locale;

import static io.restassured.RestAssured.given;

@UtilityClass
public class DataGenerator {

    Faker faker = new Faker(new Locale("en"));

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    @UtilityClass
    public static class Registration {

        public static RegistrationInfo activeUser() {
            return new RegistrationInfo(
                    faker.name().firstName(),
                    faker.internet().password(),
                    "active");
        }

        public static RegistrationInfo blockedUser() {
            return new RegistrationInfo(
                    faker.name().firstName(),
                    faker.internet().password(),
                    "blocked");
        }


        public static void registerActiveUser() {
            given()
                    .spec(requestSpec)
                    .body(activeUser())
                    .when()
                    .post("/api/system/users")
                    .then()
                    .statusCode(200);
        }


        public static void registerBlockedUser() {
            given()
                    .spec(requestSpec)
                    .body(blockedUser())
                    .when()
                    .post("/api/system/users")
                    .then()
                    .statusCode(200);
        }


        public static String generateWrongLogin() {
            return faker.name().firstName();
        }

        public static String generateWrongPassword() {
            return faker.internet().password();
        }
    }
}
