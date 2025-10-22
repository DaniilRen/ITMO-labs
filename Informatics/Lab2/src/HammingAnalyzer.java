import java.util.Scanner;
import java.util.Arrays;

public class HammingAnalyzer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter message (7 bits length):");
        String messageBits = scanner.nextLine();
        scanner.close();

		if (messageBits.length() > 7) {
			System.out.println("Type correct message");
			System.exit(0);
		}

		System.err.println("<-----------/");

        boolean[] booleanMessage = getBoolenArray(messageBits);
        boolean[] parityBits = getParityBits(booleanMessage);

		String msg = getBooleanString(booleanMessage);
        if (Arrays.equals(parityBits, new boolean[] {false, false, false})) {
			System.err.println("No errors");
			System.out.println(String.format("%s%s%s%s", msg.charAt(2), msg.charAt(4), msg.charAt(5), msg.charAt(6)));
        } else {
            short badBitPosition = getBadBitPosition(parityBits);

            if (badBitPosition >= 0 && badBitPosition < booleanMessage.length) {
                booleanMessage[badBitPosition] = !booleanMessage[badBitPosition];
            }

            String[] BIT_NAMES = {"r1", "r2", "i1", "r3", "i2", "i3", "i4"};
            String badBitName = badBitPosition >= 0 && badBitPosition < BIT_NAMES.length
                ? BIT_NAMES[badBitPosition]
                : "unknown";
			System.out.println(String.format("%s%s%s%s", msg.charAt(2), msg.charAt(4), msg.charAt(5), msg.charAt(6)));
            System.out.printf("Bad bit: %s\n", badBitName);
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
        parityBits[0] = message[0] ^ message[2] ^ message[4] ^ message[6];
        parityBits[1] = message[1] ^ message[2] ^ message[5] ^ message[6];
        parityBits[2] = message[3] ^ message[4] ^ message[5] ^ message[6];
        return parityBits;
    }

    private static short getBadBitPosition(boolean[] parityBits) {
        short position = 0;
        for (int i = 2; i >= 0; i--) {
            position <<= 1;
            if (parityBits[i]) {
                position |= 1;
            }
        }
        return (short)(position - 1); 
    }
}
