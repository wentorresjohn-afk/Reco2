package com.ucr.reco.repository;


import com.ucr.reco.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceJpaRepository extends JpaRepository<Space, Integer> {

    boolean existsByName(String name);//Valida si el nombre existe

    Space getByName(String name);//Busca y retorna un objeto tipo space por nombre



}
