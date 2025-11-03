package com.synerge.order101.inbound.model.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "inbound_detail")
public class InBoundDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inboundDetailId;

}
