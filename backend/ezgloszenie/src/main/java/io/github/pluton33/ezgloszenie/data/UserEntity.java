package io.github.pluton33.ezgloszenie.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;


@Entity(name="users")
public class UserEntity {
    public UserEntity(){}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Nullable Integer id;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(nullable = false, unique = true)
    private String email;
    private String passwordHash;
//    private Institution
    @Nullable
    private Integer badgeNumber;
    private String firstName;
    private String lastName;
    private LocalDate createdAt;

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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBadgeNumber(@Nullable Integer badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", badgeNumber=" + badgeNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}


