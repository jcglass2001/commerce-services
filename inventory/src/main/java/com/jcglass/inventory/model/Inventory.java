package com.jcglass.inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_inventory")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    @SequenceGenerator(name = "inventory_id_sequence", sequenceName = "inventory_id_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "inventory_id_sequence")
    private Long id;
    private String productId;
    private String skuCode;
    private Integer quantity;
}
