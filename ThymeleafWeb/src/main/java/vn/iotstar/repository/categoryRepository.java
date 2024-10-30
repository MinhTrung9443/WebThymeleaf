package vn.iotstar.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.iotstar.entity.Category;

public interface categoryRepository extends JpaRepository<Category, Long >{
	Optional<Category> findByName (String name);
	Page<Category> findByNameContaining(String name, Pageable pageable);

}
