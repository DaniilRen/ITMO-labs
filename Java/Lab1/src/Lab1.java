import java.util.Random;
import java.lang.Math

public class Lab1 {
    public static void main(String[] args) {
        // Заполняем 1 массив
        int[] n = new int[15];
        for (int i=6; i<21; i++) {
            n[i-6] = i;
        }
        // Заполняем 2 массив
        double[] x = new double[10];
        Random rand = new Random();
        for (int i=0; i<10; i++) {
            x[i] = rand.nextDouble(-10.0d, 10.0d);
            System.out.println(x[i]);
        } 
        // Заполняем 3 массив
        double[][] w = new double[15][10];
        for (int i=0; i<15; i++) {
            for (int j=0; j<10; j++) {
                w[i][j] = calcArrayElement(n[i], x[j]);
            }
        }
        // Выводим результат
        printMatrix(w);
    }

    // Определение элемента для 3 массива согласно заданию
    private static double calcArrayElement(int n, double x) {
        switch(n) {
            case 20:
                 return Math.pow(
                    0.5 / (Math.pow(Math.PI * Math.pow(x, (3./4-x / x)), 2)), 
                    Math.tan(Math.log1p(Math.abs(x)))
                );
            
            case 8, 11, 13, 14, 17, 18, 19:
                return Math.pow(Math.asin(Math.sin((x)) + 1) / 2, 2);

            default:
                return Math.pow(Math.E, Math.sin(Math.pow(Math.pow((x+1) / x, x), 1./3)));
        }

    }   

    // Вывод матрицы
    private static void printMatrix(double[][] arr) {
        for (double[] i : arr) {
            for (double j : i) {
                System.out.format("%12.4f ", j);
            }
            System.out.println();
        }
    }
}
