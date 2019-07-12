package bruteforce;

import java.io.UnsupportedEncodingException;
import java.security.*;

public class crhash {

    private static String hash = "81fe8bfe87576c3ecb22426f8e57847382917acf";
    private static char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        String msg = "";

        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                for (int k = 0; k < 26; k++) {
                    for (int l = 0; l < 26; l++) {
                        msg = String.valueOf(alphabet[i]) +
                                alphabet[j] +
                                alphabet[k] +
                                alphabet[l];
                        md.update(msg.getBytes());

                        if (hash.equals(convertToHex(md.digest()))) {
                            System.out.println("Le hash donné est le SHA de " + msg);
                            System.exit(0);
                        }
                    }
                }
            }
        }


    }

}
