package com.example.API.Service;

import com.example.API.Dto.LoginRequest;
import com.example.API.Dto.SignUpRequest;
import com.example.API.Entity.Role;
import com.example.API.Entity.User;
import com.example.API.Exception.AppException;
import com.example.API.Exception.ResourceNotFoundException;
import com.example.API.Repository.RoleRepository;
import com.example.API.Repository.UserRepository;
import com.example.API.Security.JwtTokenProvider;
import com.google.common.base.Preconditions;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

import static com.example.API.Enums.RoleName.ROLE_USER;

@Named
public class AuthService {

    private static final Logger LOGGER = Logger.getLogger(AuthService.class);

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    @Inject
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public void setAuthenticationContext(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    public Boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    public User findUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    public URI registerUser(SignUpRequest signUpRequest) {
        verifyDto(signUpRequest);

        User user = getUser(signUpRequest);
        setRole(user);
        userRepository.save(user);

        return buildURI(signUpRequest.getUsername());
    }

    private void verifyDto(SignUpRequest signUpRequest) {
        Preconditions.checkArgument(signUpRequest.getUsername() != null, "Username can't be null");
        Preconditions.checkArgument(signUpRequest.getEmail() != null, "Email can't be null");
        Preconditions.checkArgument(signUpRequest.getName() != null, "Name can't be null");
        Preconditions.checkArgument(signUpRequest.getPassword() != null, "Password can't be null");

        Preconditions.checkState(userRepository.existsByUsername(signUpRequest.getUsername()) == false,
                "Username is already taken. Please try another");
        Preconditions.checkState(userRepository.existsByEmail(signUpRequest.getEmail()) == false,
                "An account is already associated with this email");
    }

    private void setRole(User user) {
        Role userRole = roleRepository.findByName(ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));
    }

    private URI buildURI(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath() //
                .path("/api/users/{username}") //
                .buildAndExpand(username) //
                .toUri();
    }

    User getUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        String encPw = passwordEncoder.encode(signUpRequest.getPassword());
        return new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(), encPw);
    }

    public String authenticateUser(LoginRequest loginRequest) {
        Preconditions.checkArgument(loginRequest.getUsernameOrEmail() != null, "Must Provide Email or Username");
        Preconditions.checkArgument(loginRequest.getPassword() != null, "Must Provide a Password");

        LOGGER.info("Username or Email: " + loginRequest.getUsernameOrEmail());
        LOGGER.info("Username/Email Exists? " + userRepository
                .existsByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail()));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        setAuthenticationContext(authentication);

        return tokenProvider.generateToken(authentication);
    }

}