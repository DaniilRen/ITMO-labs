package database.mapper;

import common.models.LocationTo;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationToMapper implements RowMapper<LocationTo> {
    
    @Override
    public LocationTo mapRow(ResultSet rs) throws SQLException {
        Double x = rs.getObject("x") != null ? rs.getDouble("x") : null;
        return new LocationTo(
            x,
            rs.getDouble("y"),
            rs.getInt("z"),
            rs.getString("name")
        );
    }
}