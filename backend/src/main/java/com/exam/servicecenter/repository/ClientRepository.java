package com.exam.servicecenter.repository;

import com.exam.servicecenter.entity.ClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    @Query("""
            select c from ClientEntity c
            where (:search is null or :search = ''
                    or lower(c.fullName) like lower(concat('%', :search, '%'))
                    or lower(c.phone) like lower(concat('%', :search, '%'))
                    or lower(coalesce(c.email, '')) like lower(concat('%', :search, '%')))
              and (:active is null or c.active = :active)
            """)
    Page<ClientEntity> findFiltered(@Param("search") String search,
                                    @Param("active") Boolean active,
                                    Pageable pageable);
}
