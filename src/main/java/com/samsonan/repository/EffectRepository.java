package com.samsonan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samsonan.model.Effect;

public interface EffectRepository extends JpaRepository<Effect, Long> {

    List<Effect> findByName(String name);
    
}
