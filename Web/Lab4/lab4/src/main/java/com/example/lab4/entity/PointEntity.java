package com.example.lab4.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
@Entity
@Table(name = "points", schema = "public") //, schema = "s367590"
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long id;

    @Column(name = "r")
    @Getter
    @Setter
    private double r;

    @Column(name = "x")
    @Getter
    @Setter
    private double x;

    @Column(name = "y")
    @Getter
    @Setter
    private double y;

    @Column(name = "result")
    @Getter
    @Setter
    private boolean result;

    @Column(name = "calculated_at")
    @Getter
    @Setter
    private LocalDateTime calculatedAt = LocalDateTime.now();

    @Column(name = "calculation_time")
    @Getter
    @Setter
    private long calculationTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}

