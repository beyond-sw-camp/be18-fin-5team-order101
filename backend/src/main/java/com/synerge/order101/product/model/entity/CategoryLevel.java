package com.synerge.order101.product.model.entity;

public enum CategoryLevel {
    LARGE("대"),
    MEDIUM("중"),
    SMALL("소");

    private final String label;

    CategoryLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
