package recipe.demo.Recipe.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipe.demo.Recipe.security.user.Role;
import recipe.demo.Recipe.security.user.User;
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
      @RequestBody User user
  ) {
    if (principal == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }
    // only admin can register other user
    if (userService.getUserFromPrincipal(principal).getRole() != Role.ADMIN) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not Admin");
    }
    return ResponseEntity.ok(service.register(user));
  }


  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    try{
      return ResponseEntity.ok(service.authenticate(request));
    } catch (Exception e) {
      return ResponseEntity.status(403).body(e.getMessage());
    }

  }

  @GetMapping("/userinfo")
  public ResponseEntity<?> getUserInfo(Principal principal) {
    if (principal == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
    }

    return ResponseEntity.ok(userService.getUserDTOFromPrincipal(principal));
  }


}
