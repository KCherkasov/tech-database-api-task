package forum.misc;

import forum.forums.ServiceClassForums;
import forum.misc.models.Status;
import forum.posts.ServiceClassPosts;
import forum.threads.ServiceClassThreads;
import forum.users.ServiceClassUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author KCherkasov, 2018
 */

@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@RestController
@RequestMapping("api/service")
public class ControllerDatabaseServiceApi {
    @Autowired
    private ServiceClassUsers projectUserService;
    @Autowired
    private ServiceClassForums projectForumService;
    @Autowired
    private ServiceClassPosts projectPostService;
    @Autowired
    private ServiceClassThreads projectThreadService;

    @RequestMapping(value = "clear", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> clearWholeDatabase() {
        projectUserService.clearTable();
        projectForumService.clearForumTable();
        projectPostService.clearTable();
        projectThreadService.clearTable();
        projectForumService.clearForumVisitors();
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> getWholeDatabaseStatus() {
        final Integer totalUsers = projectUserService.tableStatus();
        final Integer totalForums = projectForumService.forumTableStatus();
        final Integer totalPosts = projectPostService.tableStatus();
        final Integer totalThreads = projectThreadService.tableStatus();

        return ResponseEntity.status(HttpStatus.OK).body(new Status(totalForums, totalPosts,
                totalThreads, totalUsers));
    }
}
