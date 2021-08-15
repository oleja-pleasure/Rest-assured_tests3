import lombok.DataLombok;
import org.junit.jupiter.api.Test;
import specs.Specs;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.request;
import static specs.Specs.response;


public class ReqresTests {
    @Test
    public void getUsersFirstName() {
        DataLombok janet = given()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                //.body("data.first_name", is("Janet"));
                .extract().as(DataLombok.class);
        assertEquals("Janet",janet.getUserData().getFirst_name());
    }

    @Test
    public void createUser() {
        given()
                .spec(request)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .spec(response);
    }

    @Test
    public void updateUser() {
        String response =
                given()
                        .spec(request)
                        .when()
                        .post("https://reqres.in/api/users")
                        .then()
                        .spec(Specs.response)
                        .extract().path("id");
        given()
                .contentType(JSON)
                .body("{\"name\":\"morpheus\",\"job\":\"zion resident\"}")
                .when()
                .put("https://reqres.in/api/users/" + response)
                .then()
                .statusCode(200)
                .body("name", is("morpheus"), "job", is("zion resident"));
    }

    @Test
    public void deleteUser() {
        String response =
                given()
                        .spec(request)
                        .when()
                        .post("https://reqres.in/api/users")
                        .then()
                        .spec(Specs.response)
                        .extract().path("id");
        given()
                .delete("https://reqres.in/api/users/" + response)
                .then()
                .statusCode(204);
    }

    @Test
    public void unsuccessfulLogin() {
        String response =
                given()
                        .contentType(JSON)
                        .body("{\"email\":\"morpheus@zion.com\"}")
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .statusCode(400)
                        .extract().path("error");
        assertEquals("Missing password", response);
    }

    @Test
    public void userSearchWithGroovy(){
        given()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body("data.findAll{it.email =~/.*?son@reqres.in/}.first_name.flatten()",
                        hasItem("Lindsay"));
    }
}