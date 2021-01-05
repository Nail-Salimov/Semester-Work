package server.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.security.jwt.details.UserDetailsJwtImpl;
import server.shop.bl.services.StoreService;
import server.shop.entities.dto.ProductDto;
import server.shop.entities.dto.ReviewDto;
import server.shop.entities.dto.ReviewRequest;

@RestController
public class ReviewRestController {

    @Autowired
    private StoreService storeService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/review")
    public ResponseEntity<ReviewDto> addReview(@RequestBody ReviewRequest reviewRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsJwtImpl userDetailsJwt = (UserDetailsJwtImpl) authentication.getDetails();

        if(!storeService.findProductById(reviewRequest.getProductId()).isPresent()){
            throw new IllegalArgumentException("product not found");
        }

        if(storeService.isOrdered(reviewRequest.getProductId(), userDetailsJwt.getUserId())){
            ReviewDto reviewDto = ReviewDto.builder()
                    .stars(reviewRequest.getStars())
                    .text(reviewRequest.getText())
                    .product(ProductDto.builder().id(reviewRequest.getProductId()).build())
                    .userId(userDetailsJwt.getUserId())
                    .build();

            return ResponseEntity.ok().body(storeService.addReview(reviewDto));
        }else {
            throw new IllegalArgumentException("user has not ordered this product");
        }
    }

}
