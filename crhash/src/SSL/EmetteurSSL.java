package SSL;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class EmetteurSSL implements Runnable {


    @Override
    public void run() {

        KeyGenerator generateur = null;

        try {
            generateur = KeyGenerator.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        generateur.init(56);
        Key key = generateur.generateKey();




    }
}
