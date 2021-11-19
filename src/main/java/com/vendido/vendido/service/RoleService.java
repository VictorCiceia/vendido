package com.vendido.vendido.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendido.vendido.dto.RoleDTO;
import com.vendido.vendido.entity.RoleEntity;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.RoleRepository;
import com.vendido.vendido.resource.RoleResource;
import com.vendido.vendido.service.mapper.RoleMapper;

@Service
public class RoleService implements BaseService<RoleDTO, RoleResource> {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RoleMapper roleMapper;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public RoleResource findAll(final Pageable pageable) {
		final Page<RoleDTO> page = this.roleRepository.findAll(pageable)//
				.map(this.roleMapper::toDTO);
		final RoleResource res = new RoleResource();
		res.setList(page.getContent());
		return res;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public RoleDTO findById(final long id) throws Exception {
		return this.roleRepository.findById(id)//
				.map(this.roleMapper::toDTO)//
				.orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public RoleDTO save(final RoleDTO dto) throws Exception {
		final RoleEntity entity = this.roleMapper.toEntity(dto);
		return this.roleMapper.toDTO(this.roleRepository.save(entity));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public RoleDTO update(final long id, final RoleDTO dto) throws Exception {
		RoleEntity entity = this.roleRepository.findById(id)//
				.orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
		entity.setAuthority(dto.getAuthority());
		return this.roleMapper.toDTO(this.roleRepository.save(entity));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(final long id) throws Exception {
		if (!this.roleRepository.existsById(id)) {
			throw new ResourceNotFoundException("Role", "id", id);
		}
		this.roleRepository.deleteById(id);
	}

}
