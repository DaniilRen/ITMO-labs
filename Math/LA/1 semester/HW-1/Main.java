import plainEquations.Plain;
import plainEquations.Point;
import java.lang.Math;

public class Main {
    public static void main(String[] args) {
        Point[] testPoints = {
            new Point(1.25, 1.25, 1.25), 
            new Point(1., 1.6, 1.), 
            new Point(2., 0.2, 1.8), 
            new Point(1., 1., 1.), 
            new Point(0., 2., 3.)
        };
     
        check(testPoints);
    }


    public static void check(Point[] testPoints) {
        Point A = new Point(1., 1., 1.);
        Point B = new Point(2., 2., 0.);
        Point C = new Point(0., 2., 2.);
        Point D = new Point(2., 0., 2.);
        
        Plain ABC = new Plain(D, point -> Math.signum(point.x() + point.z() - 2)); 
        Plain ACD = new Plain(B, point -> Math.signum(point.x() + point.y() - 2)); 
        Plain ABD = new Plain(C, point -> Math.signum(- point.y() - point.z() + 2)); 
        Plain BCD = new Plain(A, point -> Math.signum(point.x() + point.y() + point.z() - 4)); 
        
        for (Point point: testPoints) {
            System.out.println(point);
            System.out.println("ABC: " + ABC.check(point));
            System.out.println("ACD: " + ACD.check(point));
            System.out.println("ABD: " + ABD.check(point));
            System.out.println("BCD: " + BCD.check(point));
            System.out.println((ABC.check(point) && ACD.check(point) && ABD.check(point) && BCD.check(point) ? "точка внутри" : "точка снаружи"));
            System.out.println("---");
        }
    }
}
