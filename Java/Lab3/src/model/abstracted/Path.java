package model.abstracted;
import model.objects.Planet;

public record Path(Distance distance, Planet startPoint, Planet endPoint) {}
