package serverrest;

public class Service {

    public static boolean logicaDiCalcolo(String giocata, int numero)
            throws IllegalArgumentException {

        if (!parametriValidi(giocata, numero)) {
            throw new IllegalArgumentException("Parametri non validi");
        }

        try {
            boolean numeroEPari = (numero % 2 == 0);
            boolean giocataEPari = giocata.equals("pari");

            return numeroEPari == giocataEPari;

        } catch (Exception e) {
            throw new IllegalArgumentException("Errore nel calcolo: " + e.getMessage());
        }
    }

    private static boolean parametriValidi(String giocata, int numero) {
        if (giocata == null || giocata.isBlank()) {
            return false;
        }
        if (!giocata.equals("pari") && !giocata.equals("dispari")) {
            return false;
        }
        // MODERATO-3: range corretto da 1-36 a 0-36
        if (numero < 0 || numero > 36) {
            return false;
        }
        return true;
    }
}