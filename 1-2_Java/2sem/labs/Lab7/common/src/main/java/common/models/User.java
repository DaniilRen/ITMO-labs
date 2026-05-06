package common.models;

import java.util.Objects;

/**
 * Представление пользователя в коллекции
 * @author Septyq
 */
public class User extends Entity {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private String password;
    
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
    
    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean validate() {
        return name != null && password != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User convertedObject = (User) o;
        return Objects.equals(name, convertedObject.getName()) 
            && Objects.equals(password, convertedObject.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }
}