package com.example.lab4.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class UserRequestDTO {
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
}
