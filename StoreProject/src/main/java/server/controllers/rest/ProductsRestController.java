package server.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.bl.services.file.FileService;
import server.entities.file.dto.FileDto;
import server.entities.user.dto.UserDataDto;
import server.security.jwt.details.UserDetailsJwtImpl;
import server.shop.bl.services.DataProductService;
import server.shop.bl.services.StoreService;
import server.shop.entities.dto.ImageProductDto;
import server.shop.entities.dto.ProductDto;
import server.shop.entities.model.ProductState;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductsRestController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private DataProductService checkService;

    @Autowired
    private FileService fileService;

    @GetMapping("/api/products")
    @ResponseBody
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam("page") Integer page,
                                                        @RequestParam("size") Integer size) {

        if (page <= 0 || size <= 0) {
            return ResponseEntity.ok(new LinkedList<>());
        }

        return ResponseEntity.ok(storeService.findProductsByPagination(page, size));
    }

    @GetMapping("/api/product")
    @ResponseBody
    public ResponseEntity<ProductDto> getProduct(@RequestParam("id") Long id) {

        Optional<ProductDto> optionalProductDto = storeService.findProductById(id);

        return optionalProductDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(new ProductDto()));
    }

    @PreAuthorize("hasAuthority('SELLER')")
    @PostMapping("/api/product")
    public ResponseEntity<String> addProduct(@RequestBody MultipartFile[] multipartFiles,
                                             @RequestBody ProductDto productDto) {

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

        List<MultipartFile> images = Arrays.asList(multipartFiles);

        productDto.setSellerId(userDetailsJwt.getUserId());
        productDto.setProductState(ProductState.IN_STOCK);
        productDto.setTime(System.currentTimeMillis() / 1000);


        if (fileService.checkExtension(images)) {
            String error = checkService.checkData(productDto);
            if (error == null) {
                List<ImageProductDto> imageProducts = new LinkedList<>();

                for (MultipartFile file : images) {
                    FileDto fileDto = uploadImage(file, userDto);
                    ImageProductDto imageProductDto = ImageProductDto.builder()
                            .imageName(fileDto.getStorageName())
                            .productDto(productDto)
                            .build();

                    imageProducts.add(imageProductDto);
                }

                productDto.setImages(imageProducts);
                storeService.saveProduct(productDto);
                return ResponseEntity.ok("added");
            } else {
                return ResponseEntity.ok(error);
            }
        } else {
           return ResponseEntity.ok("is not image");
        }
    }

    private FileDto uploadImage(MultipartFile file, UserDataDto userDataDto) {
        return fileService.saveFile(file, userDataDto);
    }
}
