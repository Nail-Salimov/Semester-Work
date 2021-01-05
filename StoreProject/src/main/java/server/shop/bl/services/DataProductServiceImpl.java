package server.shop.bl.services;

import org.springframework.stereotype.Component;
import server.shop.entities.dto.ProductDto;

@Component
public class DataProductServiceImpl implements DataProductService {

    @Override
    public String checkData(ProductDto productDto) {

        if (productDto.getMinCost() <= 0 || productDto.getMaxCost() <= 0
                || productDto.getCount() <= 0 || productDto.getDecrease() <= 0) {
            return "Значения должны быть только положительными";
        } else if (productDto.getMaxCost() <= productDto.getMinCost()) {
            return "Максимальная стоимость должно быть больше минимальной";
        } else if (productDto.getMaxCost() - productDto.getMinCost() < productDto.getDecrease()) {
            return "Большой шаг уменьшения";
        } else {
            return null;
        }
    }

    @Override
    public int calculateCost(ProductDto productDto) {
        long currentTime = System.currentTimeMillis() / 1000;
        long productTime = productDto.getTime();

        long hour = ((currentTime - productTime) / (60 * 60));

        if (productDto.getMaxCost() - (hour * productDto.getDecrease()) >= productDto.getMinCost()) {
            return (int) (productDto.getMaxCost() - (hour * productDto.getDecrease()));
        } else {
            return productDto.getMinCost();
        }
    }
}
