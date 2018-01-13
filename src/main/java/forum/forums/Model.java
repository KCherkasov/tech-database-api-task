package forum.forums;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author KCherkasov, 2018
 */

@SuppressWarnings("unused")
public class Model {
    private String title;
    private String slug;
    private String user;
    private Integer threads;
    private Integer posts;

    public Model(@JsonProperty("posts") @NotNull Integer posts,
                 @JsonProperty("slug") @NotNull String slug,
                 @JsonProperty("threads") @NotNull Integer threads,
                 @JsonProperty("title") @NotNull String title,
                 @JsonProperty("user") @NotNull String user) {
        this.title = title;
        this.slug = slug;
        this.user = user;
        this.threads = threads;
        this.posts = posts;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getUser() {
        return user;
    }

    public Integer getThreads() {
        return threads;
    }

    public Integer getPosts() {
        return posts;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public void setSlug(@NotNull String slug) {
        this.slug = slug;
    }

    public void setUser(@NotNull String user) {
        this.user = user;
    }

    public void setThreads(@NotNull Integer threads) {
        this.threads = threads;
    }

    public void setPosts(@NotNull Integer posts) {
        this.posts = posts;
    }
}
