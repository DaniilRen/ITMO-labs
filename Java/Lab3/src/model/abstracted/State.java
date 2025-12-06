package model.abstracted;

public enum State {
    EATING("ест"),  
    SLEEPING("спит"),
    WALKING("гуляет");

    private String text;

    State(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
