package forum.forums;

import org.springframework.lang.Nullable;

/**
 * @author KCherkasov, 2018
 */

public class QueryList {
    public static final String CREATE_NEW_FORUM = "INSERT INTO forums (slug, title, uid, uname) " +
            "VALUES (?, ?, (SELECT id FROM users WHERE LOWER(nickname) = LOWER(?))," +
            "(SELECT nickname FROM users WHERE LOWER(nickname) = LOWER(?)))";
    public static final String GET_FORUM_BY_FSLUG = "SELECT f.posts, f.slug, f.threads, " +
            "f.title, uname AS user FROM forums f WHERE LOWER(f.slug) = LOWER(?)";
    public static final String FORUM_TABLE_STATUS = "SELECT COUNT(*) FROM forums";
    public static final String CLEAR_FORUM_TABLE = "TRUNCATE forums CASCADE";
    public static final String GET_FSLUG_BY_FORUM_ID = "SELECT slug FROM forums WHERE id = ?";
    public static final String GET_FORUM_ID_BY_FSLUG = "SELECT id FROM forums WHERE LOWER(slug) = LOWER(?)";

    public static String getForumUsers(@Nullable Integer entriesCount, @Nullable String startNickname,
                                       @Nullable Boolean orderingKind) {
        final String ordering = (orderingKind != null && orderingKind ? "DESC " : "ASC ");
        final String comparison = (orderingKind != null && orderingKind ? "< " : "> ");

        final StringBuilder query = new StringBuilder("SELECT about, email, fullname, nickname ");
        query.append("FROM users u JOIN forum_visitors fv ON (u.id = fv.uid) WHERE fv.fid = ? ");
        if (startNickname != null) {
            query.append(" AND nickname COLLATE \"ucs_basic\" ").append(comparison).append('\'')
                    .append(String.valueOf(startNickname)).append("' COLLATE \"ucs_basic\" ");
        }
        query.append(" ORDER BY nickname COLLATE \"ucs_basic\" ").append(ordering);
        if (entriesCount != null) {
            query.append("LIMIT ").append(String.valueOf(entriesCount));
        }
        return query.toString();
    }

    public static String getForumThreads(@Nullable Integer entriesCount, @Nullable String startDate,
                                         @Nullable Boolean orderingKind) {
        final StringBuilder query = new StringBuilder(
                "SELECT uname AS author, created, fslug AS forum, ");
        query.append("id, message, slug, title, votes FROM threads WHERE LOWER(fslug) = LOWER(?) ");

        final String comparison = (orderingKind != null && orderingKind ? "<= " : ">= ");
        if (startDate != null) {
            query.append("AND created ").append(comparison).append("?::TIMESTAMPTZ ");
        }

        query.append("ORDER BY created ");
        if (orderingKind != null && orderingKind) {
            query.append("DESC ");
        }

        if (entriesCount != null) {
            query.append("LIMIT ? ");
        }

        return query.toString();
    }
}
