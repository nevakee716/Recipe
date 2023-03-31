package recipe.demo.Recipe.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipe.demo.Recipe.security.user.Role;
import recipe.demo.Recipe.security.user.UserService;

import java.security.Principal;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<?> register(Principal principal,
      @RequestBody RegisterRequest request
  ) {
    if (principal == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }
    // only admin can register other user
    if (userService.getUserFromPrincipal(principal).getRole() != Role.ADMIN) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not Admin");
    }
    return ResponseEntity.ok(service.register(request));
  }


  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @GetMapping("/userinfo")
  public ResponseEntity<?> getUserInfo(Principal principal) {
    if (principal == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }

    return ResponseEntity.ok(userService.getUserFromPrincipal(principal));
  }


}
