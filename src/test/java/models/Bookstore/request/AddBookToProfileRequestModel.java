package models.Bookstore.request;

import lombok.Data;
import models.Bookstore.BookModel;

import java.util.List;

@Data
public class AddBookToProfileRequestModel {
    String userId;
    List<BookModel> collectionOfIsbns;
}
