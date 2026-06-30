package com.ucr.reco.repository;

import com.ucr.reco.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer> {
    User getByName(String name);

    boolean existsByEmail(String email);

    boolean existsByPassword(String password);

    User getByEmail(String email);
}
