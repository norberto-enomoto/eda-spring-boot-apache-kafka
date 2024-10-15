package com.mycompany.ecommerce.customer.service;

import com.mycompany.ecommerce.customer.model.Customer;
import com.mycompany.ecommerce.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer createCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        kafkaTemplate.send("clientes", "ClienteRegistrado", savedCustomer.getId().toString());
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("topic: {}","clientes");
        log.info("key: {}","ClienteRegistrado");
        log.info("Mensagem: {}", savedCustomer.getId().toString());        
        log.info("*********************************");
        return savedCustomer;
    }

    public Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = getCustomerById(id);
        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        kafkaTemplate.send("clientes", "PerfilAtualizado", updatedCustomer.getId().toString());
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("topic: {}","cliente");
        log.info("key: {}","PerfilAtualizado");
        log.info("Mensagem: {}", updatedCustomer.getId().toString());
        log.info("*********************************");
        return updatedCustomer;
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
        kafkaTemplate.send("clientes", "ClienteDesativado", id.toString());
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("topic: {}","cliente");
        log.info("key: {}","ClienteDesativado");
        log.info("Mensagem: {}", id.toString());
        log.info("*********************************");
        
    }
}