package server.controllers.vanilla;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import server.entities.user.model.UserDataModel;
import server.security.details.UserDetailImpl;
import server.shop.bl.services.StoreService;
import server.shop.entities.dto.ProductDto;

import java.util.List;

@Controller
public class ProductSearchingController {

    @Autowired
    private StoreService storeService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String getPage(Authentication authentication, Model model,
    @RequestParam("searchText") String searchWord){

        UserDataModel userDataModel = null;

        if(authentication != null) {
            UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
            userDataModel = userDetail.getUser();
        }
        List<ProductDto> productDtoList = storeService.findProductsBySimilarName(searchWord);

        model.addAttribute("products", productDtoList);
        model.addAttribute("userData", userDataModel);
        model.addAttribute("search_word", searchWord);

        return "search_result";
    }
}
