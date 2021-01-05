package server.controllers.vanilla;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import server.bl.services.file.FileService;
import server.entities.file.dto.FileDto;
import server.entities.user.model.UserDataModel;
import server.security.details.UserDetailImpl;
import server.shop.bl.services.DataProductService;
import server.shop.bl.services.StoreService;
import server.shop.entities.dto.ImageProductDto;
import server.shop.entities.dto.ProductDto;
import server.shop.entities.model.ProductState;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
public class OfferForSaleController {

    @Autowired
    private FileService fileService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private DataProductService checkDataProductService;

    @PreAuthorize("hasAuthority('SELLER')")
    @RequestMapping(value = "/offer_for_sale", method = RequestMethod.GET)
    public String getPage(Authentication authentication, Model model) {

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();

        model.addAttribute("userData", userDataModel);
        return "offer_for_sale";

    }

    @PreAuthorize("hasAuthority('SELLER')")
    @RequestMapping(value = "/offer_for_sale", method = RequestMethod.POST)
    public String addProduct(Authentication authentication, Model model,
                             @RequestParam("image") MultipartFile[] image,
                             @RequestParam("name") String productName,
                             @RequestParam("description") String description,
                             @RequestParam("maxCost") Integer maxCost,
                             @RequestParam("minCost") Integer minCost,
                             @RequestParam("count") Integer count,
                             @RequestParam("decrease") Integer decrease
    ) {

        List<MultipartFile> images = Arrays.asList(image);

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();

        ProductDto productDto = ProductDto.builder()
                .name(productName)
                .description(description)
                .maxCost(maxCost)
                .minCost(minCost)
                .decrease(decrease)
                .count(count)
                .sellerId(userDataModel.getId())
                .productState(ProductState.IN_STOCK)
                .time(System.currentTimeMillis() / 1000)
                .build();


        if (fileService.checkExtension(images)) {
            String error = checkDataProductService.checkData(productDto);
            if (error == null) {
                List<ImageProductDto> imageProducts = new LinkedList<>();

                for (MultipartFile file : images) {
                    FileDto fileDto = uploadImage(file, userDataModel);
                    ImageProductDto imageProductDto = ImageProductDto.builder()
                            .imageName(fileDto.getStorageName())
                            .productDto(productDto)
                            .build();

                    imageProducts.add(imageProductDto);
                }

                productDto.setImages(imageProducts);
                storeService.saveProduct(productDto);
            } else {
                model.addAttribute("error", error);
            }
        } else {
            model.addAttribute("error", "is not image");
        }
        return getPage(authentication, model);

    }

    private FileDto uploadImage(MultipartFile file, UserDataModel userDataModel) {
        return fileService.saveFile(file, userDataModel);
    }


}
