package server.shop.bl.repositories;

import server.bl.repositories.CrudRepository;
import server.shop.entities.model.ImageProductModel;
import server.shop.entities.model.ProductModel;

import java.util.List;
import java.util.Optional;

public interface StoreRepositoryJpa extends CrudRepository<Long, ProductModel> {
    List<ImageProductModel> findImageByProductId(Long id);
    Optional<ProductModel> findById(Long id);

    boolean buyProduct(ProductModel model, Integer count);
    List<ProductModel> findBySimilarName(String name);
    List<ProductModel> findByPagination(int page, int size);
    Long productsCount();

    ProductModel updateProduct(ProductModel productModel);
}
