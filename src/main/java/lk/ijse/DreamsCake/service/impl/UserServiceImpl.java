package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.UserDTO;
import lk.ijse.DreamsCake.entity.User;
import lk.ijse.DreamsCake.exception.CustomerException;
import lk.ijse.DreamsCake.repository.UserRepo;
import lk.ijse.DreamsCake.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j // Lombok haraha Logback log object eka automatically enwa
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDTO getUserDetails(String username, String password) {
        log.info("Attempting login for user: {}", username);

        Optional<User> optionalUser = userRepo.findByUserNameAndPassword(username, password);
        if(optionalUser.isEmpty()) {
            log.warn("Login failed: User not found with username: {}", username);
            throw new CustomerException(404, "User not found");
        }

        User user = optionalUser.get();
        log.info("User successfully logged in: {}", username);
        return new UserDTO(user.getUserId(), user.getUserName(), user.getUserRoles(), user.getPassword());
    }

    @Override
    public void saveUser(UserDTO userDTO) {
        log.info("Attempting to save new user: {}", userDTO.getUserName());

        if(userDTO.getUserRoles() == null || userDTO.getUserRoles().trim().isEmpty()) {
            log.error("Failed to save user: User Role is empty");
            throw new CustomerException(404, "User Role cannot be empty");
        }

        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(userDTO.getPassword());
        user.setUserRoles(userDTO.getUserRoles());

        userRepo.save(user);
        log.info("User saved successfully: {}", userDTO.getUserName());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users from database");
        return userRepo.getAllUsers();
    }

    @Override
    public List<UserDTO> filterUsers(String username) {
        log.info("Filtering users with username pattern: {}", username);
        return userRepo.filterUser(username);
    }

    @Override
    public UserDTO selectUser(long userId) {
        log.info("Fetching user details by ID: {}", userId);
        return userRepo.selectUser(userId);
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        log.info("Attempting to update user with ID: {}", userDTO.getUserId());

        Optional<User> optionalUser = userRepo.findById(userDTO.getUserId());
        if(optionalUser.isEmpty()) {
            log.error("Update failed: No user found with ID: {}", userDTO.getUserId());
            throw new RuntimeException("Sorry no user");
        }

        User user = optionalUser.get();
        user.setUserName(userDTO.getUserName());
        user.setUserRoles(userDTO.getUserRoles());

        userRepo.save(user);
        log.info("User updated successfully with ID: {}", userDTO.getUserId());
    }
}