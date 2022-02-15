package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public void update(String sql, PreparedStatementSetter pss) throws DataAccessException {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.values(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void executeUpdate(String sql, Object... parameters) {
        PreparedStatementSetter pss = createPreparedStatementSetter(parameters);
        this.update(sql,pss);
    }

    public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rm) throws DataAccessException {
        ResultSet rs = null;
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){
            pss.values(pstmt);
            rs = pstmt.executeQuery();
            List<T> result = new ArrayList<>();
            if (rs.next()) {
                result.add(rm.mapRow(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> T queryForObject(String sql, PreparedStatementSetter pss, RowMapper<T> rm) throws DataAccessException {
        List<T> result = query(sql,pss,rm);
        if(result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }


    public <T> T executeQuery(String sql, RowMapper<T> rm,  Object... parameters) throws DataAccessException {
        return this.queryForObject(sql,createPreparedStatementSetter(parameters),rm);
    }

    public <T> List<T> executeQueryForList(String sql, RowMapper<T> rm,  Object... parameters) throws DataAccessException {
        return this.query(sql,createPreparedStatementSetter(parameters),rm);
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object[] parameters) {
        return pstmt -> {
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setObject(i+1, parameters[i]);
            }
            return pstmt;
        };
    }
}
