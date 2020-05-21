package server.shop.bl.repositories;

import server.bl.repositories.CrudRepository;
import server.shop.entities.model.OrderModel;

import java.util.List;

public interface OrderRepositoryJpa extends CrudRepository<Long, OrderModel> {
    List<OrderModel> findAllBySellerId(Long id);
    List<OrderModel> findNotAcceptedOrderByBuyerId(Long id);
    void update(OrderModel orderModel);
    boolean isOrdered(Long productId, Long userId);
}
