package forum.posts;

import forum.misc.models.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Kirill Cherkasov, 2018
 */

@SuppressWarnings({"unused", "SpringAutowiredFieldsWarningInspection"})
@RestController
@RequestMapping("api/post")
public class ControllerPosts {
    @Autowired
    private ServiceClassPosts projectPostService;

    @RequestMapping(value = "{id}/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPostDetails(@PathVariable(value = "id") @NotNull Integer postId,
                                         @RequestParam(value = "related", required = false)
                                         @Nullable List<String> extraKeys) {
        try {
            final ExtendedModel requestedPost = projectPostService.getExtendedPostDataById(postId, extraKeys);
            return ResponseEntity.status(HttpStatus.OK).body(requestedPost);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
        }
    }

    @RequestMapping(value = "{id}/details", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePostDetails(@PathVariable(value = "id") @NotNull Integer id,
                                            @RequestBody @NotNull OnUpdateModel postUpdateData) {
        try {
            final Model updatedPost = projectPostService.updatePost(id, postUpdateData);
            return ResponseEntity.status(HttpStatus.OK).body(updatedPost);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
        }
    }
}
