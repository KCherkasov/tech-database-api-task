package forum.users;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author KCherkasov, 2018
 */

public class OnUpdateModel {

    private String fullname;
    private String email;
    private String about;

    public OnUpdateModel(@JsonProperty("about") @NotNull String about,
                         @JsonProperty("email") @NotNull String email,
                         @JsonProperty("fullname") @NotNull String fullname) {
        this.fullname = fullname;
        this.email = email;
        this.about = about;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getAbout() {
        return about;
    }

    public void setFullname(@NotNull String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public void setAbout(@NotNull String about) {
        this.about = about;
    }
}
