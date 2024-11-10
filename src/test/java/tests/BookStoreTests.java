package tests;

import helpers.WithLogin;
import models.Bookstore.BookModel;
import models.Session;
import org.junit.jupiter.api.*;
import controllers.BookstoreController;
import pages.BookstorePage;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static helpers.LoginExtension.clearSession;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Bookstore")
public class BookStoreTests extends TestBase {

    BookstoreController bookstoreController = new BookstoreController();
    BookstorePage bookstorePage = new BookstorePage();

    @AfterEach
    void cleanUp() {
        clearSession();
    }

    @WithLogin
    @DisplayName("Удаление книги из профиля")
    @Test
    void deleteBookFromProfileTest(Session session) {
        List<BookModel> books;
        String isbn;
        List<BookModel> collectionOfIsbns = new ArrayList<>();

        bookstoreController.deleteAllBooksFromProfile(session);
        books = bookstoreController.getBooksFromStore();
        assertNotEquals(null, books);

        isbn = bookstoreController.selectRandomBook(books);
        bookstoreController.addBookToIsbnCollection(isbn, collectionOfIsbns);
        books = bookstoreController.addBookToProfile(collectionOfIsbns, session);
        assertEquals(collectionOfIsbns, books);

        bookstorePage.openProfile();
        bookstorePage.deleteBookFromProfile(isbn);

        $(".ReactTable").$("a[href='/profile?book=" + isbn + "']").shouldNot(exist);
        books = bookstoreController.getBooksFromProfile(session);
        assertTrue(books.isEmpty());
    }
}
