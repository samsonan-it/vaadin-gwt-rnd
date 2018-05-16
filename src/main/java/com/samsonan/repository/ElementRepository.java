package com.samsonan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samsonan.model.Element;

public interface ElementRepository extends JpaRepository<Element, Long> {

}
