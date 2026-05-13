package com.yuno.payment.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yuno.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

	@Query("""
			SELECT p
			FROM Payment p
			ORDER BY
			CASE
			    WHEN p.status = 'FAILED' THEN 0
			    ELSE 1
			END,
			p.createdAt DESC
			""")
	List<Payment> findAllPaymentsOrderByFailurePriority();
}