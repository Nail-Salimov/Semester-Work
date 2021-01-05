package server.shop.bl.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.bl.repositories.UserRepository;
import server.bl.repositories.file.FileRepository;
import server.entities.user.dto.UserDataDto;
import server.entities.user.model.UserDataModel;
import server.shop.bl.repositories.OrderRepositoryJpa;
import server.shop.bl.repositories.ReviewRepository;
import server.shop.bl.repositories.StoreRepositoryJpa;
import server.shop.entities.dto.ImageProductDto;
import server.shop.entities.dto.OrderDto;
import server.shop.entities.dto.ProductDto;
import server.shop.entities.dto.ReviewDto;
import server.shop.entities.model.*;

import java.util.*;

@Component
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepositoryJpa storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private OrderRepositoryJpa orderRepository;

    @Autowired
    private ReviewRepository reviewRepository;


    @Override
    public void saveProduct(ProductDto productDto) {

        ProductModel model = ProductModel.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .maxCost(productDto.getMaxCost())
                .minCost(productDto.getMinCost())
                .sellerId(productDto.getSellerId())
                .decrease(productDto.getDecrease())
                .count(productDto.getCount())
                .time(productDto.getTime())
                .productState(productDto.getProductState())
                .stars(0.0)
                .starsCount(0)
                .build();

        model.setImages(getImagesModel(productDto.getImages(), model));
        storeRepository.save(model);
    }

    @Override
    public List<ProductDto> findAllProducts() {
        List<ProductDto> productDtoList = new LinkedList<>();


        List<ProductModel> productModelList = storeRepository.findAll();

        for (ProductModel pm : productModelList) {

            ProductDto dto = ProductDto.getProductDto(pm);
            dto.setImages(getImagesDto(pm.getImages(), dto));
            productDtoList.add(dto);
        }
        return productDtoList;
    }

    @Override
    public Optional<ProductDto> findProductById(Long id) {
        Optional<ProductModel> optionalModel = storeRepository.findById(id);

        if (optionalModel.isPresent()) {
            ProductDto productDto = ProductDto.getProductDto(optionalModel.get());
            productDto.setImages(getImagesDto(optionalModel.get().getImages(), productDto));
            List<ReviewModel> reviewModelSet = reviewRepository.findAllReviewsByProduct(optionalModel.get());
            productDto.setReviews(castToSetReviewDto(reviewModelSet));
            return Optional.of(productDto);

        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean buyProduct(ProductDto productDto, Integer cost, Integer count, UserDataModel userDataModel) {
        Optional<ProductModel> optionalModel = storeRepository.findById(productDto.getId());

        if (optionalModel.isPresent()) {
            ProductModel productModel = optionalModel.get();
            if (storeRepository.buyProduct(productModel, count)) {

                OrderModel orderModel = OrderModel.builder()
                        .buyerId(userDataModel.getId())
                        .product(productModel)
                        .sellerId(productModel.getSellerId())
                        .orderState(OrderState.EXPECTED)
                        .count(count)
                        .cost(cost)
                        .build();

                orderRepository.save(orderModel);
                return true;
            } else {
                return false;
            }
        }
        throw new IllegalArgumentException("product not found");
    }

    @Override
    public boolean buyProduct(ProductDto productDto, Integer cost, Integer count, UserDataDto userDataDto) {
        Optional<ProductModel> optionalModel = storeRepository.findById(productDto.getId());

        if (optionalModel.isPresent()) {
            ProductModel productModel = optionalModel.get();
            if (storeRepository.buyProduct(productModel, count)) {

                OrderModel orderModel = OrderModel.builder()
                        .buyerId(userDataDto.getId())
                        .product(productModel)
                        .sellerId(productModel.getSellerId())
                        .orderState(OrderState.EXPECTED)
                        .count(count)
                        .cost(cost)
                        .build();

                orderRepository.save(orderModel);
                return true;
            } else {
                return false;
            }
        }
        throw new IllegalArgumentException("product not found");
    }

    @Override
    public Long productsCount() {
        return storeRepository.productsCount();
    }

    @Override
    public List<OrderDto> findAllOrdersBySellerId(UserDataModel userDataModel) {
        List<OrderModel> orderModelList = orderRepository.findAllBySellerId(userDataModel.getId());
        List<OrderDto> orderDtoList = new LinkedList<>();

        for (OrderModel model : orderModelList) {
            orderDtoList.add(OrderDto.builder()
                    .orderState(model.getOrderState())
                    .buyerId(model.getBuyerId())
                    .id(model.getId())
                    .count(model.getCount())
                    .cost(model.getCost())
                    .sellerId(model.getSellerId())
                    .product(createProductDto(model.getProduct()))
                    .build());
        }
        return orderDtoList;
    }

    @Override
    public List<ProductDto> findProductsByPagination(int page, int size) {
        List<ProductDto> productDtoList = new LinkedList<>();
        List<ProductModel> productModelList = storeRepository.findByPagination(page, size);
        for (ProductModel model : productModelList) {
            productDtoList.add(createProductDto(model));
        }
        return productDtoList;
    }

    @Override
    public List<ProductDto> findProductsBySimilarName(String name) {
        List<ProductDto> productDtoList = new LinkedList<>();
        List<ProductModel> productModelList = storeRepository.findBySimilarName(name);
        for (ProductModel model : productModelList) {
            productDtoList.add(createProductDto(model));
        }
        return productDtoList;
    }

    @Override
    public List<OrderDto> findNotAcceptedOrdersByBuyerId(UserDataModel userDataModel) {
        List<OrderModel> orderModelList = orderRepository.findNotAcceptedOrderByBuyerId(userDataModel.getId());
        List<OrderDto> orderDtoList = new LinkedList<>();

        for (OrderModel model : orderModelList) {
            orderDtoList.add(OrderDto.builder()
                    .orderState(model.getOrderState())
                    .buyerId(model.getBuyerId())
                    .id(model.getId())
                    .sellerId(model.getSellerId())
                    .cost(model.getCost())
                    .count(model.getCount())
                    .product(createProductDto(model.getProduct()))
                    .build());
        }
        return orderDtoList;
    }

    @Override
    public Optional<OrderDto> findOrderById(Long id) {
        Optional<OrderModel> orderModel = orderRepository.find(id);

        if (orderModel.isPresent()) {
            OrderModel model = orderModel.get();
            return Optional.of(OrderDto.builder()
                    .orderState(model.getOrderState())
                    .buyerId(model.getBuyerId())
                    .count(model.getCount())
                    .id(model.getId())
                    .cost(model.getCost())
                    .sellerId(model.getSellerId())
                    .product(createProductDto(model.getProduct()))
                    .build());
        }
        return Optional.empty();
    }

    @Override
    public void acceptOrder(OrderDto orderDto, UserDataModel userDataModel) {
        long userId1 = orderDto.getBuyerId();
        long userId2 = userDataModel.getId();

        if (userId1 != userId2) {
            throw new IllegalStateException("Заказ не принадлежит пользователю");
        }

        orderDto.setOrderState(OrderState.ACCEPTED);

        OrderModel orderModel = OrderModel.builder()
                .sellerId(orderDto.getSellerId())
                .product(createProductModel(orderDto.getProduct()))
                .buyerId(orderDto.getBuyerId())
                .id(orderDto.getId())
                .orderState(orderDto.getOrderState())
                .count(orderDto.getCount())
                .build();


        orderRepository.update(orderModel);
    }

    @Override
    public void acceptOrder(OrderDto orderDto, UserDataDto userDataDto) {
        long userId1 = orderDto.getBuyerId();
        long userId2 = userDataDto.getId();

        if (userId1 != userId2) {
            throw new IllegalStateException("Заказ не принадлежит пользователю");
        }

        orderDto.setOrderState(OrderState.ACCEPTED);

        OrderModel orderModel = OrderModel.builder()
                .sellerId(orderDto.getSellerId())
                .product(createProductModel(orderDto.getProduct()))
                .buyerId(orderDto.getBuyerId())
                .id(orderDto.getId())
                .orderState(orderDto.getOrderState())
                .cost(orderDto.getCost())
                .count(orderDto.getCount())
                .build();


        orderRepository.update(orderModel);
    }

    @Override
    public boolean isOrdered(Long orderId, Long userId) {
        return orderRepository.isOrdered(orderId, userId);
    }

    @Override
    public ReviewDto addReview(ReviewDto reviewDto) {
        Optional<ProductDto> optional = findProductById(reviewDto.getProduct().getId());
        if(!optional.isPresent()){
            throw new IllegalArgumentException("product not found");
        }

        Optional<ProductModel> optionalProductModel = storeRepository.findById(optional.get().getId());
        if(!optionalProductModel.isPresent()){
            throw new IllegalArgumentException("product not found");
        }
        ProductModel productModel = optionalProductModel.get();

        ReviewModel reviewModel = ReviewModel.builder()
                .product(productModel)
                .stars(reviewDto.getStars())
                .text(reviewDto.getText())
                .userId(reviewDto.getUserId())
                .build();


        Double stars = productModel.getStars();
        Integer starsCount = productModel.getStarsCount();
        productModel.setStars(stars+reviewDto.getStars());
        productModel.setStarsCount(starsCount+1);

        storeRepository.updateProduct(productModel);
        return ReviewDto.getReviewDto(reviewRepository.addReview(reviewModel));
    }

    private List<ImageProductModel> getImagesModel(List<ImageProductDto> list, ProductModel model) {
        List<ImageProductModel> images = new LinkedList<>();

        for (ImageProductDto dto : list) {
            images.add(ImageProductModel.builder()
                    .imageName(dto.getImageName())
                    .productModel(model)
                    .build()
            );
        }
        return images;
    }

    private List<ImageProductDto> getImagesDto(List<ImageProductModel> list, ProductDto dto) {
        List<ImageProductDto> images = new LinkedList<>();

        for (ImageProductModel model : list) {
            images.add(ImageProductDto.builder()
                    .imageName(model.getImageName())
                    .productDto(dto)
                    .build());
        }

        return images;
    }

    private ProductDto createProductDto(ProductModel productModel) {
        ProductDto productDto = ProductDto.getProductDto(productModel);
        productDto.setImages(getImagesDto(productModel.getImages(), productDto));
        return productDto;
    }

    private ProductModel createProductModel(ProductDto productDto) {
        ProductModel productModel = ProductModel.builder()
                .name(productDto.getName())
                .productState(productDto.getProductState())
                .count(productDto.getCount())
                .decrease(productDto.getDecrease())
                .description(productDto.getDescription())
                .id(productDto.getId())
                .maxCost(productDto.getMaxCost())
                .minCost(productDto.getMinCost())
                .sellerId(productDto.getSellerId())
                .time(productDto.getTime())
                .build();
        productModel.setImages(getImagesModel(productDto.getImages(), productModel));

        return productModel;
    }

    private Set<ReviewDto> castToSetReviewDto(Set<ReviewModel> reviewModelSet){
        Set<ReviewDto> set = new HashSet<>();
        for(ReviewModel model : reviewModelSet){
            set.add(ReviewDto.getReviewDto(model));
        }
        return set;
    }

    private Set<ReviewDto> castToSetReviewDto(List<ReviewModel> reviewModelSet){
        Set<ReviewDto> set = new HashSet<>();
        for(ReviewModel model : reviewModelSet){
            ReviewDto reviewDto = ReviewDto.getReviewDto(model);
            Optional<UserDataModel> optionalUserDataModel = userRepository.findById(reviewDto.getUserId());
            if(!optionalUserDataModel.isPresent()){
                throw new IllegalArgumentException("user not found");
            }
            reviewDto.setUserDataDto(UserDataDto.getUserDataDto(optionalUserDataModel.get()));
            set.add(reviewDto);
        }
        return set;
    }


}
