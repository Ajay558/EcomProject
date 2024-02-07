package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;

import java.util.List;

public interface OrderService
{
    // create order
    OrderDto createOrder(CreateOrderRequest orderDto);

    //remove order
    void removeOrderId(String orderId);

    //get order of user
    List<OrderDto> getOrdersOfUser(String userId);

    //get orders

    PageableResponse<OrderDto> getOrders(int pageNumber,int pageSize,String sortBy,String sortDir );


    //other methods(logic)
}
