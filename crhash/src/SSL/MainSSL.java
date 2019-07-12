package SSL;

public class MainSSL {

    private static EmetteurSSL emetteur;
    private static RecepteurSSL recepteur;

    public static void main(String[] args) {

        emetteur = new EmetteurSSL();
        recepteur = new RecepteurSSL();



    }

}
