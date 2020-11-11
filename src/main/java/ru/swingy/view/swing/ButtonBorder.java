package ru.swingy.view.swing;

import lombok.Getter;
import ru.swingy.interfaces.MapObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ButtonBorder {

    private int N;
    private int SIZE = 0;
    @Getter
    private int rowCount = 0;
    private MapObject[][] map;
    private Integer heroPositionX;
    private Integer heroPositionY;
    private JPanel jPanel;

    private Icon iconQueen;
    private Icon iconPawn;

    public ButtonBorder(Integer size, MapObject[][] map, Integer heroPositionX,
                        Integer heroPositionY, JPanel jPanel, Integer gameMapSize) {

        this.jPanel = jPanel;
        this.rowCount = size;

        if (gameMapSize != 0) {
            if (size >= gameMapSize)
                size = gameMapSize;
            else
                size = 5;
        }

        GridLayout gridLayout = new GridLayout(size, size);
        jPanel.setLayout(gridLayout);

        this.map = map;
        this.heroPositionX = heroPositionX;
        this.heroPositionY = heroPositionY;
        //900
        SIZE = 600 / size;
        N = size;

        try {
            BufferedImage masterQueen = ImageIO.read(getClass().getResourceAsStream("/images/queen.png"));
            Image scaledQueen = masterQueen.getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH);
            iconQueen = new ImageIcon(scaledQueen);

            BufferedImage masterPawn = ImageIO.read(getClass().getResourceAsStream("/images/pawn.png"));
            Image scaledPawn = masterPawn.getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH);
            iconPawn = new ImageIcon(scaledPawn);

        } catch (IOException e) {
            e.printStackTrace();
        }

        jPanel.setPreferredSize(new Dimension(N * SIZE, N * SIZE));

        for (int i = -N / 2; i <= N / 2; i++) {
            for (int j = -N / 2; j <= N / 2; j++) {
                if (heroPositionX >= 0 && heroPositionX < map.length
                        && heroPositionY >= 0 && heroPositionY < map.length
                        && heroPositionX + i < map.length && heroPositionX + i >= 0
                        && heroPositionY + j < map.length && heroPositionY + j >= 0) {

                    jPanel.add(new ChessButton(i + heroPositionX, j + heroPositionY));
                } else {
                    jPanel.add(new ChessButton());
                }
            }
        }
    }

    class ChessButton extends JButton {

        public ChessButton(int rowNum, int colNum) {
            super("", rowNum == heroPositionX && colNum == heroPositionY ? iconQueen : map[rowNum][colNum] != null ? iconPawn : null);

            this.setOpaque(true);
            this.setBorderPainted(false);
            if ((rowNum + colNum) % 2 == 1) {
                this.setBackground(Color.gray);
            } else {
                this.setBackground(Color.white);
            }
        }

        public ChessButton() {
            super("");

            this.setOpaque(true);
            this.setBorderPainted(false);
            setVisible(false);
        }
    }

    public void refreshValues(MapObject[][] map, Integer heroPositionX, Integer heroPositionY) {
        Component[] components = jPanel.getComponents();

        for (int i = -N / 2; i <= N / 2; i++) {
            for (int j = -N / 2; j <= N / 2; j++) {

                ChessButton ch = (ChessButton) components[(N * (i + N / 2)) + (j + N / 2)];

                if (heroPositionX >= 0 && heroPositionX < map.length
                        && heroPositionY >= 0 && heroPositionY < map.length
                        && heroPositionX + i < map.length && heroPositionX + i >= 0
                        && heroPositionY + j < map.length && heroPositionY + j >= 0) {

                    ch.setVisible(true);
                    int i1 = i + heroPositionX;
                    int i2 = j + heroPositionY;

                    if (i == 0 && j == 0) {
                        if ((i1 + i2) % 2 == 1) {
                            ch.setBackground(Color.gray);
                        } else {
                            ch.setBackground(Color.white);
                        }
                    } else {

                        ch.setIcon(map[i1][i2] != null ? iconPawn : null);
                        if ((i1 + i2) % 2 == 1) {
                            ch.setBackground(Color.gray);
                        } else {
                            ch.setBackground(Color.white);
                        }
                    }
                } else {
                    ch.setVisible(false);
                }
            }
        }
    }
}