package forum.threads;

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
        final Timestamp timestamp = rs.getTimestamp("created");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return new Model(rs.getString("author"), dateFormat.format(timestamp.getTime()),
                rs.getString("forum"), rs.getInt("id"),
                rs.getString("message"), rs.getString("slug"),
                rs.getString("title"), rs.getInt("votes"));
    }
}
