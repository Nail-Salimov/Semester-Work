package server.controllers.vanilla;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import server.bl.services.UserService;
import server.entities.user.dto.UserDataDto;
import server.entities.user.model.UserDataModel;
import server.security.details.UserDetailImpl;
import server.shop.bl.services.StoreService;
import server.shop.entities.dto.OrderDto;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
public class OrdersController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('SELLER')")
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String getPage(Authentication authentication, Model model) {

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();

        List<OrderDto> orderDtoList = storeService.findAllOrdersBySellerId(userDataModel);
        List<UserDataDto> listBuyers = new LinkedList<>();

        for (OrderDto order : orderDtoList) {
            Optional<UserDataDto> optionalUserDto = userService.findUserById(order.getBuyerId());

            optionalUserDto.ifPresent(order::setBuyer);
        }

        model.addAttribute("orders", orderDtoList);
        model.addAttribute("userData", userDataModel);

        return "orders";
    }

}
