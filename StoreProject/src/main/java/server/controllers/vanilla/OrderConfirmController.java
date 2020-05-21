package server.controllers.vanilla;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import server.entities.user.model.UserDataModel;
import server.exceptions.ResourceNotFoundException;
import server.security.details.UserDetailImpl;
import server.shop.bl.services.StoreService;
import server.shop.entities.dto.OrderDto;

import java.util.Optional;

@Controller
public class OrderConfirmController {

    @Autowired
    private StoreService storeService;


    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/order/{order-id:.+}", method = RequestMethod.GET)
    public String getPage(Authentication authentication, Model model,
                          @PathVariable("order-id") Long orderId) {

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();
        model.addAttribute("userData", userDataModel);


        Optional<OrderDto> optionalOrderDto = storeService.findOrderById(orderId);


        if (optionalOrderDto.isPresent()) {
            OrderDto orderDto = optionalOrderDto.get();


            model.addAttribute("order", orderDto);

            return "order";
        }
        throw new ResourceNotFoundException();

    }


    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String acceptOrder(Authentication authentication, Model model,
                         @RequestParam("order_id") Long orderId) {

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();
        model.addAttribute("userData", userDataModel);


        Optional<OrderDto> optionalOrderDto = storeService.findOrderById(orderId);


        if (optionalOrderDto.isPresent()) {
            OrderDto orderDto = optionalOrderDto.get();
            storeService.acceptOrder(orderDto, userDataModel);

            return "redirect:/order/" + orderId;
        }
        throw new ResourceNotFoundException();

    }
}
