package com.mycompany.ecommerce.order.service;

import com.mycompany.ecommerce.order.model.Order;
import com.mycompany.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order createOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CREATED");
        Order savedOrder = orderRepository.save(order);
        kafkaTemplate.send("pedidos", "PedidoCriado", savedOrder.getId().toString());
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("topic: {}","pedidos");
        log.info("key: {}","PedidoCriado");
        log.info("Mensagem: {}", savedOrder.getId().toString());        
        log.info("*********************************");
        return savedOrder;
    }

    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        kafkaTemplate.send("pedidos", status, updatedOrder.getId().toString());
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("topic: {}","pedidos");
        log.info("key: {}",status);
        log.info("Mensagem: {}", updatedOrder.getId().toString());        
        log.info("*********************************");        
        return updatedOrder;
    }
}