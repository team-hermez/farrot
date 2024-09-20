package com.hermez.farrot.wishlist.dto;

import com.hermez.farrot.product.entity.Product;
import com.hermez.farrot.product.entity.ProductStatus;
import lombok.Data;

@Data
public class WishlistDTO {

  private Integer id;

  private String productName;

  private Integer price;

  private String description;

  private ProductStatus productStatus;

  public WishlistDTO(Product product) {
    this.id = product.getId();
    this.productName = product.getProductName();
    this.price = product.getPrice();
    this.description = product.getDescription();
    this.productStatus = product.getProductStatus();
  }

}
