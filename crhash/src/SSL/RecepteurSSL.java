package SSL;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class RecepteurSSL implements Runnable {

    @Override
    public void run() {

        KeyPairGenerator kpg = null;

        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();



    }

}
