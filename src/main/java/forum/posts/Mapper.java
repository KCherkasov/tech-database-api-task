package forum.posts;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author KCherkasov, 2018
 */

public class Mapper implements RowMapper<Model> {
    @Nullable
    @Override
    public Model mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        final Timestamp timestamp = rs.getTimestamp("created");
        return new Model(rs.getString("nickname"), dateFormat.format(timestamp.getTime()),
                rs.getString("slug"), rs.getInt("id"),
                rs.getBoolean("is_edited"), rs.getString("message"),
                rs.getInt("pid"), rs.getInt("tid"));
    }
}
