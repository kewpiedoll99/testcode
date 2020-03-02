package com.barclayadunn.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * @author Graham
 *
 */
public class DataTransforms {
	public static byte[] compressString(String str) {
		return (compressByteArray(stringToUtf8ByteArray(str)));
	}

	public static String decompressString(byte[] compressed) {
		return (utf8ByteArrayToString(decompressByteArray(compressed)));
	}

	public static byte[] stringToUtf8ByteArray(String str) {
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
//			throw new RCSystemException("Didn't expect exception: " + e.getMessage(), e);
		}
        return null;
	}

	public static String utf8ByteArrayToString(byte[] utf8Bytes) {
		try {
			return new String(utf8Bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
//			throw new RCSystemException("Didn't expect exception: " + e.getMessage(), e);
		}
        return null;
	}

	public static byte[] compressByteArray(byte[] uncompressed) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(
					uncompressed.length);
			DeflaterOutputStream out = new DeflaterOutputStream(baos);
			out.write(uncompressed);
			out.close();
			return baos.toByteArray();
		} catch (IOException e) {
//			throw new RCSystemException("Didn't expect exception: " + e.getMessage(), e);
		}
        return null;
	}

	public static byte[] decompressByteArray(byte[] compressed) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(compressed);
			InflaterInputStream in = new InflaterInputStream(bais);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(
					5 * compressed.length);
			for (;;) {
				int b = in.read();
				if (b == -1) {
					break;
				}
				baos.write(b);
			}
			return baos.toByteArray();
		} catch (IOException e) {
//			throw new RCSystemException("Didn't expect exception: " + e.getMessage(), e);
		}
        return null;
	}

	/**
	 * This is a transformation of a raw array of bytes to a string. This
	 * currently follows base64 standards with the one exception that there are
	 * no newlines. The six-bit character set is as follows:
	 *
	 * <pre>
	 *              dec | hex  | char
	 *             -----|------|------
	 *                0 | 0x00 |  'A'
	 *                    ...
	 *               25 | 0x19 |  'Z'
	 *               26 | 0x1A |  'a'
	 *                    ...
	 *               51 | 0x33 |  'z'
	 *               52 | 0x34 |  '0'
	 *                    ...
	 *               61 | 0x3D |  '9'
	 *               62 | 0x3E |  '+'
	 *               63 | 0x3F |  '/'
	 * </pre>
	 *
	 * @param raw
	 * @return The encoded string
	 */
	public static String stringEncodeByteArray(byte[] raw) {
		if (raw == null) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		byte[] threeBytes = new byte[3];
		char[] fourChars;
		int i;
		for (i = 0; i < raw.length - 2; i += 3) {
			for (int j = 0; j < 3; j++) {
				threeBytes[j] = raw[i + j];
			}
			fourChars = encodeThreeBytes(threeBytes);
			for (int j = 0; j < 4; j++) {
				buf.append(fourChars[j]);
			}
		}
		if (i == raw.length - 2) {
			for (int j = 0; j < 2; j++) {
				threeBytes[j] = raw[i + j];
			}
			fourChars = encodeTwoBytes(threeBytes);
			for (int j = 0; j < 4; j++) {
				buf.append(fourChars[j]);
			}
		} else if (i == raw.length - 1) {
			fourChars = encodeOneByte(raw[i]);
			for (int j = 0; j < 4; j++) {
				buf.append(fourChars[j]);
			}
		}
		return buf.toString();
	}

	/**
	 * Inverse of stringEncodeByteArray( byte[] )
	 *
	 * @param coded
	 * @return
	 */
	public static byte[] decodeToByteArray(String coded) {
		int codedLength = coded.length();
		if ((codedLength % 4) != 0) {
			throw new IllegalArgumentException("Not a valid code string.");
		}
		int padBytes = 0;
		if (coded.endsWith("==")) {
			padBytes = 2;
		} else if (coded.endsWith("=")) {
			padBytes = 1;
		}
		int byteLength = codedLength / 4 * 3 - padBytes;

		ByteArrayOutputStream baos = new ByteArrayOutputStream(byteLength);
		char[] fourChars = new char[4];
		for (int i = 0; i < codedLength; i += 4) {
			for (int j = 0; j < 4; j++) {
				fourChars[j] = coded.charAt(i + j);
			}
			try {
				baos.write(decodeFourChars(fourChars));
			} catch (IOException e) {
//				throw new RCSystemException("Didn't expect: " + e.getMessage(), e);
			}
		}
		return baos.toByteArray();
	}

	static char[] encodeOneByte(byte b) {
		char[] ret = new char[4];

		ret[0] = encodeVal((b & 0xFC) >>> 2);
		ret[1] = encodeVal((b & 0x03) << 4);
		ret[2] = '=';
		ret[3] = '=';

		return ret;
	}

	static char[] encodeTwoBytes(byte[] bytes) {
		char[] ret = new char[4];

		ret[0] = encodeVal((bytes[0] & 0xFC) >>> 2);
		ret[1] = encodeVal(((bytes[0] & 0x03) << 4) | ((bytes[1] & 0xF0) >>> 4));
		ret[2] = encodeVal((bytes[1] & 0x0F) << 2);
		ret[3] = '=';

		return ret;
	}

	static char[] encodeThreeBytes(byte[] bytes) {
		char[] ret = new char[4];

		ret[0] = encodeVal((bytes[0] & 0xFC) >>> 2);
		ret[1] = encodeVal(((bytes[0] & 0x03) << 4) | ((bytes[1] & 0xF0) >>> 4));
		ret[2] = encodeVal(((bytes[1] & 0x0F) << 2) | ((bytes[2] & 0xC0) >>> 6));
		ret[3] = encodeVal(bytes[2] & 0x3F);

		return ret;
	}

	static byte[] decodeFourChars(char[] chars) {
		int padBytes = 0;
		if (chars[2] == '=') {
			padBytes = 2;
		} else if (chars[3] == '=') {
			padBytes = 1;
		}
		byte[] ret = new byte[3 - padBytes];
		int[] sixBitThings = new int[4];

		for (int i = 0; i < 4; i++) {
			sixBitThings[i] = decodeChar(chars[i]);
		}
		ret[0] = (byte) ((sixBitThings[0] << 2) | (sixBitThings[1] >>> 4));
		if (padBytes < 2) {
			ret[1] = (byte) ((sixBitThings[1] << 4) | (sixBitThings[2] >>> 2));
			if (padBytes < 1) {
				ret[2] = (byte) ((sixBitThings[2] << 6) | (sixBitThings[3]));
			}
		}
		return ret;
	}

	private static char encodeVal(int val) {
		if (val < 26) {
			return (char) (val + 'A');
		}
		if (val < 52) {
			return (char) (val - 26 + 'a');
		}
		if (val < 62) {
			return (char) (val - 52 + '0');
		}
		if (val == 62) {
			return '+';
		}
		return '/';
	}

	private static int decodeChar(char ch) {
		if (ch >= 'A' && ch <= 'Z') {
			return ch - 'A';
		}
		if (ch >= 'a' && ch <= 'z') {
			return ch - 'a' + 26;
		}
		if (ch >= '0' && ch <= '9') {
			return ch - '0' + 52;
		}
		if (ch == '+') {
			return 62;
		}
		return 63;
	}
}
