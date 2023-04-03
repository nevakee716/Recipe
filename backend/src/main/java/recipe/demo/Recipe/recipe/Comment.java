package recipe.demo.Recipe.recipe;


import jakarta.persistence.*;
import recipe.demo.Recipe.security.user.User;
import recipe.demo.Recipe.security.user.UserDTO;

import java.util.Date;
import java.util.List;

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


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    private User creator;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public UserDTO getCreator() {
        return creator.toDTO();
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
