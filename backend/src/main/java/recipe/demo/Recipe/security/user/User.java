package recipe.demo.Recipe.security.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import recipe.demo.Recipe.recipe.Comment;
import recipe.demo.Recipe.recipe.Recipe;
import recipe.demo.Recipe.security.token.Token;

import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Integer id;
  private String firstname;
  private String lastname;
  private String email;


  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;


  @JsonIgnoreProperties("creator")
  @OneToMany(mappedBy = "creator", orphanRemoval = true)
  private List<Recipe> recipes;

  @JsonIgnoreProperties("creator")
  @OneToMany(mappedBy = "creator", orphanRemoval = true)
  private List<Comment> comments;

  @JsonIgnore
  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<Token> tokens;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public UserDTO toDTO() {
    UserDTO dto = new UserDTO();
    dto.setId(this.getId());
    dto.setFirstname(this.getFirstname());
    dto.setLastname(this.getLastname());
    dto.setEmail(this.getEmail());
    dto.setRole(this.getRole());
    return dto;
  }
}
