package com.example.API.Security;

import static com.example.API.Enums.RoleName.ROLE_ADMIN;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.API.Entity.Role;
import com.example.API.Entity.User;
import com.example.API.Repository.RoleRepository;
import com.example.API.Repository.UserRepository;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;

  boolean alreadySetup = false;

  @Inject
  public InitialDataLoader(final UserRepository userRepository, RoleRepository roleRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {

    if (alreadySetup) {
      return;
    }

    Optional<Role> adminRoleResult = roleRepository.findByName(ROLE_ADMIN);

    if (adminRoleResult.isPresent()) {
      Set<Role> roles = new HashSet<>();
      Role adminRole = adminRoleResult.get();
      roles.add(adminRole);

      User user = new User();
      user.setRoles(roles);
      user.setEmail("admin@overstock.com");
      user.setName("admin");
      user.setUsername("admin");
      user.setPassword(passwordEncoder.encode("admin"));
      userRepository.save(user);

      alreadySetup = true;
    }
  }
}
