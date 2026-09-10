package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.SignupDTO;
import lk.ijse.DreamsCake.dto.UserDTO;
import lk.ijse.DreamsCake.entity.Customer;
import lk.ijse.DreamsCake.entity.User;
import lk.ijse.DreamsCake.exception.ApiException;
import lk.ijse.DreamsCake.repository.CustomerRepo; // 👈 මේක නැවත එකතු කරන්න
import lk.ijse.DreamsCake.repository.UserRepo;
import lk.ijse.DreamsCake.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, CustomerRepo customerRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO getUserDetails(String username, String password) {
        log.info("Attempting login for user: {}", username);

        Optional<User> optionalUser = userRepo.findByUserName(username);
        if (optionalUser.isEmpty()) {
            log.warn("Login failed: User not found with username: {}", username);
            throw new ApiException(404, "User not found");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Login failed: Invalid password for username: {}", username);
            throw new ApiException(401, "Invalid password");
        }

        Long customerId = null;
        if ("CUSTOMER".equalsIgnoreCase(user.getUserRoles())) {
            Optional<Customer> optionalCustomer = customerRepo.findByName(user.getUserName());
            if (optionalCustomer.isPresent()) {
                customerId = optionalCustomer.get().getId();
            }
        }

        log.info("User successfully logged in: {}. Customer ID: {}", username, customerId);
        return new UserDTO(user.getUserId(), customerId, user.getUserName(), user.getUserRoles());
    }

    @Override
    public void saveUser(SignupDTO signupDTO) {
        log.info("Saving user credentials for: {}", signupDTO.getName());

        User user = new User();
        user.setUserName(signupDTO.getName());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        user.setUserRoles("CUSTOMER");

        userRepo.save(user);
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
        UserDTO userDTO = userRepo.selectUser(userId);
        if (userDTO == null) {
            throw new ApiException(404, "User not found with ID: " + userId);
        }
        return userDTO;
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        log.info("Attempting to update user with ID: {}", userDTO.getUserId());

        Optional<User> optionalUser = userRepo.findById(userDTO.getUserId());
        if (optionalUser.isEmpty()) {
            log.error("Update failed: No user found with ID: {}", userDTO.getUserId());
            throw new ApiException(404, "User not found");
        }

        User user = optionalUser.get();
        user.setUserName(userDTO.getUserName());
        user.setUserRoles(userDTO.getUserRoles());

        if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepo.save(user);
        log.info("User updated successfully with ID: {}", userDTO.getUserId());
    }
}