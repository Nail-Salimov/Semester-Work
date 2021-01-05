package server.shop.bl.repositories;


import server.shop.entities.model.ProductModel;
import server.shop.entities.model.ReviewModel;

import java.util.List;

public interface ReviewRepository {

    ReviewModel addReview(ReviewModel reviewModel);
    List<ReviewModel> findAllReviewsByProduct(ProductModel productModel);
}
