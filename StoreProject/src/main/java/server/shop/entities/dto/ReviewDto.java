package server.shop.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.entities.user.dto.UserDataDto;
import server.shop.entities.model.ProductModel;
import server.shop.entities.model.ReviewModel;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewDto {
    private Long id;

    private String text;

    private Double stars;

    private ProductDto product;
    private Long userId;
    private UserDataDto userDataDto;

    public static ReviewDto getReviewDto(ReviewModel reviewModel){
        return ReviewDto.builder()
                .id(reviewModel.getId())
                .stars(reviewModel.getStars())
                .text(reviewModel.getText())
                .userId(reviewModel.getUserId())
                .build();
    }
}
