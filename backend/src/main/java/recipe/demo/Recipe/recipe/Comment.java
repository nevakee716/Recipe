package recipe.demo.Recipe.recipe;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import recipe.demo.Recipe.security.user.User;
import recipe.demo.Recipe.security.user.UserDTO;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Comment {
    @Id
    @SequenceGenerator(
            name = "comment_sequence",
            sequenceName = "comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_sequence"
    )
    private Long id;
    private String title;

    private Date creationDate;

    @Column(length = 5000)
    private String content;

    @JsonIgnoreProperties("comment")
    @ManyToOne(fetch = FetchType.EAGER)
    private User creator;

    @JsonIgnoreProperties("comment")
    @ManyToOne(fetch = FetchType.EAGER)
    private Recipe recipe;

    public UserDTO getCreator() {
        return creator.toDTO();
    }
}
