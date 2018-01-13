package forum.misc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author KCherkasov, 2018
 */

public class Vote {

    private String nickname;
    private Integer voice;

    public Vote(@JsonProperty("nickname") @NotNull String nickname,
                @JsonProperty("voice") @NotNull Integer voice) {
        this.nickname = nickname;
        this.voice = voice;
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getVoice() {
        return voice;
    }

    public void setNickname(@NotNull String nickname) {
        this.nickname = nickname;
    }

    public void setVoice(@NotNull Integer voice) {
        this.voice = voice;
    }
}
