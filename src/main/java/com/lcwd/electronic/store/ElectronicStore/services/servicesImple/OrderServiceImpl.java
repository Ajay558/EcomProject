package com.lcwd.electronic.store.ElectronicStore.services.servicesImple;

import com.lcwd.electronic.store.ElectronicStore.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.OrderDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.entities.*;
import com.lcwd.electronic.store.ElectronicStore.exception.BadApiRequestException;
import com.lcwd.electronic.store.ElectronicStore.exception.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.Helper;
import com.lcwd.electronic.store.ElectronicStore.reposittories.CartRepository;
import com.lcwd.electronic.store.ElectronicStore.reposittories.OrderRepository;
import com.lcwd.electronic.store.ElectronicStore.reposittories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {

        String userId= orderDto.getUserId();
        String cartId = orderDto.getCartId();
        //fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given Id !!"));
        //fetch cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found !!"));

        List<CartItem> cartItems = cart.getItems();

        if(cartItems.size()<=0){
            throw new BadApiRequestException("Invalid number of items in cart !! ");

        }
        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();
        //orderItems, amount

        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {

            //CartItem->OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());

            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrderId(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found !!"));
        orderRepository.delete(order);

    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("UserNot found !!"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order->modelMapper.map(order,OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page = orderRepository.findAll(pageable);
        return Helper.getPagableresponse(page,OrderDto.class);
    }
}
