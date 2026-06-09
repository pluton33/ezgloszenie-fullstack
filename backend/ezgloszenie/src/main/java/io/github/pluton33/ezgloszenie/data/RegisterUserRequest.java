package io.github.pluton33.ezgloszenie.data;

import java.util.Date;

public record RegisterUserRequest (
        String email,
        String passwordHash,
        String firstName,
        String lastName
){
}
