package com.lcwd.electronic.store.ElectronicStore.reposittories;

import com.lcwd.electronic.store.ElectronicStore.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {

}
