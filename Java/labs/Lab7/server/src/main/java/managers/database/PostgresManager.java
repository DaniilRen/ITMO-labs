package managers.database;

import common.models.Coordinates;
import common.models.Entity;
import common.models.Location2Dimension;
import common.models.Location3Dimension;
import common.models.Route;
import util.database.api.DatabaseApi;
import common.exceptions.CollectionLoadException;
import common.exceptions.CollectionWriteException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Менеджер для операций с базой данных PostgreSQL
 * @author Septyq
 */
public class PostgresManager extends AbstractDatabaseManager {
    public PostgresManager(DatabaseApi api, String url, String user, String password) {
        super(api, url, user, password);
    }

    @Override
    public Collection<Entity> readCollection() throws CollectionLoadException {
        String queryMessage = """
            SELECT 
                r.id, r.name, r.distance, r.creation_date, r.author,
                c.x as coord_x, c.y as coord_y,
                l2.x as from_x, l2.y as from_y, l2.name as from_name,
                l3.x as to_x, l3.y as to_y, l3.z as to_z, l3.name as to_name
            FROM route r
            JOIN coordinates c ON r.coordinates_id = c.id
            JOIN location2Dimension l2 ON r.from_location_id = l2.id
            JOIN location3Dimension l3 ON r.to_location_id = l3.id
        """;
        
        List<Entity> routes = new ArrayList<>();
        
        try (
            PreparedStatement query = api.prepareQuery (
                api.getConnection(PostgreSQLurl, PostgreSQLuser, PostgreSQLpassword), 
                queryMessage
            );
            ) {
            ResultSet result = query.executeQuery();
            while (result.next()) {
                Route route = new Route(
                    result.getInt("id"),
                    result.getString("name"),
                    new Coordinates(result.getFloat("coord_x"), result.getLong("coord_y")),
                    result.getObject("creation_date", LocalDateTime.class),
                    new Location2Dimension(result.getInt("from_x"), result.getDouble("from_y"), result.getString("from_name")),
                    new Location3Dimension(result.getDouble("to_x"), result.getDouble("to_y"), result.getInt("to_z"), result.getString("to_name")),
                    result.getInt("distance"),
                    result.getString("author")
                );
                
                if (route.validate()) {
                    routes.add(route);
                }
            }
            
        } catch (SQLException e) {
            throw new CollectionLoadException("Failed to read collection: " + e.getMessage());
        }
        
        return routes;
    }
    
    @Override
    public void writeCollection(Collection<? extends Entity> collection) throws CollectionWriteException {
        try (Connection connection = api.getConnection(PostgreSQLurl, PostgreSQLuser, PostgreSQLpassword)) {
            connection.setAutoCommit(false);
            
            try {
                try (Statement statement = connection.createStatement()) {
                    statement.execute("DELETE FROM route");
                    statement.execute("DELETE FROM coordinates");
                    statement.execute("DELETE FROM location2Dimension");
                    statement.execute("DELETE FROM location3Dimension");
                }
                
                for (Entity entity : collection) {
                    if (entity instanceof Route) {
                        api.insertRoute(connection, (Route) entity);
                    } else {
                        throw new SQLException("entity should be Route instance");
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            throw new CollectionWriteException("Failed to write collection: " + e.getMessage());
        }
    }
}