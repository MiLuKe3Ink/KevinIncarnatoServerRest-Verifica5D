package serverrest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GetHandler implements HttpHandler {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            inviaErrore(exchange, 405, "Metodo non consentito. Usa GET");
            return;
        }

        try {
            Map<String, String> parametri = estraiParametri(exchange.getRequestURI().getQuery());

            if (validazioneParametri(parametri)) {
                inviaErrore(exchange, 400,
                        "Parametri mancanti. Necessari: operando1, operando2, operatore");
                return;
            }

            String giocataOriginale = parametri.get("giocata");
            int numero = Integer.parseInt(parametri.get("numero"));

            boolean vittoria = Service.logicaDiCalcolo(
                    giocataOriginale.toLowerCase(), numero);

            Response response = new Response();
            response.setGiocata(giocataOriginale);
            response.setNumero(numero);
            response.setVittoria(vittoria);

            String jsonRisposta = gson.toJson(response);
            inviaRisposta(exchange, 200, jsonRisposta);

        } catch (NumberFormatException e) {
            inviaErrore(exchange, 400, "Operandi non validi. Devono essere numeri");
        } catch (IllegalArgumentException e) {
            inviaErrore(exchange, 400, e.getMessage());
        } catch (Exception e) {
            inviaErrore(exchange, 500, "Errore interno del server: " + e.getMessage());
        }
    }

    private boolean validazioneParametri(Map<String, String> parametri) {

        if (!parametri.containsKey("giocata") || !parametri.containsKey("numero")) {
            return true;
        }

        String giocata = parametri.get("giocata").toLowerCase();
        if (!giocata.equals("pari") && !giocata.equals("dispari")) {
            return true;
        }

        try {
            int numero = Integer.parseInt(parametri.get("numero"));
            // MODERATO-1: range corretto da 1-36 a 0-36
            if (numero < 0 || numero > 36) {
                return true;
            }
        } catch (NumberFormatException e) {
            return true;
        }

        return false;
    }

    private Map<String, String> estraiParametri(String query) {
        Map<String, String> parametri = new HashMap<>();

        if (query == null || query.isEmpty()) {
            return parametri;
        }

        String[] coppie = query.split("&");
        for (String coppia : coppie) {
            String[] keyValue = coppia.split("=");
            if (keyValue.length == 2) {
                try {
                    String chiave = URLDecoder.decode(keyValue[0], "UTF-8");
                    String valore = URLDecoder.decode(keyValue[1], "UTF-8");
                    parametri.put(chiave, valore);
                } catch (Exception e) {
                    // Ignora parametri malformati
                }
            }
        }

        return parametri;
    }

    private void inviaRisposta(HttpExchange exchange, int codice, String jsonRisposta)
            throws IOException {

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");

        byte[] bytes = jsonRisposta.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(codice, bytes.length);

        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private void inviaErrore(HttpExchange exchange, int codice, String messaggio)
            throws IOException {

        Map<String, Object> errore = new HashMap<>();
        errore.put("errore", messaggio);
        errore.put("status", codice);

        String jsonErrore = gson.toJson(errore);
        inviaRisposta(exchange, codice, jsonErrore);
    }
}