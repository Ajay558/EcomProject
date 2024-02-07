package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.CategoriesDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;

public interface CategoryService {

    //Create
    CategoriesDto create(CategoriesDto categoriesDto);

    //update
    CategoriesDto update(CategoriesDto categoryDto,String categoryId);

    //delete
    void delete(String categoryId);

    //getAll
    PageableResponse<CategoriesDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

    //getSingle
    CategoriesDto get(String categoryId);
}
