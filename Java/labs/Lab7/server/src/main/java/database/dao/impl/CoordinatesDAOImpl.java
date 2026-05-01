package database.dao.impl;

import common.models.Coordinates;
import database.builder.QueryBuilder;
import database.dao.CoordinatesDAO;
import database.mapper.CoordinatesRowMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoordinatesDAOImpl implements CoordinatesDAO {
    private final CoordinatesRowMapper mapper = new CoordinatesRowMapper();
    
    @Override
    public Coordinates findById(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id", "x", "y")
            .from("coordinates")
            .where("id = ?", id)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Coordinates coords = mapper.mapRow(rs);
            coords.setId(rs.getInt("id"));
            return coords;
        }
        return null;
    }
    
    @Override
    public List<Coordinates> findAll(Connection conn) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id", "x", "y")
            .from("coordinates")
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        List<Coordinates> list = new ArrayList<>();
        while (rs.next()) {
            Coordinates coords = mapper.mapRow(rs);
            coords.setId(rs.getInt("id"));
            list.add(coords);
        }
        return list;
    }
    
    @Override
    public void insert(Connection conn, Coordinates coords) throws SQLException {
        PreparedStatement stmt = QueryBuilder.insert("coordinates")
            .column("x", coords.getX())
            .column("y", coords.getY())
            .returning("id")
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            coords.setId(rs.getInt("id"));
        } else {
            throw new SQLException("Failed to insert coordinates: no ID returned");
        }
    }
    
    @Override
    public void update(Connection conn, Coordinates coords) throws SQLException {
        PreparedStatement stmt = QueryBuilder.update("coordinates")
            .set("x", coords.getX())
            .set("y", coords.getY())
            .where("id = ?", coords.getId())
            .build(conn);
        
        stmt.executeUpdate();
    }
    
    @Override
    public void delete(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.delete("coordinates")
            .where("id = ?", id)
            .build(conn);
        
        stmt.executeUpdate();
    }
    
    @Override
    public boolean exists(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id")
            .from("coordinates")
            .where("id = ?", id)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
    
    @Override
    public Coordinates findByXY(Connection conn, float x, long y) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id", "x", "y")
            .from("coordinates")
            .where("x = ? AND y = ?", x, y)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Coordinates coords = mapper.mapRow(rs);
            coords.setId(rs.getInt("id"));
            return coords;
        }
        return null;
    }
    
    @Override
    public int insertOrGet(Connection conn, Coordinates coords) throws SQLException {
        Coordinates existing = findByXY(conn, coords.getX(), coords.getY());
        if (existing != null) {
            return existing.getId();
        }
        
        insert(conn, coords);
        
        if (coords.getId() <= 0) {
            throw new SQLException("Failed to insert coordinates: no ID generated");
        }
        
        return coords.getId();
    }
}