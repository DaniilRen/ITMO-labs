package model.abstracted;

public abstract class Person implements Character, Lookable {
    protected String name;
    protected Feeling feeling;
    protected Location location;


    public Person(String name) {
        this.name = name;
    }

    public void setFealing(Feeling feeling) {
        this.feeling = feeling;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public abstract void describeFeeling();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}