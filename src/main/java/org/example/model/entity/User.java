package org.example.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "tg_user_id")
    private String tgUserId;

    @Setter
    @CreationTimestamp
    @Column(name = "first_entry_date")
    private LocalDateTime firstEntryDate;

    @Id
    @GeneratedValue
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTgUserId(String tgUserId) {
        this.tgUserId = tgUserId;
    }

    public void setFirstEntryDate(LocalDateTime firstEntryDate) {
        this.firstEntryDate = firstEntryDate;
    }

    public String getTgUserId() {
        return tgUserId;
    }

    public LocalDateTime getFirstEntryDate() {
        return firstEntryDate;
    }
}