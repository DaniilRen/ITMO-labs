package util.database.handlers.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.models.Location2Dimension;

/**
 * Определяет доступные операции с таблицой LocationFrom
 * @author Septyq
 */
public class LocationFromHandler extends TableHandler<Location2Dimension> {
    public LocationFromHandler(Connection connection) {
        super(connection);
    }

    @Override
    public int getId(Location2Dimension object) {
        try (PreparedStatement query = prepareQuery(connection, "SELECT id FROM coordinates WHERE x = ? AND y = ?")) {
            query.setInt(1, object.getX());
            query.setDouble(2, object.getY());
            query.setString(3, object.getName());
            ResultSet result = query.executeQuery();
            if (!(result.next())) throw new SQLException("No such location From row");;
            return result.getInt("id");
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public int insert(Location2Dimension object) throws SQLException {
        try (PreparedStatement query = prepareQuery(connection, "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id")) {
            query.setInt(1, object.getX());
            query.setDouble(2, object.getY());
            query.setString(3, object.getName());
            ResultSet rs = query.executeQuery();

            if (!(rs.next())) throw new SQLException("Error while adding entry");
            return rs.getInt("id");
        }
    }
}
