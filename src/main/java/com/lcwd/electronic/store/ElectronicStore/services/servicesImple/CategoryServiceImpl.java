package com.lcwd.electronic.store.ElectronicStore.services.servicesImple;

import com.lcwd.electronic.store.ElectronicStore.dtos.CategoriesDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import com.lcwd.electronic.store.ElectronicStore.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.Helper;
import com.lcwd.electronic.store.ElectronicStore.reposittories.CategoriesRepository;
import com.lcwd.electronic.store.ElectronicStore.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    //@Autowired
    //private CategoryService categoryService;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CategoriesDto create(CategoriesDto categoriesDto) {
        String categoryId = UUID.randomUUID().toString();
        categoriesDto.setCategoryId(categoryId);
        Category category = mapper.map(categoriesDto, Category.class);
        Category savedCategory = categoriesRepository.save(category);
        return mapper.map(savedCategory,CategoriesDto.class);

    }

    @Override
    public CategoriesDto update(CategoriesDto categoryDto, String categoryId) {

        //get category of given id
        Category category = categoriesRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found Exception !!"));
        //update category
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updateCategory = categoriesRepository.save(category);
        return mapper.map(updateCategory,CategoriesDto.class);

    }

    @Override
    public void delete(String categoryId) {

        Category category = categoriesRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found Exception !!"));
        categoriesRepository.delete(category);


    }

    @Override
    public PageableResponse<CategoriesDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page = categoriesRepository.findAll(pageable);
        PageableResponse<CategoriesDto> pageableResponse = Helper.getPagableresponse(page, CategoriesDto.class);

        return pageableResponse;
    }

    @Override
    public CategoriesDto get(String categoryId) {

        Category category = categoriesRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found Exception !!"));
        return mapper.map(category,CategoriesDto.class);
    }
}
