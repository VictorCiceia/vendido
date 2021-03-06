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

import com.vendido.vendido.dto.InvoiceItemDTO;
import com.vendido.vendido.dto.ProductDTO;
import com.vendido.vendido.entity.InvoiceEntity;
import com.vendido.vendido.entity.InvoiceItemEntity;
import com.vendido.vendido.entity.ProductEntity;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.InvoiceItemRepository;
import com.vendido.vendido.resource.InvoiceItemResource;
import com.vendido.vendido.service.mapper.InvoiceItemMapper;
import com.vendido.vendido.service.mapper.ProductMapper;

@Service
public class InvoiceItemService implements BaseService<InvoiceItemDTO, InvoiceItemResource>{
	
	@Autowired
	private InvoiceItemRepository invoiceItemRepository;
	
	@Autowired
	private InvoiceItemMapper invoiceItemMapper;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductMapper productMapper;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public InvoiceItemResource findAll(final Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public InvoiceItemDTO findById(final long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<InvoiceItemDTO> findByInvoiceId(final long id) throws Exception {
		List<InvoiceItemEntity> entities = this.invoiceItemRepository.findByInvoiceId(id);
		if(!entities.isEmpty()) {
			return entities.stream().map(this.invoiceItemMapper::toDTO).collect(Collectors.toList());
		}else {
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public InvoiceItemDTO save(final InvoiceItemDTO dto) throws Exception {
		//Busca el producto
		ProductDTO productDTO = this.productService.findById(dto.getProductId());
		ProductEntity productEntity = this.productMapper.toEntity(productDTO);
		productEntity.setId(productDTO.getId());
		
		InvoiceEntity invoiceEntity = new InvoiceEntity();
		invoiceEntity.setId(dto.getInvoiceId());
		
		InvoiceItemEntity entity = this.invoiceItemMapper.toEntity(productDTO, dto.getProductQuantity());
		entity.setTaxTotal((int)((double)(entity.getProductPriceUnit() * dto.getProductQuantity()) * ((double)((double)entity.getProductTaxPercentage())/100)));
		entity.setTotal(entity.getProductPriceUnit() * dto.getProductQuantity());
		entity.setProduct(productEntity);
		entity.setInvoice(invoiceEntity);

		return this.invoiceItemMapper.toDTO(this.invoiceItemRepository.save(entity));
	}

	@Override
	@CachePut(cacheNames = "categories", key = "#id")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public InvoiceItemDTO update(final long id, final InvoiceItemDTO dto) throws Exception {
		//Se busca el item
		InvoiceItemEntity entity = this.invoiceItemRepository.findById(id)//
						.orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
				
		//Busca el producto
		ProductDTO productDTO = this.productService.findById(dto.getProductId());
		ProductEntity productEntity = this.productMapper.toEntity(productDTO);
		productEntity.setId(productDTO.getId());
		
		InvoiceEntity invoiceEntity = new InvoiceEntity();
		invoiceEntity.setId(dto.getInvoiceId());
		
		entity.setInvoice(invoiceEntity);
		entity.setProduct(productEntity);		
		entity.setTaxTotal((int)((double)(productEntity.getPrice() * dto.getProductQuantity()) * ((double)((double)productEntity.getTax_percentage())/100)));
		entity.setTotal(productEntity.getPrice() * dto.getProductQuantity());
		
		this.invoiceItemMapper.updateEntity(entity, dto.getProductQuantity());
		return this.invoiceItemMapper.toDTO(this.invoiceItemRepository.save(entity));
	}

	@Override
	@CacheEvict(cacheNames = "categories", key = "#id")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(final long id) throws Exception {
		//Se busca el item
		InvoiceItemEntity entity = this.invoiceItemRepository.findById(id)//
				.orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
		this.invoiceItemRepository.delete(entity);		
	}

}
