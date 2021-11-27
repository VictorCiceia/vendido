package com.vendido.vendido.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vendido.vendido.dto.RoleDTO;
import com.vendido.vendido.dto.UserDTO;
import com.vendido.vendido.entity.UserDetailEntity;
import com.vendido.vendido.entity.UserEntity;
import com.vendido.vendido.exception.FaildEmailException;
import com.vendido.vendido.exception.FaildPasswordException;
import com.vendido.vendido.exception.ResourceNotFoundException;
import com.vendido.vendido.repository.UserDetailRepository;
import com.vendido.vendido.repository.UserRepository;
import com.vendido.vendido.resource.UserResource;
import com.vendido.vendido.service.mapper.UserDetailMapper;
import com.vendido.vendido.service.mapper.UserMapper;

@Service
public class UserService implements BaseService<UserDTO, UserResource> {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserDetailRepository userDetailRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@Autowired
	private UserDetailMapper userDetailMapper;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private CacheManager cacheManager;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public UserResource findAll(final Pageable pageable) {
		final Page<UserDTO> page =  this.userRepository.findAllByEnabled(true, pageable)//
				.map(this.userMapper::toDetailDTO);
		final UserResource res = new UserResource();
		res.setList(page.getContent());
		for(UserDTO u: res.getList()) {
			cacheManager.getCache("users").put(u.getId(), u);
		}
		return res;
	}

	@Override
	@Cacheable(cacheNames = "users", key = "#id")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public UserDTO findById(final long id) throws Exception {
		final UserEntity entity = this.userRepository.findByIdAndEnabled(id, true)//
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		return this.userMapper.toDetailDTO(entity);
	}
	
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public UserResource searchByName(final String name, final Pageable pageable) throws Exception {
		final Page<UserDTO> page = this.userRepository.searchByNameAndEnabledStartsWith(name, true, pageable)//
				.map(this.userMapper::toDetailDTO);
		final UserResource res = new UserResource();
		res.setList(page.getContent());
		for(UserDTO u: res.getList()) {
			cacheManager.getCache("users").put(u.getId(), u);
		}
		return res;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public UserDTO save(final UserDTO dto) throws Exception {
		// Validadciones
		final RoleDTO roleDTO = roleService.findById(dto.getRoles().get(0).getId());
		dto.getRoles().get(0).setAuthority(roleDTO.getAuthority());

		// Guardar el usuario
		final UserEntity userEntity = this.userMapper.toEntity(dto);
		userEntity.setCreatedAt(new Date());
		userEntity.setEnabled(true);
		userEntity.setPassword(this.passwordEncode.encode(dto.getNewPassword()));
		final UserEntity entity = this.userRepository.save(userEntity);

		// Guardar el detalle
		dto.setId(entity.getId());
		UserDetailEntity userDetailEntity = this.userDetailRepository.save(this.userDetailMapper.toEntity(dto));
		userEntity.setDetail(userDetailEntity);

		// Convierte a DTO
		return this.userMapper.toDetailDTO(entity);
	}

	@Override
	@CachePut(cacheNames = "users", key = "#id")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public UserDTO update(final long id, final UserDTO dto) throws Exception {

		// Validad que exista el usuario
		UserEntity userEntity = this.userRepository.findByIdAndEnabled(id, true)//
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		
		// Cambio de contrasenha
		if (null != dto.getNewPassword())
			userEntity.setPassword(validPassword(userEntity.getPassword(), dto.getNewPassword(), dto.getOldPassword()));
		
		//Cambio de email
		if(null != dto.getEmail())
			userEntity.setEmail(validEmail(userEntity.getEmail(), dto.getEmail()));
			
		//Guardamos la cabecera
		this.userMapper.toUpdateEntity(userEntity, dto);
		final UserEntity entity = this.userRepository.save(userEntity);
		
		// Convierte a DTO
		return this.userMapper.toDetailDTO(entity);
	}

	@Override
	@CacheEvict(cacheNames = "users", key = "#id")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(final long id) throws Exception {
		UserEntity userEntity = this.userRepository.findByIdAndEnabled(id, true)//
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		userEntity.setEnabled(false);
		this.userRepository.save(userEntity);
	}

	private String validPassword(final String entityPass, final String newPass, final String oldPass)
			throws FaildPasswordException {
		
		if (!this.passwordEncode.matches(oldPass, entityPass)) {
			throw new FaildPasswordException("La contraseña antigua no es igual");
		}
		if (this.passwordEncode.matches(newPass, entityPass)) {
			throw new FaildPasswordException("La contraseña debe ser distinta a la antigua");
		}

		return this.passwordEncode.encode(newPass);
	}
	
	private String validEmail(final String emailEntity, final String emailDto) {
		if(emailEntity.equals(emailDto))
			return emailEntity;
		final Optional<UserEntity> userEntity = this.userRepository.findByEmailAndEnabled(emailDto, true);
		if(userEntity.isPresent())
			throw new FaildEmailException("Ya es utilizado el email por otro usuario"); 
		return emailDto;
	}

}
