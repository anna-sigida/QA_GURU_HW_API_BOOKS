package controllers;

import data.Credentials;
import io.qameta.allure.Step;
import models.Bookstore.request.LoginRequestModel;
import models.Bookstore.request.TokenRequestModel;
import models.Bookstore.response.LoginResponseModel;
import models.Bookstore.response.TokenResponseModel;
import models.Session;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.BookStoreSpec.bookStoreRequestSpec;
import static specs.BookStoreSpec.bookStoreResponseSpec200;

public class BookstoreAuthController {
    static Credentials user = new Credentials();

    @Step("Получить токен")
    public static void getToken() {
        TokenRequestModel bodyData = new TokenRequestModel();
        bodyData.setUserName(user.getLogin());
        bodyData.setPassword(user.getPassword());

        TokenResponseModel response = step("Отправить запрос", () ->
                given(bookStoreRequestSpec)
                        .body(bodyData)
                        .when()
                        .post("/Account/v1/GenerateToken")
                        .then()
                        .spec(bookStoreResponseSpec200)
                        .extract().as(TokenResponseModel.class));

        step("Проверить ответ", () -> {
            assertEquals("Success", response.getStatus());
            assertEquals("User authorized successfully.", response.getResult());
        });
    }

    @Step("Авторизация")
    public static LoginResponseModel getAuthorization() {

        getToken();

        LoginRequestModel bodyData = new LoginRequestModel();
        bodyData.setUserName(user.getLogin());
        bodyData.setPassword(user.getPassword());

        return given(bookStoreRequestSpec)
                .body(bodyData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .spec(bookStoreResponseSpec200)
                .extract().as(LoginResponseModel.class);
    }

    @Step("Сгенерировать куки для браузера")
    public static void buildAuthorizationCookie(Session session) {
        open("/favicon.ico");

        getWebDriver().manage().addCookie(new Cookie("userID", session.getUserId()));
        getWebDriver().manage().addCookie(new Cookie("token", session.getToken()));
        getWebDriver().manage().addCookie(new Cookie("expires", session.getExpires()));
    }
}
