package com.vendido.vendido.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendido.vendido.dto.InvoiceItemDTO;
import com.vendido.vendido.dto.OrderItemDTO;
import com.vendido.vendido.dto.ProductDTO;
import com.vendido.vendido.entity.InvoiceItemEntity;
import com.vendido.vendido.entity.OrderEntity;
import com.vendido.vendido.entity.OrderItemEntity;
import com.vendido.vendido.entity.ProductEntity;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.InvoiceItemRepository;
import com.vendido.vendido.service.mapper.InvoiceItemMapper;
import com.vendido.vendido.service.mapper.ProductMapper;

@Service
public class InvoiceItemService implements BaseService<InvoiceItemDTO>{
	
	@Autowired
	private InvoiceItemRepository invoiceRepository;
	
	@Autowired
	private InvoiceItemMapper invoiceItemMapper;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductMapper productMapper;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Page<InvoiceItemDTO> findAll(final Pageable pageable) {
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
		List<InvoiceItemEntity> entities = this.invoiceRepository.findByInvoiceId(id);
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
		
		InvoiceItemEntity entity = this.invoiceItemMapper.toEntity(productDTO, dto.getProductQuantity());

		return null;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public InvoiceItemDTO update(final long id, final InvoiceItemDTO dto) throws Exception {
		//Se busca el item
		InvoiceItemEntity entity = this.invoiceRepository.findById(id)//
						.orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
				
		//Busca el producto
		ProductDTO productDTO = this.productService.findById(dto.getProductId());
		return null;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(final long id) throws Exception {
		//Se busca el item
		InvoiceItemEntity entity = this.invoiceRepository.findById(id)//
				.orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
		this.invoiceRepository.delete(entity);		
	}

}
