package forum.misc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author KCherkasov, 2018
 */

public class Error {
    private String message;

    public Error(@JsonProperty("message") @NotNull String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }
}
