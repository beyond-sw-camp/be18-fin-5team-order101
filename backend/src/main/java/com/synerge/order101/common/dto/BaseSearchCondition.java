package com.synerge.order101.common.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public abstract class BaseSearchCondition {

    private LocalDate fromDate;
    private LocalDate toDate;
}
