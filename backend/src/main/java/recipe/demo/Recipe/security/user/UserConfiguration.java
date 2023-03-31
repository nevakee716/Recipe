package recipe.demo.Recipe.security.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Optional;

@Configuration
public class UserConfiguration {

        @Bean
        CommandLineRunner commandLineRunnerUser(UserRepository repository) {
            return args -> {
                // Create Recipe 1
                Optional<User> r = repository.findByEmail("admin");
                if(!r.isPresent()) {
                    User adminUser = new User(1,"admin","admin","admin","admin",Role.ADMIN,new ArrayList<>());
                    repository.save(adminUser);
                }
            };
        }
    }
