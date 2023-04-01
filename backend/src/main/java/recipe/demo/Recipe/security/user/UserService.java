package recipe.demo.Recipe.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserFromPrincipal(Principal principal) {
        if (principal == null) {
            return null;
        }

        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        return user;
    }

    public UserDTO getUserDTOFromPrincipal(Principal principal) {
        if (principal == null) {
            return null;
        }

        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        return user.toDTO();
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> user.toDTO()).toList();
    }


}