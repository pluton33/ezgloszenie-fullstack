package io.github.pluton33.ezgloszenie.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "reports")
public class ReportEntity {
    public ReportEntity() {}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Nullable Long id;
    private String title;
    private String description;
    private LocalDateTime created_date;
    private LocalDateTime accident_date;
    private String location;
    private boolean userAnonymous;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name="report_id",nullable =false)
    private CategoryEntity category;

    @Nullable
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreated_date() {
        return created_date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated_date(LocalDateTime created_date) {
        this.created_date = created_date;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public LocalDateTime getAccident_date() {
        return accident_date;
    }

    public void setAccident_date(LocalDateTime accident_date) {
        this.accident_date = accident_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isUserAnonymous() {
        return userAnonymous;
    }

    public void setUserAnonymous(boolean userAnonymous) {
        this.userAnonymous = userAnonymous;
    }
}
