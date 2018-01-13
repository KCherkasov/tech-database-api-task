package forum.posts;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author KCherkasov, 2018
 */

public class OnUpdateModel {
    private String message;

    public OnUpdateModel(@JsonProperty("message") @NotNull String message) {
        this.message = message;
    }

    public final String getMessage() {
        return message;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }
}
