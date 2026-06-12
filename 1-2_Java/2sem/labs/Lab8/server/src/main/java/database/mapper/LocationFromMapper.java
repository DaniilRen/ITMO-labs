package database.mapper;

import common.models.LocationFrom;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationFromMapper implements RowMapper<LocationFrom> {
    
    @Override
    public LocationFrom mapRow(ResultSet rs) throws SQLException {
        return new LocationFrom(
            rs.getInt("x"),
            rs.getDouble("y"),
            rs.getString("name")
        );
    }
}