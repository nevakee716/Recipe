package recipe.demo.Recipe.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import recipe.demo.Recipe.security.auth.AuthenticationResponse;

import java.security.Principal;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getUserFromPrincipal(Principal principal) {
        if (principal == null) {
            return null;
        }

        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        return user.toDTO();
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> user.toDTO()).toList();
    }


}