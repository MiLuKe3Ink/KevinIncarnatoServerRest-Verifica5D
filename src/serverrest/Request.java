/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverrest;

/**
 *
 * @author delfo
 */
public class Request {

    private String giocata;
    private Integer numero;

    // Costruttore vuoto necessario per GSON
    public Request() {
    }

    // Costruttore con parametri
    public Request(String giocata, Integer numero) {
        this.giocata = giocata;
        this.numero = numero;
    }

    // Getter
    public String getGiocata() {
        return giocata;
    }

    public Integer getNumero() {
        return numero;
    }


    // Setter
    public void setGiocata(String giocata) {
         this.giocata = giocata;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }


    // ToString
    @Override
    public String toString() {
        return "Request{" + "giocata=" + giocata + ", numero=" + numero +  "}";
    }
}