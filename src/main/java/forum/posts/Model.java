package forum.posts;

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
    private Integer thread;
    private Integer parent;
    private String message;
    private Boolean isEdited;

    public Model(@JsonProperty("author") @NotNull String author,
                 @JsonProperty("created") @NotNull String created,
                 @JsonProperty("forum") @NotNull String forum,
                 @JsonProperty("id") @NotNull Integer id,
                 @JsonProperty("isEdited") @NotNull Boolean isEdited,
                 @JsonProperty("message") @NotNull String message,
                 @JsonProperty("parent") @NotNull Integer parent,
                 @JsonProperty("thread") @NotNull Integer thread) {
        this.id = id;
        this.author = author;
        this.created = created;
        this.forum = forum;
        this.thread = thread;
        this.parent = parent;
        this.message = message;
        this.isEdited = isEdited;
    }

    public final Integer getId() {
        return id;
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

    public final Integer getThread() {
        return thread;
    }

    public final Integer getParent() {
        return parent;
    }

    public final String getMessage() {
        return message;
    }

    public final Boolean getIsEdited() {
        return isEdited;
    }

    public void setId(@NotNull Integer id) {
        this.id = id;
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

    public void setThread(@NotNull Integer thread) {
        this.thread = thread;
    }

    public void setParent(@NotNull Integer parent) {
        this.parent = parent;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }

    public void setIsEdited(@NotNull Boolean isEdited) {
        this.isEdited = isEdited;
    }
}
