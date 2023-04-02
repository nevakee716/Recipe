package recipe.demo.Recipe.security.auth;

import recipe.demo.Recipe.security.config.JwtService;
import recipe.demo.Recipe.security.token.Token;
import recipe.demo.Recipe.security.token.TokenRepository;
import recipe.demo.Recipe.security.token.TokenType;
import recipe.demo.Recipe.security.user.Role;
import recipe.demo.Recipe.security.user.User;
import recipe.demo.Recipe.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public User register(User user) {
    Optional<User> existingUser = repository.findById(user.getId());
    if(existingUser.isPresent()) {
      User editedUser = existingUser.get();
      editedUser.setEmail(user.getEmail());
      editedUser.setFirstname(user.getFirstname());
      editedUser.setLastname(user.getLastname());
      editedUser.setRole(user.getRole());
      if(user.getPassword() != null) editedUser.setPassword(passwordEncoder.encode(user.getPassword()));
      return repository.save(editedUser);
    } else {
      var newUser = User.builder()
              .firstname(user.getFirstname())
              .lastname(user.getLastname())
              .email(user.getEmail())
              .password(passwordEncoder.encode(user.getPassword()))
              .role(user.getRole())
              .build();
      return repository.save(newUser);
    }
  }




  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }
}
