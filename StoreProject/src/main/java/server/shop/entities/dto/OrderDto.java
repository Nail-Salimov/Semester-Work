package server.shop.entities.dto;

import lombok.*;
import server.entities.user.dto.UserDataDto;
import server.shop.entities.model.OrderState;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "product")

public class OrderDto {

    private Long id;

    private Long sellerId;

    private Long buyerId;

    private ProductDto product;
    private OrderState orderState;

    private UserDataDto buyer;
    private Integer count;
    private Integer cost;

}
