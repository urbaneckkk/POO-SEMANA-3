package SEMANA3;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class Game {
    private String nome;
    private BigDecimal preco;
    private String categoria;
    private int classificacaoEtaria;

    private static final Locale PT_BR = new Locale("pt", "BR");
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(PT_BR);

    public Game(String nome, double preco, String categoria, int classificacao) {
        this.nome = nome;
        this.preco = BigDecimal.valueOf(preco);
        this.categoria = categoria;
        this.classificacaoEtaria = classificacao;
    }

    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }
    public String getCategoria() { return categoria; }
    public int getClassificacaoEtaria() { return classificacaoEtaria; }

    public void setNome(String nome) {
        if (nome != null && !nome.isEmpty()) this.nome = nome;
    }

    public void setCategoria(String categoria) {
        if (categoria != null && !categoria.isEmpty()) this.categoria = categoria;
    }

    public void setPreco(double preco) {
        if (preco >= 0) this.preco = BigDecimal.valueOf(preco);
    }

    public void setClassificacaoEtaria(int idade) {
        if (idade >= 0 && idade <= 18) this.classificacaoEtaria = idade;
    }

    public static void exibirInformacoes(Game jogo) {
        System.out.println("Nome do jogo: " + jogo.getNome());
        System.out.println("Categoria: " + jogo.getCategoria());
        System.out.println("Preço do jogo: " + MOEDA.format(jogo.getPreco()));
        System.out.println("Classificação etária: " + jogo.getClassificacaoEtaria() + " anos");
        System.out.println("-----------------------------------");
    }

    @Override
    public String toString() {
        return String.format("%s — %s  (%s, %d+)",
                nome, MOEDA.format(preco), categoria, classificacaoEtaria);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return Objects.equals(nome, game.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}
