package forum.users;

import forum.misc.models.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author KCherkasov, 2018
 */

@RestController
@RequestMapping("api/user")
public class ControllerUsers {
    @Autowired
    private ServiceClassUsers projectUserService;

    @RequestMapping(value = "{nickname}/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createNewUser(@PathVariable(value = "nickname") @NotNull String nickname,
                                        @RequestBody @NotNull Model newUserModel) {
        try {
            projectUserService.createUser(nickname, newUserModel);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(projectUserService.getByNicknameOrEmail(nickname,
                    newUserModel.getEmail()));
        }
        newUserModel.setNickname(nickname);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserModel);
    }

    @RequestMapping(value = "{nickname}/profile", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserByNickname(@PathVariable(value = "nickname") @NotNull String nickname) {
        final Model requestedUser;
        try {
            requestedUser = projectUserService.getByNickname(nickname);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(requestedUser);
    }

    @RequestMapping(value = "{nickname}/profile", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUserByNickname(@PathVariable(value = "nickname") @NotNull String nickname,
                                               @RequestBody @NotNull OnUpdateModel profileUpdateData) {
        try {
            final Model updatedUser = projectUserService.updateUser(nickname, profileUpdateData);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error(e.getMessage()));
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
        }
    }
}
