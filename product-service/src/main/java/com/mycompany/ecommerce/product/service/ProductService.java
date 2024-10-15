package com.mycompany.ecommerce.product.service;

import com.mycompany.ecommerce.product.model.Product;
import com.mycompany.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        kafkaTemplate.send("produtos", "ProdutoCriado", savedProduct.getId().toString());
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("topic: {}","produtos");
        log.info("key: {}","ProdutoCriado");
        log.info("Mensagem: {}", savedProduct.getId().toString());        
        log.info("*********************************");
        return savedProduct;
    }

    public Product updateProduct(Long id, Product product) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        Product updatedProduct = productRepository.save(existingProduct);
        kafkaTemplate.send("produtos", "ProdutoAtualizado", updatedProduct.getId().toString());
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("topic: {}","produtos");
        log.info("key: {}","ProdutoAtualizado");
        log.info("Mensagem: {}", updatedProduct.getId().toString());        
        log.info("*********************************");
        return updatedProduct;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        kafkaTemplate.send("produtos", "ProdutoRemovido", id.toString());
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("topic: {}","produtos");
        log.info("key: {}","ProdutoRemovido");
        log.info("Mensagem: {}", id.toString());        
        log.info("*********************************");
    }

    public Product updateStock(Long id, Integer quantity) {
        Product product = getProductById(id);
        product.setStock(product.getStock() + quantity);
        Product updatedProduct = productRepository.save(product);
        kafkaTemplate.send("produtos", "EstoqueAtualizado", updatedProduct.getId().toString());
        log.info("*********************************");
        log.info("Mensagem enviada:");
        log.info("topic: {}","produtos");
        log.info("key: {}","EstoqueAtualizado");
        log.info("Mensagem: {}", updatedProduct.getId().toString());        
        log.info("*********************************");        
        return updatedProduct;
    }
}