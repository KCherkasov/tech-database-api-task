package forum.threads;

import forum.misc.models.Error;
import forum.misc.models.Vote;
import forum.posts.ServiceClassPosts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author KCherkasov, 2018
 */

@RestController
@RequestMapping("api/thread")
public class ControllerThreads {
    @Autowired
    private ServiceClassThreads projectThreadService;
    @Autowired
    private ServiceClassPosts projectPostService;

    @SuppressWarnings("TryWithIdenticalCatches")
    @RequestMapping(value = "{slug_or_id}/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createPostsBunch(@PathVariable(value = "slug_or_id") @NotNull String tSlugOrId,
                                           @RequestBody @NotNull List<forum.posts.Model> postsBunch) {
        try {
            final List<forum.posts.Model> addedPosts = projectPostService.bulkCreate(tSlugOrId, postsBunch);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedPosts);
        } catch (IncorrectResultSizeDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error(e.getMessage()));
        }
    }

    @RequestMapping(value = "{slug_or_id}/details", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getThread(@PathVariable(value = "slug_or_id") @NotNull String tSlugOrId) {
        try {
            final Model requestedThread = projectThreadService.getByTSlugOrId(tSlugOrId);
            return ResponseEntity.status(HttpStatus.OK).body(requestedThread);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
        }
    }

    @RequestMapping(value = "{slug_or_id}/posts", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOrderedPosts(@PathVariable(value = "slug_or_id") @NotNull String slugOrId,
                                          @RequestParam(value = "limit", required = false)
                                          @Nullable Integer entriesCount,
                                          @RequestParam(value = "since", required = false)
                                              @Nullable Integer startPostId,
                                          @RequestParam(value = "sort", required = false) @Nullable String sortKind,
                                          @RequestParam(value = "desc", required = false)
                                              @Nullable Boolean orderingKind) {
        try {
            final List<forum.posts.Model> requestedPosts = projectThreadService.getThreadPosts(slugOrId,
                    entriesCount, startPostId, sortKind, orderingKind);
            return ResponseEntity.status(HttpStatus.OK).body(requestedPosts);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new java.lang.Error(e.getMessage()));
        }
    }

    @RequestMapping(value = "{slug_or_id}/details", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateThread(@PathVariable(value = "slug_or_id") @NotNull String tSlugOrId,
                                       @RequestBody @NotNull OnUpdateModel threadUpdateData) {
        try {
            final Model updatedThread = projectThreadService.updateThread(tSlugOrId, threadUpdateData);
            return ResponseEntity.status(HttpStatus.OK).body(updatedThread);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
        }
    }

    @RequestMapping(value = "{slug_or_id}/vote", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity voteForThread(@PathVariable(value = "slug_or_id") @NotNull String tSlugOrId,
                                        @RequestBody @NotNull Vote voteData) {
        try {
            final Model votedThread = projectThreadService.voteThread(tSlugOrId, voteData);
            return ResponseEntity.status(HttpStatus.OK).body(votedThread);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error(e.getMessage()));
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
        }
    }
}
