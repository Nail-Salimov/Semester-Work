package server.controllers.vanilla;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import server.entities.user.model.UserDataModel;
import server.exceptions.ResourceNotFoundException;
import server.security.details.UserDetailImpl;
import server.shop.bl.services.StoreService;
import server.shop.entities.dto.ProductDto;

import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private StoreService storeService;

    @RequestMapping(value = "/product/{product-id:.+}", method = RequestMethod.GET)
    public String getPage(Authentication authentication, Model model,
                          @PathVariable("product-id") Long productId) {

        Optional<ProductDto> optionalProductDto = storeService.findProductById(productId);


        if (optionalProductDto.isPresent()) {
            ProductDto productDto = optionalProductDto.get();
            model.addAttribute("productData", productDto);

            UserDataModel userDataModel = null;
            if (authentication != null) {
                UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
                userDataModel = userDetail.getUser();
            }
            model.addAttribute("userData", userDataModel);

            return "product";
        }
        throw new ResourceNotFoundException();
    }
}
