package forum.posts;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * @author KCherkasov, 2018
 */

public class ExtendedModel {
    private forum.users.Model author;
    private forum.forums.Model forum;
    private forum.threads.Model thread;
    private Model post;

    public ExtendedModel(@JsonProperty("author") @Nullable forum.users.Model author,
                         @JsonProperty("forum") @Nullable forum.forums.Model forum,
                         @JsonProperty("post") @NotNull Model post,
                         @JsonProperty("thread") @Nullable forum.threads.Model thread) {
        this.author = author;
        this.forum = forum;
        this.thread = thread;
        this.post = post;
    }

    public forum.users.Model getAuthor() {
        return author;
    }

    public forum.forums.Model getForum() {
        return forum;
    }

    public forum.threads.Model getThread() {
        return thread;
    }

    public Model getPost() {
        return post;
    }

    public void setAuthor(@NotNull forum.users.Model user) {
        this.author = user;
    }

    public void setForum(@NotNull forum.forums.Model forum) {
        this.forum = forum;
    }

    public void setThread(@NotNull forum.threads.Model thread) {
        this.thread = thread;
    }

    public void setPost(@NotNull Model post) {
        this.post = post;
    }
}
