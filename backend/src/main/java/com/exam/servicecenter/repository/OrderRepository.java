package com.exam.servicecenter.repository;

import com.exam.servicecenter.entity.OrderEntity;
import com.exam.servicecenter.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query(value = """
            select distinct o from OrderEntity o
            left join o.services s
            where (:search is null or :search = ''
                    or lower(o.title) like lower(concat('%', :search, '%'))
                    or lower(coalesce(o.description, '')) like lower(concat('%', :search, '%'))
                    or lower(o.client.fullName) like lower(concat('%', :search, '%')))
              and (:status is null or o.status = :status)
              and (:clientId is null or o.client.id = :clientId)
              and (:employeeId is null or o.employee.id = :employeeId)
              and (:serviceId is null or s.id = :serviceId)
            """,
            countQuery = """
            select count(distinct o) from OrderEntity o
            left join o.services s
            where (:search is null or :search = ''
                    or lower(o.title) like lower(concat('%', :search, '%'))
                    or lower(coalesce(o.description, '')) like lower(concat('%', :search, '%'))
                    or lower(o.client.fullName) like lower(concat('%', :search, '%')))
              and (:status is null or o.status = :status)
              and (:clientId is null or o.client.id = :clientId)
              and (:employeeId is null or o.employee.id = :employeeId)
              and (:serviceId is null or s.id = :serviceId)
            """)
    Page<OrderEntity> findFiltered(@Param("search") String search,
                                   @Param("status") OrderStatus status,
                                   @Param("clientId") Long clientId,
                                   @Param("employeeId") Long employeeId,
                                   @Param("serviceId") Long serviceId,
                                   Pageable pageable);
}
