package core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
@FunctionalInterface
public interface PreparedStatementSetter {
    PreparedStatement values(PreparedStatement pstmt) throws SQLException;
}
