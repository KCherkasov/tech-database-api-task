package forum.forums;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author KCherkasov, 2018
 */

public class Mapper implements RowMapper<Model> {
    @Nullable
    @Override
    public Model mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
        return new Model(rs.getInt("posts"), rs.getString("slug"),
                rs.getInt("threads"), rs.getString("title"),
                rs.getString("user"));
    }
}
