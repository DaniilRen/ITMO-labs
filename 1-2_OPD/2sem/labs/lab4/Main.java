import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        findODZ();
    }

    private static void findODZ() {
        List<List<Integer>> allValues = new ArrayList<>();

        for (int x = -32768; x < 32768; x++) {
            for (int y = -32768; y < 32768; y++) {
                for (int z = -32768; z < 32768; z++) {
                    List<Integer> result = R(x, y, z);  
                    if (checkR((int) result.get(3))) {
                        allValues.add(result);
                    }
                }
            }
        }

   

        allValues.sort(new MyComparator());
        System.out.println(allValues.get(0));
        System.out.println(allValues.get(-1));
    }

    private static boolean checkR(int r) {
        return (r >= -Math.pow(2, 15) && r <= Math.pow(2, 15) - 1);
    }

    private static List<Integer> R(int x, int y, int z) {
        // System.out.println(String.format("X=%s, Y=%s, Z=%s", x, y, z));
        int r = F(y) - F(x - 1) + F(z) -1;
        return new ArrayList<Integer>(List.of(x, y, z, r));
    }

    private static int F(int x) {
        if (2726 <= x && x < 0) {
            return -2726;
        }
        return 5*x + 77;
    }

    static class MyComparator implements Comparator<List<Integer>> {
        @Override
        public int compare(List<Integer> var1, List<Integer> var2) {
            if  (var1.get(3) > var2.get(3)) return 1;
            if  (var1.get(3) == var2.get(3)) return 0;
            return -1;
        };
    }
}
