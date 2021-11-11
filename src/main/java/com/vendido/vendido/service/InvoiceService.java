package com.vendido.vendido.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendido.vendido.dto.InvoiceDTO;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.InvoiceRepository;
import com.vendido.vendido.service.mapper.InvoiceMapper;
import com.vendido.vendido.service.mapper.UserMapper;

@Service
public class InvoiceService implements BaseService<InvoiceDTO> {
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private InvoiceMapper invoiceMapper;
	
	@Autowired
	private InvoiceItemService invoiceItemService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMapper userMapper;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Page<InvoiceDTO> findAll(final Pageable pageable) {
		return this.invoiceRepository.findAllByDeleted(false, pageable)//
				.map(this.invoiceMapper::toDTO);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public InvoiceDTO findById(final long id) throws Exception {
		final InvoiceDTO dto = this.invoiceRepository.findByIdAndDeleted(id, false)//
				.map(this.invoiceMapper::toDTO)//
				.orElseThrow(() -> new ResourceNotFoundException("Factura", "id", id));
		dto.setItems(this.invoiceItemService.findByInvoiceId(id));
		return dto;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public InvoiceDTO save(final InvoiceDTO dto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public InvoiceDTO update(final long id, final InvoiceDTO dto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(final long id) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
