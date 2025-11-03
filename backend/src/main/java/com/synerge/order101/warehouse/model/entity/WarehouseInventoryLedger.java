package com.synerge.order101.warehouse.model.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "warehouse_inventory_ledger")
public class WarehouseInventoryLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryLedgerId;
}
