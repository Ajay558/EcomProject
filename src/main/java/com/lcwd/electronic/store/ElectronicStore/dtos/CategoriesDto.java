package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesDto {

    private String categoryId;

    @NotBlank(message = "Title required !!")
    @Size(min = 4,message = "Must be four char")
    private String title;

    @NotBlank(message = "Description required !!")
    private String description;

    private String coverImage;

}
