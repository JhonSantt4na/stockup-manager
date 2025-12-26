package com.stockup.StockUp.Manager.service.auth;

import com.stockup.StockUp.Manager.dto.Auth.user.request.ChangePasswordRequestDTO;
import com.stockup.StockUp.Manager.dto.Auth.user.request.RegisterUserRequestDTO;
import com.stockup.StockUp.Manager.dto.Auth.user.request.UpdateUserRequestDTO;
import com.stockup.StockUp.Manager.dto.Auth.user.response.RegistrationResponseDTO;
import com.stockup.StockUp.Manager.dto.Auth.user.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import java.util.List;

public interface IUserService {
	UserResponseDTO assignRoles(String username, List<String> roleNames);
	UserResponseDTO removeRoles(String username, List<String> roleNames);
	List<String> getUserRoles(String username);
	List<String> getAllSystemRoles();
	Page<UserResponseDTO> listUsers(int page, int size, String search, Boolean enabled, String[] sort);
	Page<UserResponseDTO> listUsersWithFilter(int page, int size, String search, String filter);
	UserResponseDTO findUser(String username);
	UserResponseDTO updatedUser(String username, UpdateUserRequestDTO dto);
	UserResponseDTO updateUserAsAdmin(String username, UpdateUserRequestDTO dto);
	void deleteUser(String username);
	UserResponseDTO toggleUserStatus(String username);
	RegistrationResponseDTO registerUser(RegisterUserRequestDTO credentials);
	void changePassword(String username, ChangePasswordRequestDTO dto);
}
