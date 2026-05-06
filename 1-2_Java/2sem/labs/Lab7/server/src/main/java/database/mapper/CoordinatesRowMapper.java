package database.mapper;

import common.models.Coordinates;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CoordinatesRowMapper implements RowMapper<Coordinates> {
    
    @Override
    public Coordinates mapRow(ResultSet rs) throws SQLException {
        return new Coordinates(rs.getFloat("x"), rs.getLong("y"));
    }
}