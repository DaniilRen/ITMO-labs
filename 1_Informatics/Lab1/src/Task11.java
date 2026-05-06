public class Task11 {
    public static String convertToBaseMinus10(int num) {
        if (num == 0) return "0";

        StringBuilder sb = new StringBuilder();
        int number = num;
        while (number != 0) {
            int remainder = number % (-10);
            number = number / (-10);

            if (remainder < 0) {
                remainder += 10;
                number += 1;
            }
            sb.append(remainder);
        }
        return sb.reverse().toString();
    }

    public static void main(String[] args) {
        int decimalNumber = 1937;
        String baseMinus10 = convertToBaseMinus10(decimalNumber);
        System.out.println("Число " + decimalNumber + " в системе с основанием -10: " + baseMinus10);
    }
}

