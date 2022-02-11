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

    public static String generateRandomLogin() {
        return faker.name().firstName();
    }

    public static String generateRandomPassword() {
        return faker.internet().password();
    }

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    @UtilityClass
    public static class Registration {

        public static RegistrationInfo generateUser(String status) {
            return new RegistrationInfo(
                    generateRandomLogin(),
                    generateRandomPassword(),
                    status);
        }


        public static void registerUser(RegistrationInfo user) {
            given()
                    .spec(requestSpec)
                    .body(user)
                    .when()
                    .post("/api/system/users")
                    .then()
                    .statusCode(200);
        }

        public static RegistrationInfo generateRegisteredUser(String status) {
            RegistrationInfo user = generateUser(status);
            registerUser(user);
            return user;
        }
    }
}
