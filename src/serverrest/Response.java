package serverrest;

public class Response {

    private String giocata;
    private int numero;        // corretto da String a int, coerente con gli handler
    private Boolean vittoria;

    // Costruttore vuoto necessario per GSON
    public Response() {
    }

    // MODERATO-4: costruttore corretto, vittoria accettato come parametro
    public Response(String giocata, int numero, boolean vittoria) {
        this.giocata = giocata;
        this.numero = numero;
        this.vittoria = vittoria;
    }

    // Getter
    public String getGiocata() {
        return giocata;
    }

    public int getNumero() {
        return numero;
    }

    public Boolean getVittoria() {
        return vittoria;
    }

    // Setter
    public void setGiocata(String giocata) {
        this.giocata = giocata;
    }

    public void setNumero(int numero) {   // corretto da String a int
        this.numero = numero;
    }

    public void setVittoria(Boolean vittoria) {
        this.vittoria = vittoria;
    }
}