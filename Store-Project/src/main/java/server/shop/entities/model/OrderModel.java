package server.shop.entities.model;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(exclude = "product")
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private Long buyerId;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private Integer cost;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderState orderState;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ProductModel product;
}
