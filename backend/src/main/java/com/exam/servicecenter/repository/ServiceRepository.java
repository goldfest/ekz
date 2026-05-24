package com.exam.servicecenter.repository;

import com.exam.servicecenter.entity.ServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    @Query("""
            select s from ServiceEntity s
            where (:search is null or :search = ''
                    or lower(s.title) like lower(concat('%', :search, '%'))
                    or lower(s.category) like lower(concat('%', :search, '%')))
              and (:category is null or :category = '' or lower(s.category) = lower(:category))
              and (:active is null or s.active = :active)
            """)
    Page<ServiceEntity> findFiltered(@Param("search") String search,
                                     @Param("category") String category,
                                     @Param("active") Boolean active,
                                     Pageable pageable);
}
