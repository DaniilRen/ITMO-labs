package model.abstracted;


public enum Distance {
    PLANET_DISTANCE("четыреста тысяч"), 
    ROCKET_DISTANCE_PER_SECOND("двенадцать");

    private final String distance;

    Distance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return distance + " километров";
    }
}