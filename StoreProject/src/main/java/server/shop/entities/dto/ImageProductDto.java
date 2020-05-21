package server.shop.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "productDto")
public class ImageProductDto {

    private Long id;

    private String imageName;

    @JsonIgnore
    private ProductDto productDto;

}
