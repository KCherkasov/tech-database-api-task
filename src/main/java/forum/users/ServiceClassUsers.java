package forum.users;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

import static forum.rowmappers.ProjectRowMappers.USER_MAPPER;

/**
 * @author KCherkasov, 2018
 */

@Service
public class ServiceClassUsers {
    private final JdbcTemplate template;

    public ServiceClassUsers(@NotNull JdbcTemplate template) {
        this.template = template;
    }

    public void createUser(@NotNull String nickname, @NotNull Model newUser) {
        template.update(QueryList.CREATE_NEW_USER, newUser.getAbout(), newUser.getEmail(),
                newUser.getFullname(), nickname);
    }

    public void clearTable() {
        template.execute(QueryList.CLEAR_USERS_TABLE);
    }

    public Integer tableStatus() {
        return template.queryForObject(QueryList.USERS_TABLE_STATUS, Integer.class);
    }

    public Model getByNickname(@NotNull String nickname) {
        return template.queryForObject(QueryList.GET_USER_BY_NICKNAME, USER_MAPPER, nickname);
    }

    public List<Model> getByNicknameOrEmail(@NotNull String nickname, @NotNull String email) {
        return template.query(QueryList.GET_USER_BY_NICKNAME_OR_EMAIL, USER_MAPPER, nickname, email);
    }

    public Model updateUser(@NotNull String nickname, @NotNull OnUpdateModel userUpdateData) {
        final Model updatedUser = getByNickname(nickname);
        if (userUpdateData.getFullname() != null) {
            updatedUser.setFullname(userUpdateData.getFullname());
        }
        if (userUpdateData.getEmail() != null) {
            updatedUser.setEmail(userUpdateData.getEmail());
        }
        if (userUpdateData.getAbout() != null) {
            updatedUser.setAbout(userUpdateData.getAbout());
        }
        template.update(QueryList.UPDATE_USER, updatedUser.getAbout(), updatedUser.getEmail(),
                updatedUser.getFullname(), nickname);
        return getByNickname(nickname);
    }
}
