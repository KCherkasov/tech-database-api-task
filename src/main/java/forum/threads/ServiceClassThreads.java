package forum.threads;

import forum.forums.visitors.QueryList;
import forum.misc.models.Vote;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static forum.rowmappers.ProjectRowMappers.POST_MAPPER;
import static forum.rowmappers.ProjectRowMappers.THREAD_MAPPER;

/**
 * @author KCherkasov, 2018
 */

@Service
public class ServiceClassThreads {
    private JdbcTemplate template;

    public ServiceClassThreads(@NotNull JdbcTemplate template) {
        this.template = template;
    }

    public Model createNewThread(@NotNull String slug, @NotNull Model newThread) {
        final Integer id = template.queryForObject(forum.threads.QueryList.CREATE_NEW_THREAD, Integer.class,
                newThread.getAuthor(), newThread.getAuthor(), newThread.getCreated(), slug,
                slug, newThread.getMessage(), newThread.getSlug(), newThread.getTitle());
        template.update(forum.threads.QueryList.UPDATE_THREADS_COUNT, 1, newThread.getForum());
        final Integer uId = template.queryForObject(forum.users.QueryList.GET_USER_ID_BY_NICKNAME,
                Integer.class, newThread.getAuthor());
        final Integer fId = template.queryForObject(forum.forums.QueryList.GET_FORUM_ID_BY_FSLUG, Integer.class, slug);
        template.update(QueryList.ADD_NEW_VISITOR, uId, fId);
        return template.queryForObject(forum.threads.QueryList.GET_THREAD_BY_ID, THREAD_MAPPER, id);
    }

    public void clearTable() {
        template.execute(forum.threads.QueryList.CLEAR_THREADS_TABLE);
    }

    public Integer tableStatus() {
        return template.queryForObject(forum.threads.QueryList.THREAD_TABLE_STATUS, Integer.class);
    }

    public Model getByTSlugOrId(@NotNull String tSlugOrId) {
        if (tSlugOrId.matches("\\d+")) {
            return template.queryForObject(forum.threads.QueryList.GET_THREAD_BY_ID,
                    THREAD_MAPPER, Integer.valueOf(tSlugOrId));
        }
        return template.queryForObject(forum.threads.QueryList.GET_THREAD_BY_SLUG,
                THREAD_MAPPER, tSlugOrId);
    }

    public Model updateThread(@NotNull String slugOrId, @NotNull OnUpdateModel threadUpdate) {
        final Model threadDB = getByTSlugOrId(slugOrId);

        Integer notNullFields = 0;

        if (threadUpdate.getMessage() != null) {
            ++notNullFields;
            threadDB.setMessage(threadUpdate.getMessage());
        }
        if (threadUpdate.getTitle() != null) {
            ++notNullFields;
            threadDB.setTitle(threadUpdate.getTitle());
        }

        if (notNullFields > 0) {
            template.update(forum.threads.QueryList.updateThread(), threadDB.getTitle(), threadDB.getMessage(), threadDB.getId());
        }

        return threadDB;
    }

    public List<forum.posts.Model> getThreadPosts(@NotNull String tSlugOrId, @Nullable Integer entriesCount,
                                                  @Nullable Integer startDate, @Nullable String sortKind,
                                                  @Nullable Boolean orderingKind) {
        final Model requestedThread = getByTSlugOrId(tSlugOrId);

        final List<Object> extraKeys = new ArrayList<>();
        extraKeys.add(requestedThread.getId());
        if (startDate != null) {
            extraKeys.add(startDate);
        }
        if (entriesCount != null) {
            extraKeys.add(entriesCount);
        }
        if (sortKind == null) {
            return template.query(forum.threads.QueryList.getThreadPostsFlatOrder(entriesCount, startDate, orderingKind),
                    extraKeys.toArray(), POST_MAPPER);
        }
        switch (sortKind) {
            case "flat":
                return template.query(forum.threads.QueryList.getThreadPostsFlatOrder(entriesCount, startDate, orderingKind),
                        extraKeys.toArray(), POST_MAPPER);
            case "tree":
                return template.query(forum.threads.QueryList.getThreadPostsTreeOrder(entriesCount, startDate, orderingKind),
                        extraKeys.toArray(), POST_MAPPER);
            case "parent_tree":
                return template.query(forum.threads.QueryList.getThreadPostsParentTreeOrder(entriesCount, startDate,
                        orderingKind), extraKeys.toArray(), POST_MAPPER);
            default:
                break;
        }
        return template.query(forum.threads.QueryList.getThreadPostsFlatOrder(entriesCount, startDate, orderingKind),
                extraKeys.toArray(), POST_MAPPER);
    }

    public Model voteThread(@NotNull String slugOrId, @NotNull Vote vote) {
        final Model votedThread = getByTSlugOrId(slugOrId);
        final Integer uId = template.queryForObject(forum.users.QueryList.GET_USER_ID_BY_NICKNAME,
                Integer.class, vote.getNickname());
        try {
            final Integer previousVote = template.queryForObject(forum.threads.QueryList.CHECK_PREVIOUS_VOTE,
                    Integer.class, uId, votedThread.getId());
            if (Objects.equals(previousVote, vote.getVoice())) {
                return votedThread;
            }
            template.update(forum.threads.QueryList.UPDATE_SINGLE_VOTE, vote.getVoice(), uId, votedThread.getId());
            template.update(forum.threads.QueryList.UPDATE_THREAD_VOTES,
                    2 * vote.getVoice(), votedThread.getId());
            votedThread.setVotes(votedThread.getVotes() + 2 * vote.getVoice());
        } catch (IncorrectResultSizeDataAccessException ex) {
            template.update(forum.threads.QueryList.ADD_VOTE, uId, votedThread.getId(), vote.getVoice());
            template.update(forum.threads.QueryList.UPDATE_THREAD_VOTES, vote.getVoice(), votedThread.getId());
            votedThread.setVotes(votedThread.getVotes() + vote.getVoice());
        }
        return votedThread;
    }
}
