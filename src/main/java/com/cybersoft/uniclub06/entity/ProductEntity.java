package com.cybersoft.uniclub06.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "information")
    private String information;
    @Column(name = "price")
    private double price;
    @Column(name = "create_date")
    private LocalDateTime creatDate;

    @ManyToOne
    @JoinColumn(name = "id_brand")
    private BrandEntity brand;
    @OneToMany(mappedBy = "product")
    private List<VariantEntity> variants;

    @OneToMany(mappedBy = "product")
    private List<ProductCategoryEntity> productCategories;
}
