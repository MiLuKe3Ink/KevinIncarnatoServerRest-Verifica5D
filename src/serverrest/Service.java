/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverrest;

/**
 *
 * @author delfo
 */
public class DaFareService {
    
    /**
     * Esegue l'operazione matematica richiesta
     * 
     * @param 
     * @param 
     * @param 
     * @return 
     * @throws IllegalArgumentException se ...
     */
    public static double logicaDiCalcolo() 
            throws IllegalArgumentException {
        
        // Controllo se i parametri passati sono validi
                if (!parametriValidi()) {
            throw new IllegalArgumentException("Operatore non può essere vuoto");
        }
        
        try {
            
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Opzione non valida. Opzione deve essere DA FARE");
        }
        return 0; // Placeholder, da sostituire con il risultato della logica di calcolo
    }

    // Metodo di validazione dei parametri (da implementare)
    private static boolean parametriValidi()
    {
        return false;
    }
}
