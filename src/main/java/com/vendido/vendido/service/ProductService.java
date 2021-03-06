package com.vendido.vendido.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendido.vendido.dto.CategoryDTO;
import com.vendido.vendido.dto.ProductDTO;
import com.vendido.vendido.entity.ProductEntity;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.ProductRepository;
import com.vendido.vendido.resource.ProductResource;
import com.vendido.vendido.service.mapper.ProductMapper;

@Service
public class ProductService implements BaseService<ProductDTO, ProductResource> {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CacheManager cacheManager;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public ProductResource findAll(final Pageable pageable) {
		final Page<ProductDTO> page = this.productRepository.findAllByDeleted(false, pageable)//
				.map(this.productMapper::toDTO);
		final ProductResource res = new ProductResource();
		res.setList(page.getContent());
		for(ProductDTO p : res.getList()) {
			 cacheManager.getCache("products").put(p.getId(), p);
		}
		return res;
	}

	@Override
	@Cacheable(cacheNames = "products", key = "#id")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public ProductDTO findById(final long id) throws Exception {
		return this.productRepository.findByIdAndDeleted(id, false)//
				.map(this.productMapper::toDTO)//
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ProductDTO save(final ProductDTO dto) throws Exception {
		final CategoryDTO categoryDTO = this.categoryService.findById(dto.getCategory().getId());
		dto.setCategory(categoryDTO);

		ProductEntity entity = this.productMapper.toEntity(dto);
		entity.setCreated_at(new Date());
		entity.setDeleted(false);
		return this.productMapper.toDTO(this.productRepository.save(entity));
	}

	@Override
	@CachePut(cacheNames = "products", key = "#id")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ProductDTO update(final long id, final ProductDTO dto) throws Exception {
		ProductEntity entity = this.productRepository.findByIdAndDeleted(id, false)//
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

		final CategoryDTO categoryDTO = this.categoryService.findById(dto.getCategory().getId());
		dto.setCategory(categoryDTO);

		this.productMapper.toUpdateEntity(entity, dto);
		return this.productMapper.toDTO(this.productRepository.save(entity));
	}

	@Override
	@CacheEvict(cacheNames = "products", key = "#id")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(final long id) throws Exception {
		ProductEntity entity = this.productRepository.findByIdAndDeleted(id, false)//
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

		entity.setDeleted(true);
		this.productRepository.save(entity);

	}

}
