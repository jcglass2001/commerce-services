package com.jcglass.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="t_orders")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;
    @OneToMany(cascade= CascadeType.ALL)
    private List<OrderItem> orderItemList;

}
