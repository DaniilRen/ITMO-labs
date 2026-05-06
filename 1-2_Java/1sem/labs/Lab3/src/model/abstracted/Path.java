package model.abstracted;
import model.abstracted.enums.Distance;
import model.abstracted.enums.DistanceDescription;
import model.objects.Planet;

public record Path(Distance distance, Planet startPoint, Planet endPoint, DistanceDescription distanceDescription) {
    public Path(Distance distance, Planet startPoint, Planet endPoint) {
        this(distance, startPoint, endPoint, 
            distance.toInteger() > 100000 ? 
                DistanceDescription.HUGE :
                DistanceDescription.LITTLE
        );
    }

    @Override
    public String toString() {
        return "расстояние от " + this.startPoint().name() +
         " до " + this.endPoint().name() + " " + this.distanceDescription.toString() + " - около " +
         this.distance.toString();
    }
}
