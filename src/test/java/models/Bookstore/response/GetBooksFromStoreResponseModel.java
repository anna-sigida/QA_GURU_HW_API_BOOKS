package models.Bookstore.response;

import lombok.Data;
import models.Bookstore.BookModel;

import java.util.List;

@Data
public class GetBooksFromStoreResponseModel {
    List<BookModel> books;
}