package forum.misc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author KCherkasov, 2018
 */

public class Status {
    private Integer forum;
    private Integer post;
    private Integer thread;
    private Integer user;

    public Status(@JsonProperty("forum") @NotNull Integer forum,
                  @JsonProperty("post") @NotNull Integer post,
                  @JsonProperty("thread") @NotNull Integer thread,
                  @JsonProperty("user") @NotNull Integer user) {
        this.forum = forum;
        this.post = post;
        this.thread = thread;
        this.user = user;
    }

    public Integer getForum() {
        return forum;
    }

    public Integer getPost() {
        return post;
    }

    public Integer getThread() {
        return thread;
    }

    public Integer getUser() {
        return user;
    }

    public void setForum(@NotNull Integer forum) {
        this.forum = forum;
    }

    public void setPost(@NotNull Integer post) {
        this.post = post;
    }

    public void setThread(@NotNull Integer thread) {
        this.thread = thread;
    }

    public void setUser(@NotNull Integer user) {
        this.user = user;
    }
}
