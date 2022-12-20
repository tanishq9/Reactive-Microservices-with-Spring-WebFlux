package com.example.orderservice.repository;

import com.example.orderservice.entity.PurchaseOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<PurchaseOrder, Integer> {
	List<PurchaseOrder> findByUserId(int userId);
}
