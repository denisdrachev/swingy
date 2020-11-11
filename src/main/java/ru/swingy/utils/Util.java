package ru.swingy.utils;

import ru.swingy.Application;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Util {

    public static Random random = new Random();

    public static Icon warrior;
    public static Icon archer;
    public static Icon wizard;

    public static Icon weapon;
    public static Icon armor;
    public static Icon helmet;

    public static Icon queen;
    public static Icon pawn;

    static {
        Integer infoIconSize = 80;
        try {
            BufferedImage masterQueen = ImageIO.read(Application.class.getResourceAsStream("/images/warrior.png"));
            Image scaledQueen = masterQueen.getScaledInstance(infoIconSize, infoIconSize, Image.SCALE_SMOOTH);
            warrior = new ImageIcon(scaledQueen);

            BufferedImage masterQueen2 = ImageIO.read(Application.class.getResourceAsStream("/images/archer.png"));
            Image scaledQueen2 = masterQueen2.getScaledInstance(infoIconSize, infoIconSize, Image.SCALE_SMOOTH);
            archer = new ImageIcon(scaledQueen2);

            BufferedImage masterQueen3 = ImageIO.read(Application.class.getResourceAsStream("/images/Sorceress.png"));
            Image scaledQueen3 = masterQueen3.getScaledInstance(infoIconSize, infoIconSize, Image.SCALE_SMOOTH);
            wizard = new ImageIcon(scaledQueen3);


            BufferedImage masterQueen4 = ImageIO.read(Application.class.getResourceAsStream("/images/sword.png"));
            Image scaledQueen4 = masterQueen4.getScaledInstance(infoIconSize, infoIconSize, Image.SCALE_SMOOTH);
            weapon = new ImageIcon(scaledQueen4);

            BufferedImage masterQueen5 = ImageIO.read(Application.class.getResourceAsStream("/images/armor.png"));
            Image scaledQueen5 = masterQueen5.getScaledInstance(infoIconSize, infoIconSize, Image.SCALE_SMOOTH);
            armor = new ImageIcon(scaledQueen5);

            BufferedImage masterQueen6 = ImageIO.read(Application.class.getResourceAsStream("/images/helmet.png"));
            Image scaledQueen6 = masterQueen6.getScaledInstance(infoIconSize, infoIconSize, Image.SCALE_SMOOTH);
            helmet = new ImageIcon(scaledQueen6);


            BufferedImage master = ImageIO.read(Application.class.getResourceAsStream("/images/queen.png"));
            Image scaled = master.getScaledInstance(infoIconSize, infoIconSize, Image.SCALE_SMOOTH);
            queen = new ImageIcon(scaled);

            BufferedImage master2 = ImageIO.read(Application.class.getResourceAsStream("/images/pawn.png"));
            Image scaled2 = master2.getScaledInstance(infoIconSize, infoIconSize, Image.SCALE_SMOOTH);
            pawn = new ImageIcon(scaled2);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Integer getLevelExperience(Integer level) {
        if (level == 0)
            return 0;
        return (level * 1000) + (level - 1) * (level - 1) * 450;
    }
}
