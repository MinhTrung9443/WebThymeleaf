package vn.iotstar.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.iotstar.entity.Category;
import vn.iotstar.repository.categoryRepository;
import vn.iotstar.service.categoryService;


@Service
public class categoryServiceImpl implements categoryService {
	@Autowired
	categoryRepository caterepo;

	@Override
	public Optional<Category> findByName(String name) {
		return caterepo.findByName(name);
	}

	@Override
	public Page<Category> findByNameContaining(String name, Pageable pageable) {
		return caterepo.findByNameContaining(name, pageable);
	}

	@Override
	public <S extends Category> S save(S entity) {
		return caterepo.save(entity);
	}

	@Override
	public Page<Category> findAll(Pageable pageable) {
		return caterepo.findAll(pageable);
	}

	@Override
	public Optional<Category> findById(Long id) {
		return caterepo.findById(id);
	}

	@Override
	public List<Category> findAll() {
		return caterepo.findAll();
	}

	@Override
	public long count() {
		return caterepo.count();
	}

	@Override
	public void deleteById(Long id) {
		caterepo.deleteById(id);
	}
	
}
