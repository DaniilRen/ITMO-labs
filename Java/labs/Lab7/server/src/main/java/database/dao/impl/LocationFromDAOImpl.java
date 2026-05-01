package database.dao.impl;

import common.models.LocationFrom;
import database.builder.QueryBuilder;
import database.dao.BaseDAO;
import database.mapper.LocationFromMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationFromDAOImpl implements BaseDAO<LocationFrom, Integer> {
    private final LocationFromMapper mapper = new LocationFromMapper();
    
    @Override
    public LocationFrom findById(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id", "x", "y", "name")
            .from("location2Dimension")
            .where("id = ?", id)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            LocationFrom location = mapper.mapRow(rs);
            location.setId(rs.getInt("id"));
            return location;
        }
        return null;
    }
    
    @Override
    public List<LocationFrom> findAll(Connection conn) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id", "x", "y", "name")
            .from("location2Dimension")
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        List<LocationFrom> list = new ArrayList<>();
        while (rs.next()) {
            LocationFrom location = mapper.mapRow(rs);
            location.setId(rs.getInt("id"));
            list.add(location);
        }
        return list;
    }
    
    @Override
    public void insert(Connection conn, LocationFrom location) throws SQLException {
        PreparedStatement stmt = QueryBuilder.insert("location2Dimension")
            .column("x", location.getX())
            .column("y", location.getY())
            .column("name", location.getName())
            .returning("id")
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            location.setId(rs.getInt("id"));
        } else {
            throw new SQLException("Failed to insert location2Dimension: no ID returned");
        }
    }
    
    @Override
    public void update(Connection conn, LocationFrom location) throws SQLException {
        PreparedStatement stmt = QueryBuilder.update("location2Dimension")
            .set("x", location.getX())
            .set("y", location.getY())
            .set("name", location.getName())
            .where("id = ?", location.getId())
            .build(conn);
        
        stmt.executeUpdate();
    }
    
    @Override
    public void delete(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.delete("location2Dimension")
            .where("id = ?", id)
            .build(conn);
        
        stmt.executeUpdate();
    }
    
    @Override
    public boolean exists(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id")
            .from("location2Dimension")
            .where("id = ?", id)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
    
    public LocationFrom findByXYAndName(Connection conn, int x, double y, String name) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id", "x", "y", "name")
            .from("location2Dimension")
            .where("x = ? AND y = ? AND name = ?", x, y, name)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            LocationFrom location = mapper.mapRow(rs);
            location.setId(rs.getInt("id"));
            return location;
        }
        return null;
    }
    
    public int insertOrGet(Connection conn, LocationFrom location) throws SQLException {
        LocationFrom existing = findByXYAndName(conn, location.getX(), location.getY(), location.getName());
        if (existing != null) {
            return existing.getId();
        }
        
        insert(conn, location);
        
        if (location.getId() <= 0) {
            throw new SQLException("Failed to insert location2Dimension: no ID generated");
        }
        
        return location.getId();
    }
}