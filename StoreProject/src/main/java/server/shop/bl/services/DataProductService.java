package server.shop.bl.services;

import server.shop.entities.dto.ProductDto;

public interface DataProductService {
    String checkData(ProductDto productDto);
    int calculateCost(ProductDto productDto);
}
