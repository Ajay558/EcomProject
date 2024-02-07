package com.lcwd.electronic.store.ElectronicStore.reposittories;

import com.lcwd.electronic.store.ElectronicStore.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {


}
