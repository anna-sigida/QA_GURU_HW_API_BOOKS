package models.Bookstore.response;

import lombok.Data;

@Data
public class TokenResponseModel {
    String token, status, result, expires;
}