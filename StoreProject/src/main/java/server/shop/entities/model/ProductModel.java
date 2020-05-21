package server.shop.entities.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer minCost;
    private Integer maxCost;
    private Integer count;
    private Integer decrease;
    private Long time;

    private Double stars;
    private Integer starsCount;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ProductState productState;

    @Column(nullable = false)
    private Long sellerId;

    @OneToMany(mappedBy = "productModel", fetch = FetchType.EAGER)
    private List<ImageProductModel> images;

    @Transient
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private Set<OrderModel> orders;

    @Transient
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private Set<ReviewModel> reviews;
}
