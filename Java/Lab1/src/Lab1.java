import java.util.Random;

public class Lab1 {
    public static void main(String[] args) {
        // Заполняем 1 массив
        int[] l = new int[15];
        for (int i=6; i<21; i++) {
            l[i-6] = i;
        }
        // Заполняем 2 массив
        double[] x = new double[10];
        Random rand = new Random();
        for (int i=0; i<10; i++) {
            x[i] = rand.nextDouble(-10.0d, 10.0d);
        } 
        // Заполняем 3 массив
        double[][] w = new double[15][10];
        for (int i=0; i<15; i++) {
            for (int j=0; j<10; j++) {
                w[i][j] = calcArrayElement(l[i], x[j]);
            }
        }
        // Выводим результат
        printArray(w);
    }

    // Определение элемента для 3 массива согласно заданию
    private static double calcArrayElement(int l, double x) {
        if (l == 20) {
            return Math.pow(
                0.5 / (Math.pow(Math.PI * Math.pow(x, (3/4-x / x)), 2)), 
                Math.tan(Math.log1p(Math.abs(x)))
            );
        }
        
        int[] check_sums = {8, 11, 13, 14, 17, 18, 19};
        for (int i: check_sums) {
            if (l == i) {
                return Math.pow(
                    (x + 1) / 2,
                    2
                );
            }
        }

        return Math.pow(
            Math.E, 
            Math.sin(Math.pow(Math.pow((x+1) / x, x), 1/3))  
        );
    }   

    // Вывод массива 
    private static void printArray(double[][] arr) {
        for (double[] i : arr) {
            for (double j : i) {
                System.out.format("%12.4f ", j);
            }
            System.out.println();
        }
    }
}
