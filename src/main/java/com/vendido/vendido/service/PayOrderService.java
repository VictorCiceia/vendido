package com.vendido.vendido.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendido.vendido.dto.PayOrderDTO;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.PayOrderRepository;
import com.vendido.vendido.service.mapper.PayOrderMapper;
import com.vendido.vendido.service.mapper.UserMapper;

@Service
public class PayOrderService implements BaseService<PayOrderDTO>{
	
	@Autowired
	private PayOrderRepository payOrderRepository;
	
	@Autowired
	private PayOrderMapper payOrderMapper;
	
	@Autowired
	private PayOrderItemService payOrderItemService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMapper userMapper;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Page<PayOrderDTO> findAll(final Pageable pageable) {
		return this.payOrderRepository.findAllByDeleted(false, pageable)//
				.map(this.payOrderMapper::toDTO);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PayOrderDTO findById(final long id) throws Exception {
		final PayOrderDTO dto = this.payOrderRepository.findByIdAndDeleted(id, false)//
				.map(this.payOrderMapper::toDTO)//
				.orElseThrow(() -> new ResourceNotFoundException("Factura", "id", id));
		dto.setItems(this.payOrderItemService.findByPayOrderId(id));
		return dto;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public PayOrderDTO save(final PayOrderDTO dto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public PayOrderDTO update(final long id, PayOrderDTO dto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(final long id) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
