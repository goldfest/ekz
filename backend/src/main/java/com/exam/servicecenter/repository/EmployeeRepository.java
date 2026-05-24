package com.exam.servicecenter.repository;

import com.exam.servicecenter.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    @Query("""
            select e from EmployeeEntity e
            where (:search is null or :search = ''
                    or lower(e.fullName) like lower(concat('%', :search, '%'))
                    or lower(e.position) like lower(concat('%', :search, '%')))
              and (:active is null or e.active = :active)
            """)
    Page<EmployeeEntity> findFiltered(@Param("search") String search,
                                      @Param("active") Boolean active,
                                      Pageable pageable);
}
