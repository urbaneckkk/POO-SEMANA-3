package SEMANA3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class SteamApiUtil {

    /**
     * Busca detalhes de um jogo específico pelo appid
     */
    public static SteamGame buscarGameSteam(int appid) throws Exception {
        String json = buscarDetalhesJogo(appid);
        JSONObject obj = new JSONObject(json);
        JSONObject gameWrapper = obj.getJSONObject(String.valueOf(appid));

        if (!gameWrapper.getBoolean("success")) {
            throw new Exception("Erro ao buscar jogo: " + appid);
        }

        JSONObject gameData = gameWrapper.getJSONObject("data");
        String nome = gameData.getString("name");
        String imagem = gameData.optString("header_image", "");
        double preco = 0.0;

        if (gameData.has("price_overview")) {
            preco = gameData.getJSONObject("price_overview").getDouble("final") / 100.0;
        }

        SteamGame game = new SteamGame(appid, nome, imagem, preco);

        // Extrair categorias (genres)
        if (gameData.has("genres")) {
            JSONArray genres = gameData.getJSONArray("genres");
            if (genres.length() > 0) {
                game.setCategoria(genres.getJSONObject(0).getString("description"));
            } else {
                game.setCategoria("Outro");
            }
        } else {
            game.setCategoria("Outro");
        }

        return game;
    }

    /**
     * Retorna a lista de jogos mais populares (top n) dinamicamente
     * OBS: Steam não fornece endpoint público oficial de "mais populares", mas
     * podemos usar uma lista de appids de exemplo ou buscar de forma estática
     * via SteamCharts, SteamSpy ou similar.
     */
    public static ArrayList<SteamGame> buscarJogosPopulares(int quantidade) throws Exception {
        ArrayList<SteamGame> jogos = new ArrayList<>();

        // Lista de appids de exemplo (mais vendidos/populares)
        int[] appids = {570, 730, 440, 271590, 1174180, 292030, 578080, 252490, 550, 8930,
                        105600, 578080, 1091500, 359550, 304930, 221100, 252950, 271590, 359320, 236390};

        for (int i = 0; i < Math.min(quantidade, appids.length); i++) {
            try {
                jogos.add(buscarGameSteam(appids[i]));
            } catch (Exception e) {
                System.out.println("Erro ao buscar appid " + appids[i] + ": " + e.getMessage());
            }
        }

        return jogos;
    }

    /**
     * Faz a requisição HTTP e retorna o JSON do jogo
     */
    private static String buscarDetalhesJogo(int appid) throws Exception {
        String url = "https://store.steampowered.com/api/appdetails?appids=" + appid + "&cc=br&l=portuguese";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content.toString();
    }
}
