package com.stockup.StockUp.Manager.service;

import com.stockup.StockUp.Manager.dto.Auth.roles.request.RoleDTO;
import com.stockup.StockUp.Manager.dto.Auth.roles.request.RoleUpdateDTO;
import com.stockup.StockUp.Manager.model.user.Permission;
import com.stockup.StockUp.Manager.model.user.Role;
import com.stockup.StockUp.Manager.repository.user.PermissionRepository;
import com.stockup.StockUp.Manager.repository.user.RoleRepository;
import com.stockup.StockUp.Manager.service.auth.impl.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
	
	@Mock
	private RoleRepository roleRepository;
	
	@Mock
	private PermissionRepository permissionRepository;
	
	@InjectMocks
	private RoleService roleService;
	
	private Role testRole;
	private RoleDTO createDTO;
	private RoleUpdateDTO updateDTO;
	private UUID testUuid;
	private Permission testPermission;
	
	@BeforeEach
	void setUp() {
		testUuid = UUID.randomUUID();
		testRole = new Role();
		testRole.setId(testUuid);
		testRole.setName("TEST_ROLE");
		testRole.setCreatedAt(LocalDateTime.now());
		testRole.setPermissions(new ArrayList<>());
		
		createDTO = new RoleDTO();
		createDTO.setName("TEST_ROLE");
		
		updateDTO = new RoleUpdateDTO();
		updateDTO.setOldName("TEST_ROLE");
		updateDTO.setNewName("TEST_ROLE_UPDATED");
		
		testPermission = new Permission();
		testPermission.setDescription("READ_USER");
	}
	
	@Test
	void ensureBuiltInRoles_WhenRolesDoNotExist_ShouldCreateThem() {
		// Arrange
		doReturn(Optional.empty()).when(roleRepository).findByName(eq("ADMIN"));
		doReturn(Optional.empty()).when(roleRepository).findByName(eq("USER"));
		Role adminRole = new Role("ADMIN");
		adminRole.setCreatedAt(LocalDateTime.now());
		Role userRole = new Role("USER");
		userRole.setCreatedAt(LocalDateTime.now());
		doReturn(adminRole).when(roleRepository).save(argThat(r -> r.getName().equals("ADMIN")));
		doReturn(userRole).when(roleRepository).save(argThat(r -> r.getName().equals("USER")));
		
		// Act
		roleService.ensureBuiltInRoles();
		
		// Assert
		verify(roleRepository).findByName(eq("ADMIN"));
		verify(roleRepository).findByName(eq("USER"));
		verify(roleRepository).save(argThat(r -> r.getName().equals("ADMIN")));
		verify(roleRepository).save(argThat(r -> r.getName().equals("USER")));
	}
	
	@Test
	void createRole_WhenNameNotExists_ShouldCreateAndReturnRole() {
		// Arrange
		when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.empty());
		when(roleRepository.save(any(Role.class))).thenReturn(testRole);
		
		// Act
		Role result = roleService.createRole(createDTO);
		
		// Assert
		assertEquals("TEST_ROLE", result.getName());
		assertNotNull(result.getCreatedAt());
		verify(roleRepository, times(1)).save(any(Role.class));
	}
	
	@Test
	void createRole_WhenNameExists_ShouldThrowIllegalArgumentException() {
		// Arrange
		when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.of(testRole));
		
		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> roleService.createRole(createDTO));
		assertTrue(exception.getMessage().contains("already exists"));
		verify(roleRepository, never()).save(any(Role.class));
	}
	
	@Test
	void updateRole_WhenValidUpdate_ShouldUpdateAndReturnRole() {
		// Arrange
		when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.of(testRole));
		when(roleRepository.findByName("TEST_ROLE_UPDATED")).thenReturn(Optional.empty());
		when(roleRepository.save(testRole)).thenReturn(testRole);
		
		// Act
		Role result = roleService.updateRole(updateDTO);
		
		// Assert
		assertEquals("TEST_ROLE_UPDATED", result.getName());
		assertNotNull(result.getUpdatedAt());
		verify(roleRepository, times(1)).save(testRole);
	}
	
	@Test
	void updateRole_WhenNewNameExists_ShouldThrowIllegalArgumentException() {
		// Arrange
		Role existingNew = new Role("TEST_ROLE_UPDATED");
		when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.of(testRole));
		when(roleRepository.findByName("TEST_ROLE_UPDATED")).thenReturn(Optional.of(existingNew));
		
		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> roleService.updateRole(updateDTO));
		assertTrue(exception.getMessage().contains("already exists"));
		verify(roleRepository, never()).save(any(Role.class));
	}
	
	@Test
	void updateRole_WhenSameName_ShouldUpdateAndReturnRole() {
		// Arrange
		RoleUpdateDTO sameDTO = new RoleUpdateDTO();
		sameDTO.setOldName("TEST_ROLE");
		sameDTO.setNewName("TEST_ROLE");
		when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.of(testRole));
		when(roleRepository.save(testRole)).thenReturn(testRole);
		
		// Act
		Role result = roleService.updateRole(sameDTO);
		
		// Assert
		assertEquals("TEST_ROLE", result.getName());
		assertNotNull(result.getUpdatedAt());
		verify(roleRepository, times(1)).save(testRole);
	}
	
	@Test
	void getRoleById_WhenExists_ShouldReturnRole() {
		// Arrange
		when(roleRepository.findById(testUuid)).thenReturn(Optional.of(testRole));
		
		// Act
		Role result = roleService.getRoleById(testUuid);
		
		// Assert
		assertEquals(testRole, result);
		verify(roleRepository, times(1)).findById(testUuid);
	}
	
	@Test
	void getRoleById_WhenNotExists_ShouldThrowIllegalArgumentException() {
		// Arrange
		when(roleRepository.findById(testUuid)).thenReturn(Optional.empty());
		
		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> roleService.getRoleById(testUuid));
		assertTrue(exception.getMessage().contains("not found"));
		verify(roleRepository, times(1)).findById(testUuid);
	}
	
	@Test
	void getRoleByName_WhenExists_ShouldReturnRole() {
		// Arrange
		when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.of(testRole));
		
		// Act
		Role result = roleService.getRoleByName("TEST_ROLE");
		
		// Assert
		assertEquals(testRole, result);
		verify(roleRepository, times(1)).findByName("TEST_ROLE");
	}
	
	@Test
	void getRoleByName_WhenNotExists_ShouldThrowIllegalArgumentException() {
		// Arrange
		when(roleRepository.findByName("NON_EXISTENT")).thenReturn(Optional.empty());
		
		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> roleService.getRoleByName("NON_EXISTENT"));
		assertTrue(exception.getMessage().contains("not found"));
		verify(roleRepository, times(1)).findByName("NON_EXISTENT");
	}
	
	@Test
	void deleteRole_WhenRoleExists_ShouldSoftDelete() {
		// Arrange
		when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.of(testRole));
		when(roleRepository.save(testRole)).thenReturn(testRole);
		
		// Act
		roleService.deleteRole("TEST_ROLE");
		
		// Assert
		assertNotNull(testRole.getDeletedAt());
		assertFalse(testRole.isEnabled());
		verify(roleRepository, times(1)).save(testRole);
	}
	
	@Test
	void getAllRoles_ShouldReturnPagedRoles() {
		// Arrange
		Pageable pageable = PageRequest.of(0, 10);
		Page<Role> rolePage = new PageImpl<>(List.of(testRole));
		when(roleRepository.findAll(pageable)).thenReturn(rolePage);
		
		// Act
		Page<Role> result = roleService.getAllRoles(pageable);
		
		// Assert
		assertEquals(1, result.getNumberOfElements());
		assertEquals(testRole, result.getContent().getFirst());
		verify(roleRepository, times(1)).findAll(pageable);
	}
	
	@Test
	void assignPermissions_WhenValidPermissions_ShouldAssignAndReturnRole() {
		// Arrange
		List<String> permDescs = List.of("READ_USER");
		when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.of(testRole));
		when(permissionRepository.findAllByDescriptionIn(permDescs)).thenReturn(List.of(testPermission));
		when(roleRepository.save(testRole)).thenReturn(testRole);
		
		// Act
		Role result = roleService.assignPermissions("TEST_ROLE", permDescs);
		
		// Assert
		assertEquals(1, result.getPermissions().size());
		verify(roleRepository, times(1)).save(testRole);
		verify(permissionRepository, times(1)).findAllByDescriptionIn(permDescs);
	}
	
	@Test
	void assignPermissions_WhenNoValidPermissions_ShouldThrowIllegalArgumentException() {
		// Arrange
		List<String> permDescs = List.of("INVALID");
		when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.of(testRole));
		when(permissionRepository.findAllByDescriptionIn(permDescs)).thenReturn(List.of());
		
		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> roleService.assignPermissions("TEST_ROLE", permDescs));
		assertTrue(exception.getMessage().contains("No valid permissions"));
		verify(roleRepository, never()).save(any(Role.class));
	}
	
	@Test
	void removePermissions_WhenValid_ShouldRemoveAndReturnRole() {
		// Arrange
		List<String> permDescs = List.of("READ_USER");
		testRole.getPermissions().add(testPermission);
		when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.of(testRole));
		when(roleRepository.save(testRole)).thenReturn(testRole);
		
		// Act
		Role result = roleService.removePermissions("TEST_ROLE", permDescs);
		
		// Assert
		assertTrue(result.getPermissions().isEmpty());
		verify(roleRepository, times(1)).save(testRole);
	}
	
	@Test
	void getRolePermissions_WhenRoleExists_ShouldReturnPermissionDescriptions() {
		// Arrange
		testRole.getPermissions().add(testPermission);
		when(roleRepository.findByName("TEST_ROLE")).thenReturn(Optional.of(testRole));
		
		// Act
		List<String> result = roleService.getRolePermissions("TEST_ROLE");
		
		// Assert
		assertEquals(1, result.size());
		assertEquals("READ_USER", result.getFirst());
		verify(roleRepository, times(1)).findByName("TEST_ROLE");
	}
}