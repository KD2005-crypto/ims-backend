package com.codeb.ims.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "team_members") // <--- Renamed to avoid SQL errors
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String avatarPath;
}