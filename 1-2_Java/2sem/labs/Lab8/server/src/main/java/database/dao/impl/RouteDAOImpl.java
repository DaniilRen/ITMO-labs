package database.dao.impl;

import common.models.Route;
import database.builder.QueryBuilder;
import database.dao.RouteDAO;
import database.mapper.RouteRowMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RouteDAOImpl implements RouteDAO {
    private final RouteRowMapper mapper = new RouteRowMapper();
    private final CoordinatesDAOImpl coordinatesDAO = new CoordinatesDAOImpl();
    private final LocationFromDAOImpl locationFromDAO = new LocationFromDAOImpl();
    private final LocationToDAOImpl locationToDAO = new LocationToDAOImpl();
    
    @Override
    public Route findById(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select(
                "r.id", "r.name", "r.distance", "r.creation_date", "r.author",
                "c.x as coord_x", "c.y as coord_y",
                "l2.x as from_x", "l2.y as from_y", "l2.name as from_name",
                "l3.x as to_x", "l3.y as to_y", "l3.z as to_z", "l3.name as to_name")
            .from("route r")
            .join("JOIN coordinates c ON r.coordinates_id = c.id")
            .join("JOIN location2Dimension l2 ON r.from_location_id = l2.id")
            .join("JOIN location3Dimension l3 ON r.to_location_id = l3.id")
            .where("r.id = ?", id)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapper.mapRow(rs);
        }
        return null;
    }
    
    @Override
    public List<Route> findAll(Connection conn) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select(
                "r.id", "r.name", "r.distance", "r.creation_date", "r.author",
                "c.x as coord_x", "c.y as coord_y",
                "l2.x as from_x", "l2.y as from_y", "l2.name as from_name",
                "l3.x as to_x", "l3.y as to_y", "l3.z as to_z", "l3.name as to_name")
            .from("route r")
            .join("JOIN coordinates c ON r.coordinates_id = c.id")
            .join("JOIN location2Dimension l2 ON r.from_location_id = l2.id")
            .join("JOIN location3Dimension l3 ON r.to_location_id = l3.id")
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        List<Route> routes = new ArrayList<>();
        while (rs.next()) {
            routes.add(mapper.mapRow(rs));
        }
        return routes;
    }
    
    @Override
    public void insert(Connection conn, Route route) throws SQLException {
        int coordsId = coordinatesDAO.insertOrGet(conn, route.getCoordinates());
        int fromId = locationFromDAO.insertOrGet(conn, route.getLocationFrom());
        int toId = locationToDAO.insertOrGet(conn, route.getLocationTo());
        
        PreparedStatement stmt = QueryBuilder.insert("route")
            .column("name", route.getName())
            .column("distance", route.getDistance())
            .column("creation_date", route.getCreationDate())
            .column("coordinates_id", coordsId)
            .column("from_location_id", fromId)
            .column("to_location_id", toId)
            .column("author", route.getAuthor())
            .returning("id")
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            route.setId(rs.getInt("id"));
        }
    }
    
    @Override
    public void update(Connection conn, Route route) throws SQLException {
        PreparedStatement stmt = QueryBuilder.update("route")
            .set("name", route.getName())
            .set("distance", route.getDistance())
            .set("author", route.getAuthor())
            .where("id = ?", route.getId())
            .build(conn);
        
        stmt.executeUpdate();
    }
    
    @Override
    public void delete(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.delete("route")
            .where("id = ?", id)
            .build(conn);
        
        stmt.executeUpdate();
    }
    
    @Override
    public boolean exists(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id")
            .from("route")
            .where("id = ?", id)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
    
    @Override
    public List<Route> findByAuthor(Connection conn, String author) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select(
                "r.id", "r.name", "r.distance", "r.creation_date", "r.author",
                "c.x as coord_x", "c.y as coord_y",
                "l2.x as from_x", "l2.y as from_y", "l2.name as from_name",
                "l3.x as to_x", "l3.y as to_y", "l3.z as to_z", "l3.name as to_name")
            .from("route r")
            .join("JOIN coordinates c ON r.coordinates_id = c.id")
            .join("JOIN location2Dimension l2 ON r.from_location_id = l2.id")
            .join("JOIN location3Dimension l3 ON r.to_location_id = l3.id")
            .where("r.author = ?", author)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        List<Route> routes = new ArrayList<>();
        while (rs.next()) {
            routes.add(mapper.mapRow(rs));
        }
        return routes;
    }
    
    @Override
    public List<Route> findByDistanceGreaterThan(Connection conn, int distance) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select(
                "r.id", "r.name", "r.distance", "r.creation_date", "r.author",
                "c.x as coord_x", "c.y as coord_y",
                "l2.x as from_x", "l2.y as from_y", "l2.name as from_name",
                "l3.x as to_x", "l3.y as to_y", "l3.z as to_z", "l3.name as to_name")
            .from("route r")
            .join("JOIN coordinates c ON r.coordinates_id = c.id")
            .join("JOIN location2Dimension l2 ON r.from_location_id = l2.id")
            .join("JOIN location3Dimension l3 ON r.to_location_id = l3.id")
            .where("r.distance > ?", distance)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        List<Route> routes = new ArrayList<>();
        while (rs.next()) {
            routes.add(mapper.mapRow(rs));
        }
        return routes;
    }
}