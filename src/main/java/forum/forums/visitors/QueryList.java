package forum.forums.visitors;

/**
 * @author KCherkasov, 2018
 */

public class QueryList {
    public static final String ADD_NEW_VISITOR =
            "INSERT INTO forum_visitors (uid, fid) VALUES (?, ?) ON CONFLICT (uid, fid) DO NOTHING";
    public static final String CLEAR_TABLE = "TRUNCATE forum_visitors CASCADE";
}