package com.synerge.order101.product.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_category")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productCategoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_level", nullable = false)
    private CategoryLevel categoryLevel;

    @Column(name = "category_name",  nullable = false, length = 80)
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private ProductCategory parent;

    @OneToMany(mappedBy = "parent")
    private List<ProductCategory> children = new ArrayList<>();

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdAt;

}
