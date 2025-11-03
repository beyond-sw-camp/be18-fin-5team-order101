package com.synerge.order101.inbound.model.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "inbound")
public class InBound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inboundId;

}
