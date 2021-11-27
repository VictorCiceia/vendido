package com.vendido.vendido.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendido.vendido.dto.PayOrderItemDTO;
import com.vendido.vendido.dto.ProductDTO;
import com.vendido.vendido.entity.PayOrderEntity;
import com.vendido.vendido.entity.PayOrderItemEntity;
import com.vendido.vendido.entity.ProductEntity;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.PayOrderItemRepository;
import com.vendido.vendido.resource.PayOrderItemResource;
import com.vendido.vendido.service.mapper.PayOrderItemMapper;
import com.vendido.vendido.service.mapper.ProductMapper;

@Service
public class PayOrderItemService implements BaseService<PayOrderItemDTO, PayOrderItemResource>{
	
	@Autowired
	private PayOrderItemRepository payOrderItemRepository;
	
	@Autowired
	private PayOrderItemMapper payOrderItemMapper;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductMapper productMapper;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PayOrderItemResource findAll(final Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PayOrderItemDTO findById(final long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<PayOrderItemDTO> findByPayOrderId(final long id) throws Exception {
		List<PayOrderItemEntity> entities = this.payOrderItemRepository.findByPayOrderId(id);
		if(!entities.isEmpty()) {
			return entities.stream().map(this.payOrderItemMapper::toDTO).collect(Collectors.toList());
		}else {
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PayOrderItemDTO save(final PayOrderItemDTO dto) throws Exception {
		//Busca el producto
		ProductDTO productDTO = this.productService.findById(dto.getProductId());
		ProductEntity productEntity = this.productMapper.toEntity(productDTO);
		productEntity.setId(productDTO.getId());
		
		PayOrderEntity payOrderEntity = new PayOrderEntity();
		payOrderEntity.setId(dto.getPayOrderId());
		
		PayOrderItemEntity entity = this.payOrderItemMapper.toEntity(productDTO, dto.getProductQuantity());
		entity.setProduct(productEntity);
		entity.setPayOrder(payOrderEntity);

		return this.payOrderItemMapper.toDTO(this.payOrderItemRepository.save(entity));
	}

	@Override
	@CachePut(cacheNames = "payordeitems", key = "#id")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PayOrderItemDTO update(final long id, final PayOrderItemDTO dto) throws Exception {
		//Se busca el item
		PayOrderItemEntity entity = this.payOrderItemRepository.findById(id)//
			.orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
						
		//Busca el producto
		ProductDTO productDTO = this.productService.findById(dto.getProductId());
		ProductEntity productEntity = this.productMapper.toEntity(productDTO);
		productEntity.setId(productDTO.getId());
		
		PayOrderEntity payOrderEntity = new PayOrderEntity();
		payOrderEntity.setId(dto.getPayOrderId());
		
		entity.setProduct(productEntity);
		entity.setPayOrder(payOrderEntity);
		this.payOrderItemMapper.toUpdateEntiry(entity, dto.getProductQuantity());
		
		return this.payOrderItemMapper.toDTO(this.payOrderItemRepository.save(entity));
	}

	@Override
	@CacheEvict(cacheNames = "payordeitems", key = "#id")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public void delete(final long id) throws Exception {
		PayOrderItemEntity entity = this.payOrderItemRepository.findById(id)//
				.orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
		this.payOrderItemRepository.delete(entity);			
	}

}
