package com.chozoi.convertdata.convert.app.controllers;

import com.chozoi.convertdata.convert.app.dtos.ProductAuctionDTO;
import com.chozoi.convertdata.convert.app.dtos.ShopProductDTO;
import com.chozoi.convertdata.convert.domain.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/convert")
public class ProductController {

  @Autowired private ProductService productService;

  @GetMapping("product")
  public Object productStats(Pageable pageable) {
    return productService.getAllProduct(pageable);
  }

  @PostMapping("product")
  public Object productStats(@RequestBody @Valid ProductAuctionDTO dto) {
    return productService.getProductAuction(dto.getProductIds());
  }

  @PostMapping("shop_product")
  public Object productStats(@RequestBody @Valid ShopProductDTO dto) {
    return productService.getShopProduct(dto.getShop_id());
  }
}
