package forum.threads;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * @author KCherkasov, 2018
 */

public class QueryList {
    public static final String CREATE_NEW_THREAD =
            "INSERT INTO threads (uid, uname, created, fid, fslug, message, slug, title) " +
                    "VALUES ((SELECT id FROM users WHERE nickname = ?), ?, "
                    + "COALESCE(?::TIMESTAMPTZ, CURRENT_TIMESTAMP), (SELECT id FROM forums WHERE "
                    + "LOWER(slug) = LOWER(?)), (SELECT slug FROM forums WHERE "
                    + "LOWER(slug) = LOWER(?)), ?, ?, ?) RETURNING id";
    public static final String THREAD_TABLE_STATUS = "SELECT COUNT(*) FROM threads";
    public static final String CLEAR_THREADS_TABLE = "TRUNCATE threads CASCADE";
    public static final String GET_THREAD_BY_ID = "SELECT t.uname AS author, t.created , fslug AS forum, t.id, " +
            "t.message AS message, t.slug AS slug, t.title, t.votes FROM threads t WHERE t.id = ?";
    public static final String GET_THREAD_BY_SLUG = "SELECT t.uname AS author, t.created , fslug AS forum, t.id," +
            "t.message AS message, t.slug AS slug, t.title, t.votes FROM threads t WHERE LOWER(t.slug) = LOWER(?)";
    public static final String GET_THREAD_ID_BY_SLUG = "SELECT id FROM threads WHERE LOWER(slug) = LOWER(?)";
    public static final String UPDATE_THREAD_VOTES = "UPDATE threads SET votes = votes + ? WHERE id = ?";
    public static final String UPDATE_SINGLE_VOTE = "UPDATE votes SET voice = ? WHERE uid = ? AND tid = ?";
    public static final String UPDATE_THREADS_COUNT =
            "UPDATE forums SET threads = threads + ? WHERE LOWER(slug) = LOWER(?)";
    public static final String CHECK_PREVIOUS_VOTE = "SELECT voice FROM votes WHERE uid = ? AND tid = ?";
    public static final String CHECK_THREAD_EXISTENCE = "SELECT id FROM threads WHERE id = ?";
    public static final String ADD_VOTE = "INSERT INTO votes (uid, tid, voice) VALUES (?, ?, ?)";

    public static String updateThread() {
        return "UPDATE threads SET title = ?, message = ? WHERE id = ?";
    }

    public static String getThreadPostsFlatOrder(@Nullable Integer entriesCount, @Nullable Integer startPostId,
                                                 @Nullable Boolean orderingKind) {
        final StringBuilder query = new StringBuilder(
                "SELECT p.uname AS nickname, p.created, p.fslug AS slug, p.id, p.is_edited,");
        query.append(" p.message, p.pid, p.tid FROM posts p WHERE p.tid = ? ");
        final String comparison = (orderingKind != null && orderingKind ? " < " : " > ");
        final String ordering = (orderingKind != null && orderingKind ? " DESC " : " ASC ");

        if (startPostId != null) {
            query.append(" AND p.id").append(comparison).append("? ");
        }
        query.append("ORDER BY p.id ").append(ordering);

        if (entriesCount != null) {
            query.append("LIMIT ?");
        }

        return query.toString();
    }

    public static String getThreadPostsTreeOrder(@Nullable Integer entriesCount, @Nullable Integer startPostId,
                                                 @Nullable Boolean orderingKind) {
        final StringBuilder query = new StringBuilder(
                "SELECT p.uname AS nickname, p.created, p.fslug AS slug, p.id, p.is_edited,");
        query.append(" p.message, p.pid, p.tid FROM posts p WHERE p.tid = ? ");
        final String comparison = (orderingKind != null && orderingKind ? " < " : " > ");
        final String ordering = (orderingKind != null && orderingKind ? " DESC " : " ASC ");

        if (startPostId != null) {
            query.append(" AND p.path ").append(comparison).append("(SELECT path FROM posts WHERE id = ?) ");
        }
        query.append("ORDER BY p.path ").append(ordering);

        if (entriesCount != null) {
            query.append("LIMIT ?");
        }

        return query.toString();
    }

    public static String getThreadPostsParentTreeOrder(@Nullable Integer entriesCount, @Nullable Integer startPostId,
                                                       @Nullable Boolean orderingKind) {
        final String comparison = (orderingKind != null && orderingKind ? " < " : " > ");
        final String ordering = (orderingKind != null && orderingKind ? " DESC " : " ASC ");
        final StringBuilder query = new StringBuilder(
                "SELECT p.uname AS nickname, p.created, p.fslug AS slug, p.id, p.is_edited,");
        query.append(" p.message, p.pid, p.tid FROM posts p ");
        query.append("WHERE p.rid IN (SELECT id FROM posts WHERE tid = ? AND pid=0 ");

        if (startPostId != null) {
            query.append(" AND path ").append(comparison).append("(SELECT path FROM posts WHERE id = ?) ");
        }
        query.append("ORDER BY id ").append(ordering);

        if (entriesCount != null) {
            query.append(" LIMIT ?");
        }
        query.append(") ");

        query.append("ORDER BY p.path ").append(ordering);

        return query.toString();
    }

    public static String getFIdByTSlugOrId(@NotNull String tSlugOrId) {
        if (tSlugOrId.matches("\\d+")) {
            return "SELECT f.id FROM threads t JOIN forums f ON (f.id = t.fid) WHERE t.id = ?::INTEGER";
        } else {
            return "SELECT f.id FROM threads t JOIN forums f ON (f.id = t.fid) WHERE LOWER(t.slug) = LOWER(?)";
        }
    }
}
