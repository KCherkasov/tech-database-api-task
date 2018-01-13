package forum.posts;

import forum.forums.visitors.QueryList;
import forum.users.Model;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.sql.Array;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import static forum.rowmappers.ProjectRowMappers.*;

/**
 * @author KCherkasov, 2018
 */

@Service
public class ServiceClassPosts {
    private JdbcTemplate template;

    public ServiceClassPosts(@NotNull JdbcTemplate template) {
        this.template = template;
    }

    public void clearTable() {
        template.execute(forum.posts.QueryList.CLEAR_POST_TABLE);
    }

    public Integer tableStatus() {
        return template.queryForObject(forum.posts.QueryList.POST_TABLE_STATUS, Integer.class);
    }

    public List<forum.posts.Model> bulkCreate(@NotNull String fSlugOrId, @NotNull List<forum.posts.Model> postsToAdd)
            throws SQLException {
        final Integer tId = fSlugOrId.matches("\\d+") ?
                template.queryForObject(forum.threads.QueryList.CHECK_THREAD_EXISTENCE,
                        Integer.class, Integer.valueOf(fSlugOrId)) :
                template.queryForObject(forum.threads.QueryList.GET_THREAD_ID_BY_SLUG, Integer.class, fSlugOrId);
        if (postsToAdd.isEmpty()) {
            return postsToAdd;
        }
        final Integer fId = template.queryForObject(forum.threads.QueryList.getFIdByTSlugOrId(fSlugOrId),
                Integer.class, fSlugOrId);
        final String forumSlug = template.queryForObject(forum.forums.QueryList.GET_FSLUG_BY_FORUM_ID, String.class, fId);
        final String currentTime = ZonedDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        for (forum.posts.Model post : postsToAdd) {
            final Integer postId = template.queryForObject(forum.posts.QueryList.GET_NEXT_AVAILABLE_POST_ID, Integer.class);
            post.setId(postId);
            post.setForum(forumSlug);
            post.setThread(tId);
            post.setCreated(currentTime);

            final Integer uId = template.queryForObject(forum.users.QueryList.GET_USER_ID_BY_NICKNAME,
                    Integer.class, post.getAuthor());

            Integer pId = 0;
            if (post.getParent() != null && post.getParent() != 0) {
                try {
                    pId = template.queryForObject(forum.posts.QueryList.CHECK_PARENT_POST_ID,
                            Integer.class, post.getParent(), tId);
                } catch (IncorrectResultSizeDataAccessException e) {
                    //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
                    throw new NoSuchElementException("Parent not found!");
                }
            }

            post.setParent(pId);

            final Array array = (pId == 0) ? null
                    : template.queryForObject(forum.posts.QueryList.GET_POST_PATH, Array.class, pId);

            final Long root = (pId == 0) ? postId : ((Long[]) array.getArray())[0];
            template.update(forum.posts.QueryList.CREATE_POST, uId, uId, currentTime,
                    fId, fId, postId, post.getMessage(), pId, tId, array, postId, root);
            template.update(QueryList.ADD_NEW_VISITOR, uId, fId);
        }

        template.update(forum.posts.QueryList.UPDATE_POSTS_COUNT, postsToAdd.size(), fId);

        return postsToAdd;
    }

    public forum.posts.Model updatePost(@NotNull Integer id, @NotNull OnUpdateModel postUpdateData) {
        final forum.posts.Model updatedPost =
                template.queryForObject(forum.posts.QueryList.GET_POST_BY_ID, POST_MAPPER, id);
        if (postUpdateData.getMessage() == null || postUpdateData.getMessage().equals(updatedPost.getMessage())) {
            return updatedPost;
        }
        template.update(forum.posts.QueryList.UPDATE_POST, postUpdateData.getMessage(), id);
        updatedPost.setIsEdited(Boolean.TRUE);
        updatedPost.setMessage(postUpdateData.getMessage());
        return updatedPost;
    }

    public ExtendedModel getExtendedPostDataById(@NotNull Integer id, @Nullable List<String> extraKeys) {
        final forum.posts.Model post = template.queryForObject(forum.posts.QueryList.GET_POST_BY_ID, POST_MAPPER, id);
        final ExtendedModel extendedPostRequested =
                new ExtendedModel(null, null, null, null);
        extendedPostRequested.setPost(post);
        if (extraKeys != null) {
            for (String key : extraKeys) {
                switch (key) {
                    case "thread":
                        final forum.threads.Model thread = template.queryForObject(forum.threads.QueryList.GET_THREAD_BY_ID,
                                THREAD_MAPPER, post.getThread());
                        extendedPostRequested.setThread(thread);
                        break;
                    case "forum":
                        final forum.forums.Model forumModel = template.queryForObject(forum.forums.QueryList.GET_FORUM_BY_FSLUG,
                                FORUM_MAPPER, post.getForum());
                        extendedPostRequested.setForum(forumModel);
                        break;
                    case "user":
                        final Model author = template.queryForObject(forum.users.QueryList.GET_USER_BY_NICKNAME,
                                USER_MAPPER, post.getAuthor());
                        extendedPostRequested.setAuthor(author);
                        break;
                    default:
                        break;
                }
            }
        }
        return extendedPostRequested;
    }
}
