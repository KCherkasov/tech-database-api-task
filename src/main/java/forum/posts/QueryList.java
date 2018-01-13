package forum.posts;

/**
 * @author KCherkasov, 2018
 */

public class QueryList {
    public static final String CREATE_POST = "INSERT INTO posts (uid, uname, created, fid, fslug, id, message, "
            + "pid, tid, path, rid) VALUES (?, (SELECT nickname FROM users WHERE id = ?), ?::TIMESTAMPTZ, ?, "
            + "(SELECT slug FROM forums WHERE id = ?), ?, ?, ?, ?, array_append(?, ?::BIGINT), ?)";
    public static final String POST_TABLE_STATUS = "SELECT COUNT(*) FROM posts";
    public static final String CLEAR_POST_TABLE = "TRUNCATE posts CASCADE";
    public static final String UPDATE_POST = "UPDATE posts SET message = ?, is_edited = TRUE WHERE id = ?";
    public static final String UPDATE_POSTS_COUNT = "UPDATE forums SET posts = posts + ? WHERE id = ?";
    public static final String GET_POST_PATH = "SELECT path FROM posts WHERE id = ?";
    public static final String GET_POST_BY_ID =
            "SELECT uname AS nickname, created, fslug AS slug, id, is_edited, "
                    + "message, pid, tid FROM posts WHERE id = ?";
    public static final String GET_NEXT_AVAILABLE_POST_ID = "SELECT nextval(pg_get_serial_sequence('posts', 'id'))";
    public static final String CHECK_PARENT_POST_ID = "SELECT id FROM posts WHERE id = ? AND tid = ?";
}
