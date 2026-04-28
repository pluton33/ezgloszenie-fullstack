package io.github.pluton33.ezgloszenie.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.Date;

@Entity(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Nullable Integer id;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String email;
    private String passwordHash;
//    private Institution
    @Nullable
    private Integer badgeNumber;
    private String firstName;
    private String lastName;
    private Date createdAt;

    @Nullable
    public Integer getId() {
        return id;
    }

    public UserRole getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Nullable
    public Integer getBadgeNumber() {
        return badgeNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}


