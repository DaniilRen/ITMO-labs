package util.database.handlers.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.models.Location3Dimension;

/**
 * Определяет доступные операции с таблицой LocationTo
 * @author Septyq
 */
public class LocationToHandler extends TableHandler<Location3Dimension> {
    public LocationToHandler(Connection connection) {
        super(connection);
    }

    @Override
    public int getId(Location3Dimension object) {
        try (PreparedStatement query = prepareQuery(connection, "SELECT id FROM coordinates WHERE x = ? AND y = ?")) {
            query.setDouble(1, object.getX());
            query.setDouble(2, object.getY());
            query.setInt(3, object.getZ());
            ResultSet result = query.executeQuery();
            if (!(result.next())) throw new SQLException("No such location To row");;
            return result.getInt("id");
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public int insert(Location3Dimension object) throws SQLException {
        try (PreparedStatement query = prepareQuery(connection, "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id")) {
            query.setDouble(1, object.getX());
            query.setDouble(2, object.getY());
            query.setInt(3, object.getZ());
            ResultSet rs = query.executeQuery();

            if (!(rs.next())) throw new SQLException("Error while adding entry");
            return rs.getInt("id");
        }
    }
}