package forum.users;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author KCherkasov, 2018
 */

public class Model {
    private String fullname;
    private String nickname;
    private String email;
    private String about;

    public Model(@JsonProperty("about") @NotNull String about,
                 @JsonProperty("email") @NotNull String email,
                 @JsonProperty("fullname") @NotNull String fullname,
                 @JsonProperty("nickname") @NotNull String nickname) {
        this.fullname = fullname;
        this.nickname = nickname;
        this.email = email;
        this.about = about;
    }

    public String getFullname() {
        return fullname;
    }

    public String getNickname() {
        return nickname;
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

    public void setNickname(@NotNull String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public void setAbout(@NotNull String about) {
        this.about = about;
    }
}
