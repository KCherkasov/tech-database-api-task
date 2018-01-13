package forum.forums;

import forum.forums.visitors.QueryList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static forum.rowmappers.ProjectRowMappers.*;

/**
 * @author KCherkasov, 2018
 */

@Service
public class ServiceClassForums {
    private JdbcTemplate template;

    public ServiceClassForums(@NotNull JdbcTemplate template) {
        this.template = template;
    }

    public Model createForum(@NotNull Model model) {
        template.update(forum.forums.QueryList.CREATE_NEW_FORUM, model.getSlug(),
                model.getTitle(), model.getUser(), model.getUser());
        return getForumByFSlug(model.getSlug());
    }

    public Model getForumByFSlug(@NotNull String fSlug) {
        return template.queryForObject(forum.forums.QueryList.GET_FORUM_BY_FSLUG, FORUM_MAPPER, fSlug);
    }

    public Integer forumTableStatus() {
        return template.queryForObject(forum.forums.QueryList.FORUM_TABLE_STATUS, Integer.class);
    }

    public void clearForumTable() {
        template.execute(forum.forums.QueryList.CLEAR_FORUM_TABLE);
    }

    public void clearForumVisitors() {
        template.execute(QueryList.CLEAR_TABLE);
    }

    public List<forum.users.Model> getForumUsers(@NotNull String fSlug, @Nullable Integer entriesCount,
                                     @Nullable String startNickname, @Nullable Boolean orderingKind) {
        final Integer forumId = template.queryForObject(forum.forums.QueryList.GET_FORUM_ID_BY_FSLUG,
                Integer.class, fSlug);
        return template.query(forum.forums.QueryList.getForumUsers(entriesCount, startNickname, orderingKind),
                USER_MAPPER, forumId);
    }

    public List<forum.threads.Model> getForumThreads(@NotNull String fSlug, @Nullable Integer entriesCount,
                                                     @Nullable String startDate, @Nullable Boolean orderingKind) {
        final List<Object> extraKeys = new ArrayList<>();
        extraKeys.add(fSlug);
        if (startDate != null) {
            extraKeys.add(startDate);
        }
        if (entriesCount != null) {
            extraKeys.add(entriesCount);
        }
        return template.query(forum.forums.QueryList.getForumThreads(entriesCount, startDate, orderingKind),
                extraKeys.toArray(), THREAD_MAPPER);
    }
}
