package util.database.handlers.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Определяет доступные операции с таблицой
 * @author Septyq
 */
public abstract class TableHandler<T> {
    protected final Connection connection;

    public TableHandler(Connection connection) {
        this.connection = connection;
    }

    protected PreparedStatement prepareQuery(Connection connection, String queryMessage) throws SQLException {
        return connection.prepareStatement(queryMessage);
    }

    abstract public int getId(T object);

    abstract public int insert(T object) throws SQLException;
}
