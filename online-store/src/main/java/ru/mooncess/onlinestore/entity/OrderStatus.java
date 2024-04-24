package ru.mooncess.onlinestore.entity;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "status")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
