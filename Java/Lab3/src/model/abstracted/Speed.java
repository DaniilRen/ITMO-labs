package model.abstracted;

public record Speed(Distance distance, TimeUnit timeUnit) {
    @Override
    public String toString() {
        return distance.toString() + " в " + timeUnit.toString();
    }
}
