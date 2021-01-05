package server.shop.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.shop.entities.model.ProductModel;
import server.shop.entities.model.ProductState;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private Integer minCost;
    private Integer maxCost;
    private Integer decrease;
    private Integer count;
    private ProductState productState;

    private Long sellerId;
    private Long time;

    private Double stars;
    private Integer starsCount;
    private Set<ReviewDto> reviews;

    private List<ImageProductDto> images;

    public static ProductDto getProductDto(ProductModel productModel){
        return ProductDto.builder()
                .id(productModel.getId())
                .name(productModel.getName())
                .description(productModel.getDescription())
                .minCost(productModel.getMinCost())
                .maxCost(productModel.getMaxCost())
                .decrease(productModel.getDecrease())
                .count(productModel.getCount())
                .sellerId(productModel.getSellerId())
                .time(productModel.getTime())
                .productState(productModel.getProductState())
                .stars(productModel.getStars())
                .starsCount(productModel.getStarsCount())

                .build();
    }

}
