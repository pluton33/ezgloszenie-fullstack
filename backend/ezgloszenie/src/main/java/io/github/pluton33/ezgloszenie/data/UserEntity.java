package io.github.pluton33.ezgloszenie.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Nullable Integer id;
    @Enumerated
    private UserRole role;
    private String email;
    private String passwordHash;
//    private Institution
    @Nullable
    private Integer badgeNumber;
    private String firstName;
    private String lastName;
    private Date createdAt;


}


