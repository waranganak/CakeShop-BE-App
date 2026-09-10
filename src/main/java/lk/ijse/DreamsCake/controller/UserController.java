package lk.ijse.DreamsCake.controller;

import jakarta.validation.Valid;
import lk.ijse.DreamsCake.constant.CommonResponse;
import lk.ijse.DreamsCake.dto.AuthDTO;
import lk.ijse.DreamsCake.dto.SignupDTO;
import lk.ijse.DreamsCake.dto.UserDTO;
import lk.ijse.DreamsCake.dto.UserDataDTO;
import lk.ijse.DreamsCake.security.JwtUtil;
import lk.ijse.DreamsCake.service.CustomerService;
import lk.ijse.DreamsCake.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "v1/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CustomerService customerService;

    @GetMapping(value = "/testing")
    public String testSecurity(){
        return "API Security Successful";
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse signupCustomer(@Valid @RequestBody SignupDTO signupDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return new CommonResponse(400, null, errorMsg);
        }

        customerService.saveCustomer(signupDTO);

        userService.saveUser(signupDTO);

        return new CommonResponse(0, "Customer Registered Successfully!");
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse authLogin(@RequestBody AuthDTO authDTO) {
        UserDTO userDetails = userService.getUserDetails(authDTO.getUserName(), authDTO.getPassword());
        String token = jwtUtil.generateToken(userDetails);

        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setUserId(userDetails.getUserId());
        userDataDTO.setToken(token);
        userDataDTO.setRole(userDetails.getUserRoles());

        return new CommonResponse(0, userDataDTO, "JWT Token");
    }



    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllUsers(){
        List<UserDTO> allUsers = userService.getAllUsers();
        return new CommonResponse(0, allUsers, "Get All users API");
    }

    @GetMapping(value = "/filter-users", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse filterUsers(@RequestParam(value = "userName", required = false) String userName){
        List<UserDTO> userDTOS = userService.filterUsers(userName);
        return new CommonResponse(0, userDTOS, "Get Filter users API");
    }

    @GetMapping(value = "/select-user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectUser(@PathVariable long userId){
        UserDTO dto = userService.selectUser(userId);
        return new CommonResponse(0, dto, "USER SELECTED");
    }

    @PutMapping(value = "/update-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return new CommonResponse(400, null, errorMsg);
        }

        userService.updateUser(userDTO);
        return new CommonResponse(0, "USER UPDATED");
    }
}