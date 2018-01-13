package forum.rowmappers;

import forum.users.Mapper;
import forum.users.Model;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author KCherkasov, 2018
 */

public class ProjectRowMappers {
    public static final RowMapper<Model> USER_MAPPER = new Mapper();
    public static final RowMapper<forum.forums.Model> FORUM_MAPPER = new forum.forums.Mapper();
    public static final RowMapper<forum.threads.Model> THREAD_MAPPER = new forum.threads.Mapper();
    public static final RowMapper<forum.posts.Model> POST_MAPPER = new forum.posts.Mapper();
}
