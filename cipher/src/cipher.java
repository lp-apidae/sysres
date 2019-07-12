import java.io.InputStream;
import java.security.*;
import java.util.Scanner;
import javax.crypto.*;

public class cipher {

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        KeyGenerator keygen = KeyGenerator.getInstance("DES");
        keygen.init(56);
        Key maClef = keygen.generateKey();

        Scanner clavier = new Scanner(System.in);
        String msg = clavier.next();


        Cipher cph = Cipher.getInstance("DES");
        cph.init(Cipher.ENCRYPT_MODE, maClef);
        byte[] msgCode = cph.doFinal(msg.getBytes());
        System.out.println(new String(msgCode));


        cph.init(Cipher.DECRYPT_MODE, maClef);
        System.out.println(new String(cph.doFinal(msgCode)));
    }

}
