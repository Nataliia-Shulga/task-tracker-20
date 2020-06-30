package com.task.tracker.servise;

import com.task.tracker.entity.user.EUserRole;
import com.task.tracker.entity.user.User;
import com.task.tracker.entity.user.UserRole;
import com.task.tracker.repository.UserRepository;
import com.task.tracker.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Page<User> findAll(Pageable paging) {
        return userRepository.findAll(paging);
    }

    public Optional<User> findById(java.lang.Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        User newUser = new User(user.getUsername(),
                user.getEmail(), user.getPassword(),
                user.getFirstName(), user.getLastName(),
                user.getRoles());
        return userRepository.save(newUser);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public Set<UserRole> getRoles(Set<String> strRoles) {
        Set<UserRole> roles = new HashSet<>();
        if (strRoles == null) {
            UserRole userRole = userRoleRepository.findByName(EUserRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        UserRole adminRole = userRoleRepository.findByName(EUserRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        UserRole modRole = userRoleRepository.findByName(EUserRole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        UserRole userRole = userRoleRepository.findByName(EUserRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        return roles;
    }

    public void deleteByUsername(String username) {
        Optional<User> userFromDB = userRepository.findByUsername(username);
        userFromDB.ifPresent(user -> userRepository.delete(user));
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
