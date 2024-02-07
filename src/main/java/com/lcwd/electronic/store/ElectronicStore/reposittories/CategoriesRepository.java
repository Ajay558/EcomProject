package com.lcwd.electronic.store.ElectronicStore.reposittories;


import com.lcwd.electronic.store.ElectronicStore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Category,String> {
}
