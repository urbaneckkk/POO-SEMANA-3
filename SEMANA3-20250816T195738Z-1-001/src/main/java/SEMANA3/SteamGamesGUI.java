package SEMANA3;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class SteamGamesGUI extends JFrame {

    public SteamGamesGUI(List<SteamGame> jogos) {
        setTitle("Jogos Steam");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel painelJogos = new JPanel();
        painelJogos.setLayout(new BoxLayout(painelJogos, BoxLayout.Y_AXIS));
        painelJogos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (SteamGame jogo : jogos) {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Altura fixa, largura expansível

            // Imagem do jogo
            JLabel labelImagem;
            try {
                URL url = new URL(jogo.getImagemUrl());
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                labelImagem = new JLabel(new ImageIcon(img));
            } catch (Exception e) {
                labelImagem = new JLabel("Imagem indisponível");
            }
            panel.add(labelImagem, BorderLayout.WEST);

            // Nome e preço
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.add(new JLabel("Nome: " + jogo.getNome()));
            infoPanel.add(new JLabel("Preço: R$ " + jogo.getPreco()));
            panel.add(infoPanel, BorderLayout.CENTER);

            // Botões
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton addBtn = new JButton("Adicionar");
            JButton removeBtn = new JButton("Remover");
            buttonsPanel.add(addBtn);
            buttonsPanel.add(removeBtn);
            panel.add(buttonsPanel, BorderLayout.EAST);

            painelJogos.add(Box.createVerticalStrut(10)); // Espaço entre jogos
            painelJogos.add(panel);
        }

        JScrollPane scroll = new JScrollPane(painelJogos);
        add(scroll, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
