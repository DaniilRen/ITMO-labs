package model.abstracted;

import model.abstracted.enums.Distance;
import model.abstracted.enums.TimeUnit;

public record Speed(Distance distance, TimeUnit timeUnit) {
    @Override
    public String toString() {
        return distance.toString() + " в " + timeUnit.toString();
    }
}
