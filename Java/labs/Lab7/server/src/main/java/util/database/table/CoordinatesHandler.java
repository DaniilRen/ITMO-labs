package util.database.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.models.Coordinates;

/**
 * Определяет доступные операции с таблицой Coordinates
 * @author Septyq
 */
public class CoordinatesHandler extends TableHandler<Coordinates> {
    public CoordinatesHandler(Connection connection) {
        super(connection);
    }

    @Override
    public int getId(Coordinates object) {
        try (PreparedStatement query = prepareQuery(connection, "SELECT id FROM coordinates WHERE x = ? AND y = ?")) {
            query.setDouble(1, object.getX());
            query.setLong(2, object.getY());
            ResultSet result = query.executeQuery();
            if (!(result.next())) throw new SQLException("No such coordinates row");;
            return result.getInt("id");
        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public int insert(Coordinates object) throws SQLException {
        try (PreparedStatement query = prepareQuery(connection, "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id")) {
            query.setDouble(1, object.getX());
            query.setLong(2, object.getY());
            ResultSet rs = query.executeQuery();

            if (!(rs.next())) throw new SQLException("Error while adding entry");
            return rs.getInt("id");
        }
    }
}
