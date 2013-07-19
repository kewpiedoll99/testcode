/**
 * User: barclaydunn
 * Date: 6/4/12
 * Time: 9:55 AM
 */
public class Decrypt {

    public static void main(String args[]) throws Exception {

        Cryptography cryptography = new Cryptography("DC4BC5EC90B888F4", true);
        String encryptedString = "$1$jSCKq5uW$57N/.O5E8zkbZVOV5fR1F.", decryptedString;
        try {
            decryptedString = cryptography.decryptToString(encryptedString);

            System.out.println("encryptedString: " + encryptedString + "; decryptedString: " + decryptedString);
        } catch (Exception e) {
            System.out.println(" --> ERROR with encryptedString: " + encryptedString);
        }
    }
}
