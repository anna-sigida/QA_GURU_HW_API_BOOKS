package models.Bookstore.request;

import lombok.Data;

@Data
public class TokenRequestModel {
    String userName, password;
}
