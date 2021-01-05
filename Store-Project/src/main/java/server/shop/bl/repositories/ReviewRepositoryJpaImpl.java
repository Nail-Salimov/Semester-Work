package server.shop.bl.repositories;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server.shop.entities.model.ProductModel;
import server.shop.entities.model.ReviewModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class ReviewRepositoryJpaImpl implements ReviewRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ReviewModel addReview(ReviewModel reviewModel) {
        entityManager.persist(reviewModel);
        return reviewModel;
    }

    @Override
    @Transactional
    public List<ReviewModel> findAllReviewsByProduct(ProductModel productModel) {
        return entityManager.createQuery("SELECT r FROM ReviewModel r WHERE r.product.id=:productId", ReviewModel.class)
                .setParameter("productId", productModel.getId())
                .getResultList();
    }
}
