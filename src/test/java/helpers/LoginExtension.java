package helpers;

import models.Session;
import models.Bookstore.response.LoginResponseModel;
import org.junit.jupiter.api.extension.*;
import controllers.BookstoreAuthController;

public class LoginExtension implements BeforeEachCallback, ParameterResolver {

    private static final ThreadLocal<Session> session = new ThreadLocal<>();

    public static Session getSession() {
        return session.get();
    }

    public static void clearSession() {
        session.remove();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Session newSession = new Session();
        LoginResponseModel authResponse = BookstoreAuthController.getAuthorization();
        newSession.setUserId(authResponse.getUserId());
        newSession.setToken(authResponse.getToken());
        newSession.setExpires(authResponse.getExpires());
        session.set(newSession);

        BookstoreAuthController.buildAuthorizationCookie(session.get());
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType().equals(Session.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return getSession();
    }
}
