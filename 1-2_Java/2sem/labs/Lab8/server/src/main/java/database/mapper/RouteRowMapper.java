package database.mapper;

import common.models.Coordinates;
import common.models.LocationFrom;
import common.models.LocationTo;
import common.models.Route;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class RouteRowMapper implements RowMapper<Route> {
    
    @Override
    public Route mapRow(ResultSet rs) throws SQLException {
        return new Route(
            rs.getInt("id"),
            rs.getString("name"),
            new Coordinates(rs.getFloat("coord_x"), rs.getLong("coord_y")),
            rs.getObject("creation_date", LocalDateTime.class),
            new LocationFrom(rs.getInt("from_x"), rs.getDouble("from_y"), rs.getString("from_name")),
            new LocationTo(rs.getDouble("to_x"), rs.getDouble("to_y"), rs.getInt("to_z"), rs.getString("to_name")),
            rs.getInt("distance"),
            rs.getString("author")
        );
    }
}