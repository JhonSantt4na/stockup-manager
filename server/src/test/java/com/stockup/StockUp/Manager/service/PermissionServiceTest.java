package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.dto.Auth.permission.request.PermissionCreateDTO;
import com.stockup.StockUp.Manager.dto.Auth.permission.request.PermissionUpdateDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.repository.auth.PermissionRepository;
import com.stockup.StockUp.Manager.service.auth.impl.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {
	
	@Mock
	private PermissionRepository permissionRepository;
	
	@InjectMocks
	private PermissionService permissionService;
	
	private Permission testPermission;
	private PermissionCreateDTO createDTO;
	private PermissionUpdateDTO updateDTO;
	
	@BeforeEach
	void setUp() {
		UUID testUuid = UUID.randomUUID();
		testPermission = new Permission();
		testPermission.setId(testUuid);
		testPermission.setDescription("READ_USER");
		testPermission.setCreatedAt(LocalDateTime.now());
		
		createDTO = new PermissionCreateDTO();
		createDTO.setDescription("READ_USER");
		
		updateDTO = new PermissionUpdateDTO();
		updateDTO.setOldDescription("READ_USER");
		updateDTO.setNewDescription("READ_USER_UPDATED");
	}
	
	@Test
	void createPermission_WhenDescriptionNotExists_ShouldCreateAndReturnPermission() {
		// Arrange
		when(permissionRepository.findByDescription("READ_USER")).thenReturn(Optional.empty());
		when(permissionRepository.save(any(Permission.class))).thenReturn(testPermission);
		
		// Act
		Permission result = permissionService.createPermission(createDTO);
		
		// Assert
		assertEquals("READ_USER", result.getDescription());
		assertNotNull(result.getCreatedAt());
		verify(permissionRepository, times(1)).save(any(Permission.class));
	}
	
	@Test
	void createPermission_WhenDescriptionExists_ShouldThrowDuplicateResourceException() {
		// Arrange
		when(permissionRepository.findByDescription("READ_USER")).thenReturn(Optional.of(testPermission));
		
		// Act & Assert
		DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
			() -> permissionService.createPermission(createDTO));
		assertTrue(exception.getMessage().contains("already exists"));
		verify(permissionRepository, never()).save(any(Permission.class));
	}
	
	@Test
	void updatePermission_WhenValidUpdate_ShouldUpdateAndReturnPermission() {
		// Arrange
		when(permissionRepository.findByDescription("READ_USER")).thenReturn(Optional.of(testPermission));
		when(permissionRepository.findByDescription("READ_USER_UPDATED")).thenReturn(Optional.empty());
		when(permissionRepository.save(testPermission)).thenReturn(testPermission);
		
		// Act
		Permission result = permissionService.updatePermission(updateDTO);
		
		// Assert
		assertEquals("READ_USER_UPDATED", result.getDescription());
		assertNotNull(result.getUpdatedAt());
		verify(permissionRepository, times(1)).save(testPermission);
	}
	
	@Test
	void updatePermission_WhenNewDescriptionExists_ShouldThrowDuplicateResourceException() {
		// Arrange
		Permission existingNew = new Permission();
		existingNew.setDescription("READ_USER_UPDATED");
		when(permissionRepository.findByDescription("READ_USER")).thenReturn(Optional.of(testPermission));
		when(permissionRepository.findByDescription("READ_USER_UPDATED")).thenReturn(Optional.of(existingNew));
		
		// Act & Assert
		DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
			() -> permissionService.updatePermission(updateDTO));
		assertTrue(exception.getMessage().contains("already exists"));
		verify(permissionRepository, never()).save(any(Permission.class));
	}
	
	@Test
	void updatePermission_WhenSameDescription_ShouldUpdateAndReturnPermission() {
		// Arrange
		PermissionUpdateDTO sameDTO = new PermissionUpdateDTO();
		sameDTO.setOldDescription("READ_USER");
		sameDTO.setNewDescription("READ_USER");
		when(permissionRepository.findByDescription("READ_USER")).thenReturn(Optional.of(testPermission));
		when(permissionRepository.save(testPermission)).thenReturn(testPermission);
		
		// Act
		Permission result = permissionService.updatePermission(sameDTO);
		
		// Assert
		assertEquals("READ_USER", result.getDescription());
		assertNotNull(result.getUpdatedAt());
		verify(permissionRepository, times(1)).save(testPermission);
	}
	
	@Test
	void deletePermission_WhenPermissionExists_ShouldSoftDelete() {
		// Arrange
		when(permissionRepository.findByDescription("READ_USER")).thenReturn(Optional.of(testPermission));
		when(permissionRepository.save(testPermission)).thenReturn(testPermission);
		
		// Act
		permissionService.deletePermission("READ_USER");
		
		// Assert
		assertNotNull(testPermission.getDeletedAt());
		assertFalse(testPermission.isEnabled());
		verify(permissionRepository, times(1)).save(testPermission);
	}
	
//	@Test
//	void getAllPermission_ShouldReturnPagedPermissions() {
//		// Arrange
//		Pageable pageable = PageRequest.of(0, 10);
//		Page<Permission> permissionPage = new PageImpl<>(List.of(testPermission));
//		when(permissionRepository.findAll(pageable)).thenReturn(permissionPage);
//
//		// Act
//		Page<Permission> result = permissionService.getAllPermission(pageable);
//
//		// Assert
//		assertEquals(1, result.getNumberOfElements());
//		assertEquals(testPermission, result.getContent().getFirst());
//		verify(permissionRepository, times(1)).findAll(pageable);
//	}
	
	@Test
	void getPermissionByDescription_WhenExists_ShouldReturnPermission() {
		// Arrange
		when(permissionRepository.findByDescription("READ_USER")).thenReturn(Optional.of(testPermission));
		
		// Act
		Permission result = permissionService.getPermissionByDescription("READ_USER");
		
		// Assert
		assertEquals(testPermission, result);
		verify(permissionRepository, times(1)).findByDescription("READ_USER");
	}
	
	@Test
	void getPermissionByDescription_WhenNotExists_ShouldThrowIllegalArgumentException() {
		// Arrange
		when(permissionRepository.findByDescription("NON_EXISTENT")).thenReturn(Optional.empty());
		
		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> permissionService.getPermissionByDescription("NON_EXISTENT"));
		assertTrue(exception.getMessage().contains("not found"));
		verify(permissionRepository, times(1)).findByDescription("NON_EXISTENT");
	}
}