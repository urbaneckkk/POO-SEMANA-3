package SEMANA3;

import SEMANA3.SteamApiUtil;
import SEMANA3.SteamGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Main {

    private static User currentUser;
    private static ArrayList<SteamGame> cart = new ArrayList<>();
    private static JLabel saldoLabel;
    private static JPanel cartPanel;
    private static JPanel gamesPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> mostrarLogin());
    }

    private static void mostrarLogin() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 150);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Nome do usuário:"));
        JTextField nomeField = new JTextField();
        panel.add(nomeField);

        JButton entrarBtn = new JButton("Entrar");
        panel.add(entrarBtn);

        loginFrame.add(panel, BorderLayout.CENTER);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);

        entrarBtn.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            if (!nome.isEmpty()) {
                currentUser = new User(nome, 500.0);
                loginFrame.dispose();
                mostrarTelaPrincipal();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Digite um nome válido!");
            }
        });
    }

    private static final int JOGOS_POR_PAGINA = 3;
    private static int paginaAtual = 0;
    private static ArrayList<SteamGame> todosOsJogos = new ArrayList<>();
    private static String categoriaSelecionada = "Todos";

    private static void mostrarTelaPrincipal() {
        JFrame frame = new JFrame("Loja Steam");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Topo - usuário, saldo e categorias
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel userLabel = new JLabel("Usuário: " + currentUser.getNome());
        saldoLabel = new JLabel("Saldo: R$ " + currentUser.getSaldo());
        topPanel.add(userLabel);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(saldoLabel);

        // ComboBox de categorias
        String[] categorias = { "Todos", "MOBA", "FPS", "Aventura", "RPG", "Simulação" };
        JComboBox<String> comboCategorias = new JComboBox<>(categorias);
        comboCategorias.addActionListener(e -> {
            categoriaSelecionada = (String) comboCategorias.getSelectedItem();
            paginaAtual = 0;
            atualizarPagina();
        });
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(comboCategorias);
        frame.add(topPanel, BorderLayout.NORTH);

        // Centro - painel de jogos
        gamesPanel = new JPanel();
        gamesPanel.setLayout(new BoxLayout(gamesPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(gamesPanel);
        frame.add(scroll, BorderLayout.CENTER);

        // Lado - carrinho
        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        JScrollPane cartScroll = new JScrollPane(cartPanel);
        cartScroll.setPreferredSize(new Dimension(200, 0));
        frame.add(cartScroll, BorderLayout.EAST);

        // Buscar jogos da Steam dinamicamente
        try {
            // Exemplo: buscar os 20 jogos mais populares
            ArrayList<SteamGame> jogosPopulares = SteamApiUtil.buscarJogosPopulares(20);
            String[] categoriasTeste = { "MOBA", "FPS", "Aventura", "RPG", "Simulação" };

            for (SteamGame game : jogosPopulares) {
                // Definir categoria aleatória de teste
                game.setCategoria(categoriasTeste[(int) (Math.random() * categoriasTeste.length)]);
                todosOsJogos.add(game);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar jogos: " + e.getMessage());
        }

        // Painel inferior com botões de paginação
        JPanel bottomPanel = new JPanel();
        JButton prevBtn = new JButton("Anterior");
        JButton nextBtn = new JButton("Próximo");

        prevBtn.addActionListener(e -> {
            if (paginaAtual > 0) {
                paginaAtual--;
                atualizarPagina();
            }
        });

        nextBtn.addActionListener(e -> {
            if ((paginaAtual + 1) * JOGOS_POR_PAGINA < todosOsJogos.size()) {
                paginaAtual++;
                atualizarPagina();
            }
        });

        bottomPanel.add(prevBtn);
        bottomPanel.add(nextBtn);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Mostra a primeira página
        atualizarPagina();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Atualiza o painel central conforme página e categoria selecionada
    private static void atualizarPagina() {
        gamesPanel.removeAll();
        ArrayList<SteamGame> filtrados = new ArrayList<>();

        for (SteamGame game : todosOsJogos) {
            if (categoriaSelecionada.equals("Todos") || game.getCategoria().equals(categoriaSelecionada)) {
                filtrados.add(game);
            }
        }

        int inicio = paginaAtual * JOGOS_POR_PAGINA;
        int fim = Math.min(inicio + JOGOS_POR_PAGINA, filtrados.size());

        for (int i = inicio; i < fim; i++) {
            adicionarJogoAoPainel(filtrados.get(i));
        }

        gamesPanel.revalidate();
        gamesPanel.repaint();
    }

    private static void adicionarJogoAoPainel(SteamGame game) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(game.getNome()));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel precoLabel = new JLabel("Preço: R$ " + game.getPreco());
        panel.add(precoLabel);

        if (game.getImagemUrl() != null && !game.getImagemUrl().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(new java.net.URL(game.getImagemUrl()));
                JLabel imgLabel = new JLabel(icon);
                panel.add(imgLabel);
            } catch (Exception ignored) {
            }
        }

        JButton addBtn = new JButton("Adicionar ao carrinho");
        panel.add(addBtn);

        addBtn.addActionListener(e -> {
            java.math.BigDecimal saldo = new java.math.BigDecimal(currentUser.getSaldo());
            java.math.BigDecimal preco = (game.getPreco() instanceof java.math.BigDecimal)
                    ? (java.math.BigDecimal) game.getPreco()
                    : new java.math.BigDecimal(game.getPreco().toString());
            if (saldo.compareTo(preco) >= 0) {
                cart.add(game);
                currentUser.setSaldo(saldo.subtract(preco).doubleValue());
                saldoLabel.setText("Saldo: R$ " + currentUser.getSaldo());
                atualizarCarrinho();
            } else {
                JOptionPane.showMessageDialog(null, "Saldo insuficiente para adicionar este jogo!");
            }
        });

        gamesPanel.add(panel);
        gamesPanel.revalidate();
    }

    private static void atualizarCarrinho() {
        cartPanel.removeAll();
        for (SteamGame game : cart) {
            JPanel panel = new JPanel(new FlowLayout());
            JLabel lbl = new JLabel(game.getNome() + " | R$ " + game.getPreco());
            JButton removeBtn = new JButton("Remover");
            removeBtn.addActionListener(e -> {
                cart.remove(game);
                java.math.BigDecimal saldo = new java.math.BigDecimal(currentUser.getSaldo());
                java.math.BigDecimal preco = (game.getPreco() instanceof java.math.BigDecimal)
                        ? (java.math.BigDecimal) game.getPreco()
                        : new java.math.BigDecimal(game.getPreco().toString());
                currentUser.setSaldo(saldo.add(preco).doubleValue());
                saldoLabel.setText("Saldo: R$ " + currentUser.getSaldo());
                atualizarCarrinho();
            });
            panel.add(lbl);
            panel.add(removeBtn);
            cartPanel.add(panel);
        }
        cartPanel.revalidate();
        cartPanel.repaint();
    }

    // Classe interna User
    static class User {
        private String nome;
        private double saldo;

        public User(String nome, double saldo) {
            this.nome = nome;
            this.saldo = saldo;
        }

        public String getNome() {
            return nome;
        }

        public double getSaldo() {
            return saldo;
        }

        public void setSaldo(double saldo) {
            this.saldo = saldo;
        }
    }
}
