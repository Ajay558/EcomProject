package com.lcwd.electronic.store.ElectronicStore.dtos;

import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {


    private String productId;
    private String title;
    private String description;
    private int price;
    private int discountedPrice;
    private int  quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImageName;
    private CategoriesDto category;
}
