package com.andela.irrigation_system.persistence;

import com.andela.irrigation_system.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByEmailLike(Pageable pageable, String filter);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

