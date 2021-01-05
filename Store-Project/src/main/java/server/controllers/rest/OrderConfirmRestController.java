package server.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.entities.ancillary.responce.ResponseDto;
import server.entities.user.dto.UserDataDto;
import server.security.jwt.details.UserDetailsJwtImpl;
import server.shop.bl.services.StoreService;
import server.shop.entities.dto.OrderDto;

import java.util.Optional;

@RestController
public class OrderConfirmRestController {

    @Autowired
    private StoreService storeService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/order")
    public ResponseEntity<ResponseDto> acceptOrder(@RequestBody OrderDto orderDto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsJwtImpl userDetailsJwt = (UserDetailsJwtImpl) authentication.getDetails();

        UserDataDto userDto = UserDataDto.builder()
                .id(userDetailsJwt.getUserId())
                .address(userDetailsJwt.getAddress())
                .mail(userDetailsJwt.getUsername())
                .name(userDetailsJwt.getName())
                .role(userDetailsJwt.getRole())
                .state(userDetailsJwt.getState())
                .build();

        Optional<OrderDto> optionalOrderDto = storeService.findOrderById(orderDto.getId());

        if (optionalOrderDto.isPresent()) {
            orderDto = optionalOrderDto.get();
            storeService.acceptOrder(orderDto, userDto);

            return ResponseEntity.ok().body(new ResponseDto("OK", "ACCEPTED"));
        }
        return ResponseEntity.ok().body(new ResponseDto("ERROR", "The order does not belong to you"));
    }
}
