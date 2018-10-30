package com.barclayadunn.crypt;

/**
 * User: barclaydunn
 * Date: 6/4/12
 * Time: 10:00 AM
 */
public class Encrypt {

    public static void main(String args[]) throws Exception {

        Cryptography cryptography = new Cryptography("DC4BC5EC90B888F4", true);
        String encryptedString, decryptedString = "RA8KOxeh";
        try {
            encryptedString = cryptography.encryptStringToString(decryptedString);

            System.out.println("encryptedString: " + encryptedString + "; decryptedString: " + decryptedString);
        } catch (Exception e) {
            System.out.println(" --> ERROR with decryptedString: " + decryptedString);
        }
    }
}
