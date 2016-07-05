package com.dixin.util;

public class SecurityUtil {
	/**
	 * code for 64 radix encoding.
	 */
	private static char[] code = {// 64 chars for encoding
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
			'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B',
			'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', '-', '_' };

	/**
	 * get coresponding value that the specific code represents.
	 * 
	 * @param code
	 * @return
	 */
	private static int getValue(char code) {
		switch (code) {
		case 'a':
			return 0;
		case 'b':
			return 1;
		case 'c':
			return 2;
		case 'd':
			return 3;
		case 'e':
			return 4;
		case 'f':
			return 5;
		case 'g':
			return 6;
		case 'h':
			return 7;
		case 'i':
			return 8;
		case 'j':
			return 9;
		case 'k':
			return 10;
		case 'l':
			return 11;
		case 'm':
			return 12;
		case 'n':
			return 13;
		case 'o':
			return 14;
		case 'p':
			return 15;
		case 'q':
			return 16;
		case 'r':
			return 17;
		case 's':
			return 18;
		case 't':
			return 19;
		case 'u':
			return 20;
		case 'v':
			return 21;
		case 'w':
			return 22;
		case 'x':
			return 23;
		case 'y':
			return 24;
		case 'z':
			return 25;
		case 'A':
			return 26;
		case 'B':
			return 27;
		case 'C':
			return 28;
		case 'D':
			return 29;
		case 'E':
			return 30;
		case 'F':
			return 31;
		case 'G':
			return 32;
		case 'H':
			return 33;
		case 'I':
			return 34;
		case 'J':
			return 35;
		case 'K':
			return 36;
		case 'L':
			return 37;
		case 'M':
			return 38;
		case 'N':
			return 39;
		case 'O':
			return 40;
		case 'P':
			return 41;
		case 'Q':
			return 42;
		case 'R':
			return 43;
		case 'S':
			return 44;
		case 'T':
			return 45;
		case 'U':
			return 46;
		case 'V':
			return 47;
		case 'W':
			return 48;
		case 'X':
			return 49;
		case 'Y':
			return 50;
		case 'Z':
			return 51;
		case '0':
			return 52;
		case '1':
			return 53;
		case '2':
			return 54;
		case '3':
			return 55;
		case '4':
			return 56;
		case '5':
			return 57;
		case '6':
			return 58;
		case '7':
			return 59;
		case '8':
			return 60;
		case '9':
			return 61;
		case '-':
			return 62;
		case '_':
			return 63;
		default:
			return -1;
		}
	}

	/**
	 * encode the specified string <code>str</code>.
	 * 
	 * @param str
	 * @return
	 */
	public static String encode(String str) {
		if (str == null || "".equals(str))
			return str;
		byte[] bytes = str.getBytes();
		int size = (bytes.length * 4 - 1) / 3 + 1;
		int[] result = new int[size];
		for (int i = 0, index = 0; i < bytes.length; i++) {
			int value = bytes[i] & 0x00FF; // ignore sign bits
			switch (i % 3) {
			case 0:
				result[index++] |= (value & 0xFC) >>> 2;
				result[index] |= (value & 0x03) << 4;
				break;
			case 1:
				result[index++] |= (value >>> 4);
				result[index] |= (value & 0x0F) << 2;
				break;
			case 2:
				result[index++] |= (value >>> 6);
				result[index++] |= value & 0x3F;
				break;
			}
		}
		// denote the values with coresponding code.
		for (int i = 0; i < result.length; i++) {
			result[i] = code[result[i]];
		}
		return new String(result, 0, size);
	}

	public static String decode(String str) {
		if (str == null || "".equals(str))
			return str;
		char[] chars = str.toCharArray();
		int size = chars.length * 3 / 4;
		int[] result = new int[size];
		for (int i = 0, index = 0; i < chars.length; i++) {
			int value = getValue(chars[i]);
			switch (i % 4) {
			case 0:
				result[index] |= value << 2;
				break;
			case 1:
				result[index++] |= value >>> 4;
				if (index < size)
					result[index] |= value << 4;
				break;
			case 2:
				result[index++] |= value >>> 2;
				if (index < size)
					result[index] |= value << 6;
				break;
			case 3:
				if (index < size)
					result[index++] |= value;
				break;
			}
		}
		byte[] bytes = new byte[size];
		for (int i = 0; i < result.length; i++) {
			bytes[i] = (byte) result[i];
		}
		return new String(bytes);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		switch (args.length) {
		case 0:
			break;
		case 2:
			if ("-d".equals(args[0])) {
				System.out.println(decode(args[1]));
			} else if ("-e".equals(args[0])) {
				System.out.println(encode(args[1]));
			}
			break;
		default:
			System.out.println("Wrong number of arguments!");
			break;
		}
	}
}