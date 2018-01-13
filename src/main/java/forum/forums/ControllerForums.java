package forum.forums;

import forum.misc.models.Error;
import forum.threads.ServiceClassThreads;
import forum.users.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author KCherkasov, 2018
 */

@SuppressWarnings({"unused", "SpringAutowiredFieldsWarningInspection"})
@RestController
@RequestMapping(value = "api/forum")
public class ControllerForums {
    @Autowired
    private ServiceClassForums projectForumService;
    @Autowired
    private ServiceClassThreads projectThreadService;

    @RequestMapping(value = "create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createForum(@RequestBody @NotNull forum.forums.Model model) {
        try {
            final forum.forums.Model newForum = projectForumService.createForum(model);
            return ResponseEntity.status(HttpStatus.CREATED).body(newForum);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(projectForumService.getForumByFSlug(model.getSlug()));
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
        }
    }

    @RequestMapping(value = "{slug}/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getForumDetails(@PathVariable(value = "slug") @NotNull String fSlug) {
        try {
            final forum.forums.Model requestedForum = projectForumService.getForumByFSlug(fSlug);
            return ResponseEntity.status(HttpStatus.OK).body(requestedForum);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Error("Can't find forum with slug " + fSlug));
        }
    }

    @RequestMapping(value = "{slug}/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getForumUsers(@PathVariable(value = "slug") @NotNull String fSlug,
                                        @RequestParam(value = "limit", required = false) @Nullable Integer entriesCount,
                                        @RequestParam(value = "since", required = false) @Nullable String startNickname,
                                        @RequestParam(value = "desc", required = false) @Nullable Boolean orderingKind) {
        try {
            final List<Model> requestedForumUsers = projectForumService.getForumUsers(fSlug, entriesCount,
                    startNickname, orderingKind);
            return ResponseEntity.status(HttpStatus.OK).body(requestedForumUsers);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
        }
    }

    @RequestMapping(value = "{slug}/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createThread(@RequestBody @NotNull forum.threads.Model model,
                                       @PathVariable(value = "slug") @NotNull String fSlug) {
        try {
            final forum.threads.Model newThread = projectThreadService.createNewThread(fSlug, model);
            return ResponseEntity.status(HttpStatus.CREATED).body(newThread);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(projectThreadService.getByTSlugOrId(model.getSlug()));
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
        }
    }

    @RequestMapping(value = "{slug}/threads", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getForumThreads(@PathVariable(value = "slug") @NotNull String fSlug,
                                          @RequestParam(value = "limit", required = false)
                                              @Nullable Integer entriesCount,
                                          @RequestParam(value = "since", required = false) @Nullable String startDate,
                                          @RequestParam(value = "desc", required = false)
                                              @Nullable Boolean orderingKind) {
        try {
            final forum.forums.Model requestedForum = projectForumService.getForumByFSlug(fSlug);
            final List<forum.threads.Model> requestedForumThreads = projectForumService.getForumThreads(
                    fSlug, entriesCount, startDate, orderingKind);
            return ResponseEntity.status(HttpStatus.OK).body(requestedForumThreads);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
        }
    }
}
