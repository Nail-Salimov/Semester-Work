package server.shop.bl.services;

import server.entities.user.dto.UserDataDto;
import server.entities.user.model.UserDataModel;
import server.shop.entities.dto.OrderDto;
import server.shop.entities.dto.ProductDto;
import server.shop.entities.dto.ReviewDto;

import java.util.List;
import java.util.Optional;

public interface StoreService {
    void saveProduct(ProductDto productDto);

    List<ProductDto> findAllProducts();

    Optional<ProductDto> findProductById(Long id);

    boolean buyProduct(ProductDto productDto, Integer cost, Integer count, UserDataModel userDataModel);
    boolean buyProduct(ProductDto productDto, Integer cost, Integer count, UserDataDto UserDataDto);
    Long productsCount();

    List<OrderDto> findAllOrdersBySellerId(UserDataModel userDataModel);
    List<ProductDto> findProductsByPagination(int page, int size);
    List<ProductDto> findProductsBySimilarName(String name);
    List<OrderDto> findNotAcceptedOrdersByBuyerId(UserDataModel userDataModel);
    Optional<OrderDto> findOrderById(Long id);
    void acceptOrder(OrderDto orderDto, UserDataModel userDataModel);
    void acceptOrder(OrderDto orderDto, UserDataDto userDataDto);

    boolean isOrdered(Long productId, Long userId);
    ReviewDto addReview(ReviewDto reviewDto);

}
