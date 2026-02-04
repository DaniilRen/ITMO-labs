package plainEquations;

import java.util.function.Function;

public class Plain {
    Point oppositeVertex;
    Function<Point, Double> getSign;

    public Plain(Point oppositeVertex, Function<Point, Double> getSign) {
        this.oppositeVertex = oppositeVertex;
        this.getSign = getSign;
    }

    public boolean check(Point checkPoint) {
        Double sign1 = this.getSign.apply(this.oppositeVertex);
        Double sign2 = this.getSign.apply(checkPoint);
        return sign1.equals(sign2) || sign2 == 0;
    }
} 
