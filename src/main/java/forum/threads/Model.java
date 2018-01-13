package forum.threads;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author KCherkasov, 2018
 */

public class Model {
    private Integer id;
    private String author;
    private String created;
    private String forum;
    private String slug;
    private String title;
    private String message;
    private Integer votes;

    public Model(@JsonProperty("author") @NotNull String author,
                 @JsonProperty("created") @NotNull String created,
                 @JsonProperty("forum") @NotNull String forum,
                 @JsonProperty("id") @NotNull Integer id,
                 @JsonProperty("message") @NotNull String message,
                 @JsonProperty("slug") @NotNull String slug,
                 @JsonProperty("title") @NotNull String title,
                 @JsonProperty("votes") @NotNull Integer votes) {
        this.id = id;
        this.author = author;
        this.created = created;
        this.forum = forum;
        this.slug = slug;
        this.title = title;
        this.message = message;
        this.votes = votes;
    }

    public final String getAuthor() {
        return author;
    }

    public final String getCreated() {
        return created;
    }

    public final String getForum() {
        return forum;
    }

    public final Integer getId() {
        return id;
    }

    public final String getMessage() {
        return message;
    }

    public final String getSlug() {
        return slug;
    }

    public final String getTitle() {
        return title;
    }

    public final Integer getVotes() {
        return votes;
    }

    public void setAuthor(@NotNull String author) {
        this.author = author;
    }

    public void setCreated(@NotNull String created) {
        this.created = created;
    }

    public void setForum(@NotNull String forum) {
        this.forum = forum;
    }

    public void setId(@NotNull Integer id) {
        this.id = id;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }

    public void setSlug(@NotNull String slug) {
        this.author = slug;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public void setVotes(@NotNull Integer votes) {
        this.votes = votes;
    }
}
