package com.synerge.order101.initialsku.model.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "inbound")
public class intialsku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long intialskuId;

}
