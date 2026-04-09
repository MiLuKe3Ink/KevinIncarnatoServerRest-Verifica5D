package serverrest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PostHandler implements HttpHandler {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            inviaErrore(exchange, 405, "Metodo non consentito. Usa POST");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));

            Request request = gson.fromJson(reader, Request.class);
            reader.close();

            if (request == null) {
                inviaErrore(exchange, 400, "Body della richiesta vuoto o non valido");
                return;
            }

            if (validazioneParametri(request)) {
                inviaErrore(exchange, 400, "Operatore mancante o vuoto");
                return;
            }

            // ERRATO-3: conserva il valore originale separato da quello normalizzato
            String giocataOriginale = request.getGiocata();
            // ERRATO-4: tipo di ritorno boolean
            boolean vittoria = Service.logicaDiCalcolo(
                    giocataOriginale.toLowerCase(), request.getNumero());

            Response response = new Response();
            response.setGiocata(giocataOriginale); // formato originale preservato
            response.setNumero(request.getNumero());
            response.setVittoria(vittoria);

            String jsonRisposta = gson.toJson(response);
            inviaRisposta(exchange, 200, jsonRisposta);

        } catch (JsonSyntaxException e) {
            inviaErrore(exchange, 400, "JSON non valido: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            inviaErrore(exchange, 400, e.getMessage());
        } catch (Exception e) {
            inviaErrore(exchange, 500, "Errore interno del server: " + e.getMessage());
        }
    }

    private boolean validazioneParametri(Request request) {
        if (request.getGiocata() == null || request.getGiocata().isBlank()) {
            return true;
        }

        String giocata = request.getGiocata().toLowerCase();
        if (!giocata.equals("pari") && !giocata.equals("dispari")) {
            return true;
        }

        // MODERATO-1: range corretto da 1-36 a 0-36
        if (request.getNumero() < 0 || request.getNumero() > 36) {
            return true;
        }

        return false;
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