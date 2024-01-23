package com.example.lab4.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@ToString
@EqualsAndHashCode
@Entity
@Table(name = "users", schema = "public") //, schema = "s367590"
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long id;

    @Column(name = "username")
    @Getter
    @Setter
    private String username;

    @Column(name = "password")
    @Getter
    @Setter
    private String password;

    @Column(name = "non_expired")
    @Getter
    @Setter
    private boolean nonExpired;

    @Column(name = "non_locked")
    @Getter
    @Setter
    private boolean nonLocked;

    @Column(name = "credentials_non_expired")
    @Getter
    @Setter
    private boolean credentialsNonExpired;

    @Column(name = "enabled")
    @Getter
    @Setter
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<PointEntity> points;
}
