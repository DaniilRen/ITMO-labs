package model.abstracted;
import model.abstracted.enums.Distance;
import model.abstracted.enums.Size;
import model.objects.Planet;

public record Path(Distance distance, Planet startPoint, Planet endPoint, Size distanceLength) {
    public Path(Distance distance, Planet startPoint, Planet endPoint, Size distanceLength) {
        this.distance = distance;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.distanceLength = distanceLength;
    }   

    public Path(Distance distance, Planet startPoint, Planet endPoint) {
        this(distance, startPoint, endPoint, distance.toInteger() > 100000 ? Size.HUGE : Size.NOT_SO_BIG);
    }

    public void describe() {
        System.out.println("расстояние от " + this.startPoint().name() +
         " до " + this.endPoint().name() + " " + this.distanceLength.toString() + " -- около " +
         this.distance.toString());
    }
}
