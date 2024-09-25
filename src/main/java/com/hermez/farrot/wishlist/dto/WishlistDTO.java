package com.hermez.farrot.wishlist.dto;

import com.hermez.farrot.image.entity.Image;
import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.entity.ProductStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistDTO {

  private Integer id;

  private String productName;

  private Integer price;

  private String description;

  private ProductStatus productStatus;

  private List<Image> images = new ArrayList<>();

  private String latestImagePath;

  public WishlistDTO(Product product) {
    this.id = product.getId();
    this.productName = product.getProductName();
    this.price = product.getPrice();
    this.description = product.getDescription();
    this.productStatus = product.getProductStatus();
  }

}
