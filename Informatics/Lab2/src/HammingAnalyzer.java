import java.util.Scanner;

public class HammingAnalyzer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter message bits:");
        String messageBits = scanner.nextLine();
        scanner.close();

        boolean[] booleanMessage = getBoolenArray(messageBits);
        boolean[] parityBits = getParityBits(booleanMessage);
 
        if (parityBits.equals(new boolean[] {false ,false, false})) {
            System.out.println(getBooleanString(booleanMessage));
        } else {
            short badBitPosition = getBadBitPosition(parityBits);
            booleanMessage[badBitPosition] = !booleanMessage[badBitPosition];

            String[] BIT_NAMES = {"r1", "r2", "i1", "r3", "i2", "i3", "i4"};
            String badBitName = BIT_NAMES[badBitPosition];

            System.out.printf("bad bit name: %s\n", badBitName);

            System.out.printf("Result: %s\n", getBooleanString(booleanMessage));
        }
    }

    private static boolean[] getBoolenArray(String booleanString) {
        String[] stringBooleans = booleanString.split("");
        boolean[] booleanArray = new boolean[stringBooleans.length];
        for (int i = 0; i < stringBooleans.length; i++) {
            booleanArray[i] = stringBooleans[i].equals("1");
        }
        return booleanArray;
    }

    private static String getBooleanString(boolean[] booleanArray) {
        StringBuilder sb = new StringBuilder();
        for (boolean b : booleanArray) {
            sb.append(b ? '1' : '0');
        }
        return sb.toString();
    }

    private static boolean[] getParityBits(boolean[] message) {
        boolean[] parityBits = new boolean[3];
        parityBits[2] = message[0] ^ message[2] ^ message[4] ^ message[6];
        parityBits[1] = message[1] ^ message[2] ^ message[5] ^ message[6];
        parityBits[0] = message[3] ^ message[4] ^ message[5] ^ message[6];
        return parityBits;
    }

    private static short getBadBitPosition(boolean[] parityBits) {
        short position = 0;
        for (int i = 0; i < parityBits.length; i++) {
            position <<= 1; 
            if (parityBits[i]) {
                position |= 1; 
            }
        }
        position -= 1;
        return position;
    }

}


