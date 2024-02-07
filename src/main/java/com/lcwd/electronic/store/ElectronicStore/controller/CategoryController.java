package com.lcwd.electronic.store.ElectronicStore.controller;

import com.lcwd.electronic.store.ElectronicStore.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.ElectronicStore.dtos.CategoriesDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.services.CategoryService;
import com.lcwd.electronic.store.ElectronicStore.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController
{
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    //Create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoriesDto> createCategory(@Valid @RequestBody CategoriesDto categoryDto ){

        //call service to call object
        CategoriesDto categoriesDto1 = categoryService.create(categoryDto);
        return new ResponseEntity<>(categoriesDto1, HttpStatus.CREATED);

    }
    //Update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoriesDto> updateCategory(
            @PathVariable String categoryId,
            @RequestBody CategoriesDto categoryDto) {

        CategoriesDto updatedCategory = categoryService.update(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);

    }

    //delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(
            @PathVariable String categoryId
    ){
        categoryService.delete(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category is deleted successfully !!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }
    //GetAll

    @GetMapping
    public ResponseEntity<PageableResponse<CategoriesDto>> getAll(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<CategoriesDto> pageableResponse = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }



    //getSingle
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoriesDto> getSingle(@PathVariable String categoryId){
        CategoriesDto categoriesDto = categoryService.get(categoryId);
        return ResponseEntity.ok(categoriesDto);

    }

    //Create product with category

    @PostMapping("{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestBody ProductDto dto
    )
    {
        ProductDto productWithCategory = productService.createWithCategory(dto,categoryId);
        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);

    }

    //update category of product
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryPfProduct(
            @PathVariable String categoryId,
            @PathVariable String productId
    )
    {
         ProductDto productDto = productService.updateCategory(productId,categoryId);
         return new ResponseEntity<>(productDto,HttpStatus.OK);

    }

    //get product of category


    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductsOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir

    )
    {
       PageableResponse<ProductDto>  response= productService.getAllofCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
       return new ResponseEntity<>(response,HttpStatus.OK);

    }





}
