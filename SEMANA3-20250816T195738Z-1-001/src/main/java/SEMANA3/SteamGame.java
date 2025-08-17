package SEMANA3;

public class SteamGame extends Game {
    private int appid;
    private String imagemUrl;

    public SteamGame(int appid, String nome, String imagemUrl, double preco) {
        super(nome, preco, "Steam", 0); // categoria Steam, classificacao 0 (opcional)
        this.appid = appid;
        this.imagemUrl = imagemUrl;
    }

    public int getAppid() { return appid; }
    public String getImagemUrl() { return imagemUrl; }

    @Override
    public String toString() {
        return getNome() + " | Preço: " + getPreco() + " | AppID: " + appid;
    }
}