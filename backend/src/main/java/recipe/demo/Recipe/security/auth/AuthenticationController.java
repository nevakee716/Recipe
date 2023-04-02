package recipe.demo.Recipe.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

  @DeleteMapping("/user/{userId}")
  public ResponseEntity<?> deleteUser(@PathVariable Integer userId,Principal principal) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    if (user.getRole() != Role.ADMIN) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not Admin");
    }
    userService.delete(userId);
    return ResponseEntity.ok(userService.getUserDTOFromPrincipal(principal));
  }

  @GetMapping("/userinfo")
  public ResponseEntity<?> getUserInfo(Principal principal) {

    return ResponseEntity.ok(userService.getUserDTOFromPrincipal(principal));
  }


  @GetMapping("/users")
  public ResponseEntity<?> getUsers() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    if (user.getRole() != Role.ADMIN) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not Admin");
    }

    return ResponseEntity.ok(userService.getAllUsers());
  }

}
