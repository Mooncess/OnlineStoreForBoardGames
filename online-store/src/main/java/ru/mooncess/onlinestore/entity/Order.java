package ru.mooncess.onlinestore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String orderNumber;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String orderDate;
    @Column(nullable = false)
    private Double total;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private OrderStatus status;

    @ManyToMany
    private List<OrderItem> orderItemList;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;
}
