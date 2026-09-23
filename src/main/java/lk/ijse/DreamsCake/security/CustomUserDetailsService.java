package lk.ijse.DreamsCake.security;

import lk.ijse.DreamsCake.entity.Rider;
import lk.ijse.DreamsCake.entity.User;
import lk.ijse.DreamsCake.repository.RiderRepo;
import lk.ijse.DreamsCake.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepository;
    private final RiderRepo riderRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByUserName(usernameOrEmail);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String userRolesStr = user.getUserRoles();
            String[] roles = parseRoles(userRolesStr);

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .roles(roles)
                    .build();
        }

        Optional<Rider> optionalRider = riderRepository.findByEmail(usernameOrEmail);

        if (optionalRider.isPresent()) {
            Rider rider = optionalRider.get();

            String roleName = rider.getRole() != null ? rider.getRole().name() : "RIDER";

            return org.springframework.security.core.userdetails.User.builder()
                    .username(rider.getEmail())
                    .password(rider.getPassword())
                    .roles(roleName)
                    .build();
        }

        throw new UsernameNotFoundException("User or Rider not found with username/email: " + usernameOrEmail);
    }

    private String[] parseRoles(String userRolesStr) {
        if (userRolesStr != null && !userRolesStr.trim().isEmpty()) {
            return Arrays.stream(userRolesStr.split(","))
                    .map(String::trim)
                    .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                    .filter(role -> !role.isEmpty())
                    .toArray(String[]::new);
        }
        return new String[0];
    }
}