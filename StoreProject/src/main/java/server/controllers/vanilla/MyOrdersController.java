package server.controllers.vanilla;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import server.entities.user.model.UserDataModel;
import server.security.details.UserDetailImpl;
import server.shop.bl.services.StoreService;

@Controller
public class MyOrdersController {

    @Autowired
    private StoreService storeService;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/my_orders", method = RequestMethod.GET)
    public String getPage(Authentication authentication, Model model){

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();

        model.addAttribute("orders", storeService.findNotAcceptedOrdersByBuyerId(userDataModel));
        model.addAttribute("userData", userDataModel);

        return "my_orders";
    }


}
