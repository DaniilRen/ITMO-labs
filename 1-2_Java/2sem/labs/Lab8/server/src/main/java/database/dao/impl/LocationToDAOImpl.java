package database.dao.impl;

import common.models.LocationTo;
import database.builder.QueryBuilder;
import database.dao.BaseDAO;
import database.mapper.LocationToMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationToDAOImpl implements BaseDAO<LocationTo, Integer> {
    private final LocationToMapper mapper = new LocationToMapper();
    
    @Override
    public LocationTo findById(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id", "x", "y", "z", "name")
            .from("location3Dimension")
            .where("id = ?", id)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            LocationTo location = mapper.mapRow(rs);
            location.setId(rs.getInt("id"));
            return location;
        }
        return null;
    }
    
    @Override
    public List<LocationTo> findAll(Connection conn) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id", "x", "y", "z", "name")
            .from("location3Dimension")
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        List<LocationTo> list = new ArrayList<>();
        while (rs.next()) {
            LocationTo location = mapper.mapRow(rs);
            location.setId(rs.getInt("id"));
            list.add(location);
        }
        return list;
    }
    
    @Override
    public void insert(Connection conn, LocationTo location) throws SQLException {
        PreparedStatement stmt = QueryBuilder.insert("location3Dimension")
            .column("x", location.getX())
            .column("y", location.getY())
            .column("z", location.getZ())
            .column("name", location.getName())
            .returning("id")
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            location.setId(rs.getInt("id"));
        } else {
            throw new SQLException("Failed to insert location3Dimension: no ID returned");
        }
    }
    
    @Override
    public void update(Connection conn, LocationTo location) throws SQLException {
        PreparedStatement stmt = QueryBuilder.update("location3Dimension")
            .set("x", location.getX())
            .set("y", location.getY())
            .set("z", location.getZ())
            .set("name", location.getName())
            .where("id = ?", location.getId())
            .build(conn);
        
        stmt.executeUpdate();
    }
    
    @Override
    public void delete(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.delete("location3Dimension")
            .where("id = ?", id)
            .build(conn);
        
        stmt.executeUpdate();
    }
    
    @Override
    public boolean exists(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id")
            .from("location3Dimension")
            .where("id = ?", id)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
    
    public LocationTo findByXYAndZAndName(Connection conn, Double x, double y, int z, String name) throws SQLException {
        PreparedStatement stmt;
        if (x == null) {
            stmt = QueryBuilder.select("id", "x", "y", "z", "name")
                .from("location3Dimension")
                .where("x IS NULL AND y = ? AND z = ? AND name = ?", y, z, name)
                .build(conn);
        } else {
            stmt = QueryBuilder.select("id", "x", "y", "z", "name")
                .from("location3Dimension")
                .where("x = ? AND y = ? AND z = ? AND name = ?", x, y, z, name)
                .build(conn);
        }
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            LocationTo location = mapper.mapRow(rs);
            location.setId(rs.getInt("id"));
            return location;
        }
        return null;
    }
    
    public int insertOrGet(Connection conn, LocationTo location) throws SQLException {
        LocationTo existing = findByXYAndZAndName(conn, location.getX(), location.getY(), location.getZ(), location.getName());
        if (existing != null) {
            return existing.getId();
        }
        
        insert(conn, location);
        
        if (location.getId() <= 0) {
            throw new SQLException("Failed to insert location3Dimension: no ID generated");
        }
        
        return location.getId();
    }
}