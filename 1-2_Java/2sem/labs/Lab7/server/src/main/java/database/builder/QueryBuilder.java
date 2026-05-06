package database.builder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryBuilder {
    
    public static SelectBuilder select(String... columns) {
        return new SelectBuilder(columns);
    }
    
    public static InsertBuilder insert(String table) {
        return new InsertBuilder(table);
    }
    
    public static UpdateBuilder update(String table) {
        return new UpdateBuilder(table);
    }
    
    public static DeleteBuilder delete(String table) {
        return new DeleteBuilder(table);
    }
    
    public static class SelectBuilder {
        private final StringBuilder sql = new StringBuilder();
        private final List<Object> params = new ArrayList<>();
        
        public SelectBuilder(String... columns) {
            sql.append("SELECT ").append(String.join(", ", columns));
        }
        
        public SelectBuilder from(String table) {
            sql.append(" FROM ").append(table);
            return this;
        }
        
        public SelectBuilder join(String joinClause) {
            sql.append(" ").append(joinClause);
            return this;
        }
        
        public SelectBuilder where(String condition, Object... values) {
            sql.append(" WHERE ").append(condition);
            params.addAll(Arrays.asList(values));
            return this;
        }
        
        public SelectBuilder and(String condition, Object... values) {
            sql.append(" AND ").append(condition);
            params.addAll(Arrays.asList(values));
            return this;
        }
        
        public SelectBuilder or(String condition, Object... values) {
            sql.append(" OR ").append(condition);
            params.addAll(Arrays.asList(values));
            return this;
        }
        
        public SelectBuilder orderBy(String column, String direction) {
            sql.append(" ORDER BY ").append(column).append(" ").append(direction);
            return this;
        }
        
        public PreparedStatement build(Connection conn) throws SQLException {
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            return stmt;
        }
    }
    
    public static class InsertBuilder {
        private final String table;
        private final List<String> columns = new ArrayList<>();
        private final List<Object> values = new ArrayList<>();
        private String returning = null;
        
        public InsertBuilder(String table) {
            this.table = table;
        }
        
        public InsertBuilder column(String name, Object value) {
            columns.add(name);
            values.add(value);
            return this;
        }
        
        public InsertBuilder returning(String column) {
            this.returning = column;
            return this;
        }
        
        public PreparedStatement build(Connection conn) throws SQLException {
            StringBuilder sql = new StringBuilder("INSERT INTO ").append(table);
            sql.append(" (").append(String.join(", ", columns)).append(")");
            sql.append(" VALUES (").append("?,".repeat(columns.size()).replaceAll(",$", "")).append(")");
            if (returning != null) {
                sql.append(" RETURNING ").append(returning);
            }
            
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            return stmt;
        }
    }
    
    public static class UpdateBuilder {
        private final String table;
        private final List<String> sets = new ArrayList<>();
        private final List<Object> values = new ArrayList<>();
        private String where = null;
        private List<Object> whereParams = new ArrayList<>();
        
        public UpdateBuilder(String table) {
            this.table = table;
        }
        
        public UpdateBuilder set(String column, Object value) {
            sets.add(column + " = ?");
            values.add(value);
            return this;
        }
        
        public UpdateBuilder where(String condition, Object... params) {
            this.where = condition;
            this.whereParams = Arrays.asList(params);
            return this;
        }
        
        public PreparedStatement build(Connection conn) throws SQLException {
            StringBuilder sql = new StringBuilder("UPDATE ").append(table);
            sql.append(" SET ").append(String.join(", ", sets));
            if (where != null) {
                sql.append(" WHERE ").append(where);
            }
            
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            int index = 1;
            for (Object value : values) {
                stmt.setObject(index++, value);
            }
            for (Object param : whereParams) {
                stmt.setObject(index++, param);
            }
            return stmt;
        }
    }
    
    public static class DeleteBuilder {
        private final String table;
        private String where = null;
        private List<Object> whereParams = new ArrayList<>();
        
        public DeleteBuilder(String table) {
            this.table = table;
        }
        
        public DeleteBuilder where(String condition, Object... params) {
            this.where = condition;
            this.whereParams = Arrays.asList(params);
            return this;
        }
        
        public PreparedStatement build(Connection conn) throws SQLException {
            StringBuilder sql = new StringBuilder("DELETE FROM ").append(table);
            if (where != null) {
                sql.append(" WHERE ").append(where);
            }
            
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < whereParams.size(); i++) {
                stmt.setObject(i + 1, whereParams.get(i));
            }
            return stmt;
        }
    }
}