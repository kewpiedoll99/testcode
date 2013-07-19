import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Graham
 *
 */
public class Cryptography
{
	private static final String	ALGORITHM		= "Blowfish";
	// BLOCK_SIZE is in bits
	private static final int	BLOCK_SIZE		= 64;
	private static final String	MODE			= "ECB";
	// "NoPadding" because the padding is done manually
	private static final String	PADDING			= "NoPadding";
	private static final String	TRANSFORMATION	= ALGORITHM + "/" + MODE + "/" + PADDING;
	private String				secretKeyString;
	private SecretKey secretKey;

	public Cryptography(String secretKeyString) {
		this(secretKeyString, false);
	}

	public Cryptography(String secretKeyString, boolean deobfuscate) {
		this.secretKeyString = secretKeyString;
		byte[] rawKey = getSecretKeyBytes();
		if (deobfuscate) {
			rawKey = deobfuscate(rawKey);
		}
		secretKey = new SecretKeySpec(rawKey, ALGORITHM);
	}

	/**
	 * @param cipherText
	 * @return
	 */
	public String decryptToString(String cipherText) {
		byte[] cryptoBytes = DataTransforms.decodeToByteArray(cipherText);
		byte[] plainTextAsByteArray = decrypt(cryptoBytes);
		return DataTransforms.utf8ByteArrayToString(plainTextAsByteArray);
	}

	public byte[] decrypt(byte[] cipherText) {
		Cipher cipher;
			try {
				cipher = Cipher.getInstance(TRANSFORMATION);
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
				return removePadding(cipher.doFinal(cipherText));
//			} catch (InvalidKeyException e) {
//				throw new RCSystemException("Got exception in decrypt: " + e, e);
//			} catch (NoSuchAlgorithmException e) {
//				throw new RCSystemException("Got exception in decrypt: " + e, e);
//			} catch (NoSuchPaddingException e) {
//				throw new RCSystemException("Got exception in decrypt: " + e, e);
//			} catch (IllegalBlockSizeException e) {
//				throw new RCSystemException("Got exception in decrypt: " + e, e);
//			} catch (BadPaddingException e) {
//				throw new RCSystemException("Got exception in decrypt: " + e, e);
			} catch (Exception e) {
                System.err.println("Got exception in decrypt: " + e.getMessage());
            }
        return null;
	}

    public String encryptStringToString(String plainText) {
        byte[] plainTextAsByteArray = DataTransforms.stringToUtf8ByteArray(plainText);
        return DataTransforms.stringEncodeByteArray(encrypt(plainTextAsByteArray));
    }

    public byte[] encrypt(byte[] plainText) {
        return encrypt(plainText, true);
    }

    byte[] encrypt(byte[] plainText, boolean addPadding) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] toEncrypt = plainText;
            if (addPadding) {
                toEncrypt = addPadding(plainText);
            }
            return cipher.doFinal(toEncrypt);
//        } catch (InvalidKeyException e) {
//            throw new RCSystemException("Got exception in encrypt: " + e, e);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RCSystemException("Got exception in encrypt: " + e, e);
//        } catch (NoSuchPaddingException e) {
//            throw new RCSystemException("Got exception in encrypt: " + e, e);
//        } catch (IllegalBlockSizeException e) {
//            throw new RCSystemException("Got exception in encrypt: " + e, e);
//        } catch (BadPaddingException e) {
//            throw new RCSystemException("Got exception in encrypt: " + e, e);
        } catch (Exception e) {
            System.err.println("Got exception in encrypt: " + e.getMessage());
            return null;
        }
    }

    public String getSecretKeyString() {
		return secretKeyString;
	}

	public void setSecretKeyString(String secretKeyString) {
		this.secretKeyString = secretKeyString;
	}

	public String getAlgorithm() {
		return ALGORITHM;
	}

	private byte[] getSecretKeyBytes() {
		return stringToBytes(secretKeyString);
	}

	private byte[] deobfuscate(byte[] rawKey) {
		byte[] mask = { (byte) 0xF1, (byte) 0xBB, (byte) 0x3A, (byte) 0x22, (byte) 0x26,
				(byte) 0x16, (byte) 0x67, (byte) 0xCF, (byte) 0xE2, (byte) 0x00, (byte) 0x08,
				(byte) 0x19, (byte) 0x11, (byte) 0xAF, (byte) 0xF0, (byte) 0xD5 };
		int length = rawKey.length;
		byte[] ret = new byte[length];
		for(int i=0; i<length; i++) {
			int a = rawKey[i] & 0xFF;
			int b = mask[i % mask.length] & 0xFF;
			ret[i] =  (byte) (a ^ b);
		}
		return ret;
	}

	public static byte[] stringToBytes(String hexString) {
		int byteLength = hexString.length() / 2;
		byte[] raw = new byte[byteLength];
		for (int i = 0; i < byteLength; i++) {
			String byteStr = hexString.substring(i * 2, i * 2 + 2);
			int byteInt = Integer.parseInt(byteStr, 16);
			raw[i] = (byte) (byteInt & 0xFF);
		}
		return raw;
	}

	public static byte[] addPadding(byte[] plainText) {
		int byteBlockSize = BLOCK_SIZE / 8;
		int numPaddingBytes = byteBlockSize - (plainText.length % byteBlockSize);
		if (numPaddingBytes == 0) {
			numPaddingBytes = byteBlockSize;
		}
		byte[] paddedText = new byte[plainText.length + numPaddingBytes];
		for (int i = 0; i < plainText.length; i++) {
			paddedText[i] = plainText[i];
		}
		paddedText[plainText.length] = (byte) '\n';
		for (int i = plainText.length + 1; i < plainText.length + numPaddingBytes; i++) {
			paddedText[i] = 0;
		}
		return paddedText;
	}

	public static byte[] removePadding(byte[] paddedText) {
		int endOfPlainText = paddedText.length - 1;
		byte b;
		do {
			b = paddedText[endOfPlainText--];
		} while (b == 0);
		byte[] plainText = new byte[endOfPlainText + 1];
		for (int i = 0; i <= endOfPlainText; i++) {
			plainText[i] = paddedText[i];
		}
		return plainText;
	}
}
