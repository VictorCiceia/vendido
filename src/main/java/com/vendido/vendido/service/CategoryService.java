package com.vendido.vendido.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendido.vendido.dto.CategoryDTO;
import com.vendido.vendido.entity.CategoryEntity;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.CategoryRepository;
import com.vendido.vendido.service.mapper.CategoryMapper;

@Service
public class CategoryService implements BaseService<CategoryDTO>{
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Page<CategoryDTO> findAll(final Pageable pageable) {
		return this.categoryRepository.findAllByDeleted(false, pageable)//
				.map(this.categoryMapper::toDTO);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public CategoryDTO findById(final long id) throws Exception {
		return this.categoryRepository.findByIdAndDeleted(id, false)//
				.map(this.categoryMapper::toDTO)//
				.orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public CategoryDTO save(final CategoryDTO dto) throws Exception {
		final CategoryEntity entity = this.categoryMapper.toEntity(dto);
		entity.setDeleted(false);
		return this.categoryMapper.toDTO(this.categoryRepository.save(entity));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public CategoryDTO update(final long id, final CategoryDTO dto) throws Exception {
		CategoryEntity entity = this.categoryRepository.findByIdAndDeleted(id, false)//
				.orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));
		entity.setCategory(dto.getCategory());
		return this.categoryMapper.toDTO(this.categoryRepository.save(entity));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(final long id) throws Exception {
		CategoryEntity entity = this.categoryRepository.findByIdAndDeleted(id, false)//
				.orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));
		entity.setDeleted(true);
		this.categoryRepository.save(entity);		
	}

}
