package helpers;

import models.Session;
import models.Bookstore.response.LoginResponseModel;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import controllers.BookstoreAuthController;

public class LoginExtension implements BeforeEachCallback {

    private static final ThreadLocal<Session> session = new ThreadLocal<>();

    public static Session getSession() {
        return session.get();
    }

    public static void clearSession() {
        session.remove();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Session newSession = new Session();
        LoginResponseModel authResponse = BookstoreAuthController.getAuthorization();
        newSession.setUserId(authResponse.getUserId());
        newSession.setToken(authResponse.getToken());
        newSession.setExpires(authResponse.getExpires());
        session.set(newSession);

        BookstoreAuthController.buildAuthorizationCookie(session.get());
    }
}