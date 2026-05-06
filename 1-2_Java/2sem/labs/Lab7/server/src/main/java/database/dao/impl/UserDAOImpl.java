package database.dao.impl;

import common.models.User;
import database.builder.QueryBuilder;
import database.dao.UserDAO;
import database.mapper.UserMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private final UserMapper mapper = new UserMapper();
    
    @Override
    public User findById(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id", "name", "password")
            .from("users")
            .where("id = ?", id)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapper.mapRow(rs);
        }
        return null;
    }
    
    @Override
    public List<User> findAll(Connection conn) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id", "name", "password")
            .from("users")
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(mapper.mapRow(rs));
        }
        return users;
    }
    
    @Override
    public void insert(Connection conn, User user) throws SQLException {
        PreparedStatement stmt = QueryBuilder.insert("users")
            .column("name", user.getName())
            .column("password", user.getPassword())
            .returning("id")
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            user.setId(rs.getInt("id"));
        }
    }
    
    @Override
    public void update(Connection conn, User user) throws SQLException {
        PreparedStatement stmt = QueryBuilder.update("users")
            .set("name", user.getName())
            .set("password", user.getPassword())
            .where("id = ?", user.getId())
            .build(conn);
        
        stmt.executeUpdate();
    }
    
    @Override
    public void delete(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.delete("users")
            .where("id = ?", id)
            .build(conn);
        
        stmt.executeUpdate();
    }
    
    @Override
    public boolean exists(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id")
            .from("users")
            .where("id = ?", id)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
    
    @Override
    public User findByName(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id", "name", "password")
            .from("users")
            .where("name = ?", name)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapper.mapRow(rs);
        }
        return null;
    }
    
    @Override
    public boolean authenticate(Connection conn, String name, String password) throws SQLException {
        PreparedStatement stmt = QueryBuilder.select("id")
            .from("users")
            .where("name = ? AND password = ?", name, password)
            .build(conn);
        
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }
    
    @Override
    public void changePassword(Connection conn, int userId, String newPassword) throws SQLException {
        PreparedStatement stmt = QueryBuilder.update("users")
            .set("password", newPassword)
            .where("id = ?", userId)
            .build(conn);
        
        stmt.executeUpdate();
    }
}