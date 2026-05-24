package com.exam.servicecenter.repository;

import com.exam.servicecenter.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("""
            select u from UserEntity u
            where (:search is null or :search = '' or lower(u.username) like lower(concat('%', :search, '%')))
            """)
    Page<UserEntity> findFiltered(@Param("search") String search, Pageable pageable);
}
