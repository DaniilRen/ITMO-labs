public class Task11 {
    public static void main(String[] args) {
        System.out.println(convertNotation(730));
    }

    private static String convertNotation(int number) {
        StringBuilder result = new StringBuilder();
        while (Math.abs(number) > 0) {
            System.out.println(String.format("div: %d, mod: %d", number / -10, number % -10));
            result.append(Math.abs(number % (-10)));
            number = number / -10;
        }
        return result.reverse().toString();
    }
}