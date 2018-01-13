package forum.users;

/**
 * @author KCherkasov, 2018
 */

public class QueryList {
    public static final String CREATE_NEW_USER =
            "INSERT INTO users (about, email, fullname, nickname) VALUES (?, ?, ?, ?)";
    public static final String USERS_TABLE_STATUS = "SELECT COUNT(*) FROM users";
    public static final String CLEAR_USERS_TABLE = "TRUNCATE users CASCADE";
    public static final String GET_USER_BY_NICKNAME = "SELECT * FROM users WHERE LOWER(nickname) = LOWER(?)";
    public static final String GET_USER_BY_NICKNAME_OR_EMAIL =
            "SELECT * FROM users WHERE (LOWER(nickname) = LOWER(?) OR LOWER(email) = LOWER(?))";
    public static final String GET_USER_ID_BY_NICKNAME = "SELECT id FROM users WHERE LOWER(nickname) = LOWER(?)";
    public static final String UPDATE_USER =
            "UPDATE users SET about = ?, email = ?, fullname = ? WHERE LOWER(nickname) = LOWER(?)";
}
