package server.controllers.vanilla;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import server.entities.user.model.UserDataModel;
import server.security.details.UserDetailImpl;
import server.shop.bl.services.StoreService;

@Controller
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PreAuthorize("permitAll()")
    @GetMapping("/store")
    public String getPage(Authentication authentication, Model model,
                          @RequestParam(value = "page", defaultValue = "1") String pageStr) {



        UserDataModel userDataModel= null;


        if (authentication != null) {
            UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
            userDataModel = userDetail.getUser();
        }

        int page = isNumber(pageStr)?Integer.parseInt(pageStr):1;
        


        model.addAttribute("listProduct",storeService.findProductsByPagination(page, 3));
        model.addAttribute("productsCount", storeService.productsCount());
        model.addAttribute("page", String.valueOf(page));
        model.addAttribute("userData", userDataModel);
        return "store";
    }

    private boolean isNumber(String page){
        return page.matches("[-+]?\\d+");
    }
}
