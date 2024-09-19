package com.hermez.farrot.product.dto.response;

import com.hermez.farrot.image.entity.Image;
import com.hermez.farrot.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.N;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithImage {

    private Product product;

    private List<Image> images;

}