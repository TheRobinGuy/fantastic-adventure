package com.example.API.Controller;

import com.example.API.Dto.*;
import com.example.API.Entity.User;
import com.example.API.Security.CurrentUser;
import com.example.API.Security.UserPrincipal;
import com.example.API.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthService authService;

    @Inject
    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName(),
                currentUser.getAuthorities());
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailabilty(@RequestParam(value = "username") String username) {
        return new UserIdentityAvailability(authService.isUsernameAvailable(username));
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        return new UserIdentityAvailability(authService.isEmailAvailable(email));
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = authService.findUserOrThrow(username);
        return new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getEmail());
    }

    @PostMapping("/auth/signup")
    public ResponseEntity registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        URI location = authService.registerUser(signUpRequest);
        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String jwt = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

}