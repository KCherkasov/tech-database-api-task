package forum.threads;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author KCherkasov, 2018
 */

public class OnUpdateModel {
    private String title;
    private String message;

    public OnUpdateModel(@JsonProperty("message") @NotNull String message,
                         @JsonProperty("title") @NotNull String title) {
        this.title = title;
        this.message = message;
    }

    public final String getTitle() {
        return title;
    }

    public final String getMessage() {
        return message;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }
}
