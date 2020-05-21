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
import server.security.details.UserDetailImpl;
import server.shop.bl.services.DataProductService;
import server.shop.bl.services.StoreService;
import server.shop.entities.dto.ProductDto;

import java.util.Optional;

@Controller
public class BuyProductController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private DataProductService dataProductService;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/buy/{product-id:.+}", method = RequestMethod.POST)
    public String buyProduct(Authentication authentication, Model model,
                             @PathVariable("product-id") Long productId,
                             @RequestParam("count") Integer count){

        Optional<ProductDto> optionalProductDto = storeService.findProductById(productId);
        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();


        if(optionalProductDto.isPresent()){

            if(userDataModel.getAddress() != null) {
                ProductDto productDto = optionalProductDto.get();

                if (storeService.buyProduct(productDto, dataProductService.calculateCost(productDto),
                        count, userDataModel)) {

                    model.addAttribute("state", "ok");
                } else {
                    model.addAttribute("state", "no");
                }
            }else {
                model.addAttribute("state", "no address");
            }

            return "redirect:/product/" + productId;
        }
        throw new IllegalArgumentException("product has not founded");
    }
}
