package ru.swingy.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.swingy.model.Artifact;
import ru.swingy.model.Enemy;
import ru.swingy.model.GameMap;
import ru.swingy.model.Hero;
import ru.swingy.utils.Util;
import ru.swingy.view.interfaces.ViewInterface;
import ru.swingy.view.swing.ButtonBorder;
import ru.swingy.view.swing.PickHeroPanel;

import javax.swing.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.System.exit;

@Service
public class JFrameView implements ViewInterface, ActionListener, KeyListener {

    private JFrame jFrame;
    private JLabel heroInfoLabel;
    private JPanel mapPanel;
    private JLabel enemyInfoLabel;
    private JSplitPane jSplitPane;

    private JTextArea console;
    private JPanel heroPanel;
    private JPanel enemyPanel;
    private ButtonBorder buttonBorder = null;

    private JLabel artifactAttackLabel;
    private JLabel artifactDefenceLabel;
    private JLabel artifactHelmetLabel;
    private JProgressBar jProgressHp;
    private JProgressBar jProgressExp;

    private String step = null;

    @Value("${game.map.size:5}")
    private Integer gameMapSize;

    public void init(JFrame jFrame) {
        this.jFrame = jFrame;
        createFrame();
    }

    private void createFrame() {

        jFrame.setVisible(false);
        jFrame.setSize(1400, 900);
        jFrame.setTitle("Time to Play The Game!");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);

        enemyPanel = new JPanel(new BorderLayout());
        enemyInfoLabel = new JLabel();
        enemyPanel.add(enemyInfoLabel, BorderLayout.NORTH);

        mapPanel = new JPanel();
        heroPanel = new JPanel(new GridBagLayout());
        heroInfoLabel = new JLabel();
        heroPanel.add(heroInfoLabel, new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(50,1,1,1), 0, 0));

        jProgressHp = new JProgressBar();
        jProgressHp.setForeground(Color.RED);
        jProgressHp.setStringPainted(true);
        jProgressHp.setVisible(false);
        heroPanel.add(jProgressHp, new GridBagConstraints(1, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10), 0, 0));

        jProgressExp = new JProgressBar();
        jProgressExp.setForeground(Color.ORANGE);
        jProgressExp.setStringPainted(true);
        jProgressExp.setVisible(false);
        heroPanel.add(jProgressExp, new GridBagConstraints(1, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(35,10,10,10), 0, 0));

        JPanel heroArtifactPanel = new JPanel(new BorderLayout());

        artifactAttackLabel = new JLabel();
        artifactAttackLabel.setIcon(Util.weapon);
        heroArtifactPanel.add(artifactAttackLabel, BorderLayout.NORTH);
        artifactAttackLabel.setVisible(false);

        artifactDefenceLabel = new JLabel();
        artifactDefenceLabel.setIcon(Util.armor);
        heroArtifactPanel.add(artifactDefenceLabel, BorderLayout.CENTER);
        artifactDefenceLabel.setVisible(false);

        artifactHelmetLabel = new JLabel();
        artifactHelmetLabel.setIcon(Util.helmet);
        heroArtifactPanel.add(artifactHelmetLabel, BorderLayout.SOUTH);
        artifactHelmetLabel.setVisible(false);

        heroPanel.add(heroArtifactPanel, new GridBagConstraints(2, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(1,1,1,1), 0, 0));

        JButton jButtonLeft = new JButton("West");
        JButton jButtonRight = new JButton("East");
        JButton jButtonUp = new JButton("North");
        JButton jButtonDown = new JButton("South");
        JButton jButtonSwap = new JButton("CONSOLE");

        jButtonLeft.addActionListener(this);
        jButtonRight.addActionListener(this);
        jButtonUp.addActionListener(this);
        jButtonDown.addActionListener(this);
        jButtonSwap.addActionListener(this);

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(jButtonLeft, BorderLayout.SOUTH);
        buttonPanel.add(jButtonRight, BorderLayout.LINE_END);
        buttonPanel.add(jButtonUp, BorderLayout.NORTH);
        buttonPanel.add(jButtonDown, BorderLayout.EAST);
        buttonPanel.add(jButtonSwap, BorderLayout.EAST);

        heroPanel.add(buttonPanel, new GridBagConstraints(3, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
                GridBagConstraints.PAGE_END, GridBagConstraints.HORIZONTAL, new Insets(1,1,1,1), 0, 0));

        jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, enemyPanel, mapPanel);
        jSplitPane.setDividerLocation(280);

        JSplitPane jSplitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jSplitPane, heroPanel);
        jSplitPane2.setDividerLocation(980);

        console = new JTextArea("Welcome!\n");
        console.setEditable(false);
        console.addKeyListener(this);

        JScrollPane scroll = new JScrollPane(
                console,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        JSplitPane jSplitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jSplitPane2, scroll);
        jSplitPane3.setDividerLocation(jFrame.getHeight() - 200);

        jFrame.add(jSplitPane3);
    }


    @Override
    public void showMap(GameMap gameMap, Integer heroPositionX, Integer heroPositionY) {

        if (buttonBorder == null || buttonBorder.getRowCount() != gameMap.getDimension()) {
            if (buttonBorder != null) {
                mapPanel.removeAll();
            }
            buttonBorder = new ButtonBorder(gameMap.getDimension(), gameMap.getMap(), heroPositionX, heroPositionY, mapPanel, gameMapSize);
            jSplitPane.setDividerLocation(280);
        } else {
            buttonBorder.refreshValues(gameMap.getMap(), heroPositionX, heroPositionY);
        }
    }

    @Override
    public void showHeroInfo(Hero hero) {
        heroInfoLabel.setText(hero.getHtmlInfo(true));
        heroInfoLabel.setIcon(hero.getIcon());
        heroInfoLabel.setVisible(true);

        jProgressHp.setVisible(true);
        jProgressHp.setMinimum(0);
        jProgressHp.setMaximum(hero.getHp());
        jProgressHp.setValue(hero.getCurrentHp());

        jProgressExp.setVisible(true);
        jProgressExp.setMinimum(Util.getLevelExperience(hero.getLevel()));
        jProgressExp.setMaximum(Util.getLevelExperience(hero.getLevel() + 1));
        jProgressExp.setValue(hero.getExperience());

        for (Artifact artifact : hero.getArtifacts()) {
            if (artifact.getType().equals("Weapon")) {
                artifactAttackLabel.setText("Attack +".concat(artifact.getAttack().toString()));
                artifactAttackLabel.setVisible(true);
            } else if (artifact.getType().equals("Armor")) {
                artifactDefenceLabel.setText("Defence +".concat(artifact.getDefence().toString()));
                artifactDefenceLabel.setVisible(true);
            } else if (artifact.getType().equals("Helmet")) {
                artifactHelmetLabel.setText("HP +".concat(artifact.getHp().toString()));
                artifactHelmetLabel.setVisible(true);
            }
        }
    }

    @Override
    public void showEvent(String eventText) {
        console.append(eventText);
        console.append("\n");
        console.setCaretPosition(console.getDocument().getLength());
    }

    @Override
    public boolean checkNewArtifact(Artifact artifact) {
        if (artifact != null) {
            showEvent("Enemy drop artifact: " + artifact);
            showEvent("Take it?");

            enemyInfoLabel.setText(artifact.toHtml());
            enemyInfoLabel.setIcon(artifact.getIcon());

            int n = JOptionPane.showConfirmDialog(
                    enemyPanel,
                    "Put on an artifact?",
                    "New artifact found!",
                    JOptionPane.YES_NO_OPTION);

            if (n == 0) {
                enemyInfoLabel.setText("");
                enemyInfoLabel.setIcon(null);
                return true;
            } else {
                enemyInfoLabel.setText("");
                enemyInfoLabel.setIcon(null);
                return false;
            }
        }
        return false;
    }

    @Override
    public Hero pickHeroOrCreate(List<Hero> heroes) {

        heroInfoLabel.setVisible(false);
        artifactHelmetLabel.setVisible(false);
        artifactDefenceLabel.setVisible(false);
        artifactAttackLabel.setVisible(false);
        jProgressExp.setVisible(false);
        jProgressHp.setVisible(false);

        int pickHeroOrCreateValue = -1;

        while (pickHeroOrCreateValue == -1) {
            pickHeroOrCreateValue = pickHeroOrCreateMessage();
            if (pickHeroOrCreateValue == 0) {
                PickHeroPanel pickHeroPanel = new PickHeroPanel(heroes);
                Hero hero = pickHeroPanel.waitPickHero();
                if (hero != null)
                    return hero;
                pickHeroOrCreateValue = -1;

            } else if (pickHeroOrCreateValue == 1) {

                Hero hero = createHeroGui();
                if (hero != null) return hero;
                pickHeroOrCreateValue = -1;
            }
        }
        return null;
    }

    private Hero createHeroGui() {
        int result = 0;

        while (result == 0) {
            JTextField heroNameField = new JTextField(20);
            JPanel myPanel = new JPanel();
            myPanel.add(new JLabel("Name:"));
            myPanel.add(heroNameField);
            myPanel.add(Box.createHorizontalStrut(15));
            myPanel.add(new JLabel("Class:"));

            DefaultListModel listModel = new DefaultListModel();
            listModel.addElement("WARRIOR");
            listModel.addElement("ARCHER");
            listModel.addElement("WIZARD");

            JList list = new JList(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setSelectedIndex(0);

            myPanel.add(list);

            result = JOptionPane.showConfirmDialog(
                    null,
                    myPanel,
                    "Create new hero",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (result == JOptionPane.OK_OPTION) {
                Hero hero = createHero(
                        heroNameField.getText(),
                        listModel.getElementAt(list.getSelectedIndex()).toString()
                );
                if (hero != null) return hero;
            }
        }
        return null;
    }

    private int pickHeroOrCreateMessage() {
        Object[] options = {"Pick", "Create"};
        int pickHeroOrCreateMessageValue;
        pickHeroOrCreateMessageValue = JOptionPane.showOptionDialog(mapPanel,
                "Create a hero or continue?",
                "Hero",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (pickHeroOrCreateMessageValue == -1)
            exit(0);
        return pickHeroOrCreateMessageValue;
    }

    private Hero createHero(String heroName, String heroClass) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Hero hero = new Hero(heroName, heroClass);
        Set<ConstraintViolation<Hero>> validate = validator.validate(hero);
        if (validate.size() == 0)
            return hero;

        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<Hero> heroConstraintViolation : validate) {
            errors.add(heroConstraintViolation.getMessage());
        }
        String[] strings = errors.toArray(new String[errors.size()]);

        JOptionPane.showMessageDialog(null,
                strings,
                "Error input",
                JOptionPane.ERROR_MESSAGE);
        return null;
    }

    @Override
    public String move() {
        console.requestFocus();

        String returnValue = null;

        while (step == null) {
        }

        switch (step) {
            case "West":
                returnValue = "A";
                break;
            case "East":
                returnValue = "D";
                break;
            case "North":
                returnValue = "W";
                break;
            case "South":
                returnValue = "S";
                break;
            case "SWAP":
                returnValue = "SWAP";
                break;
        }
        step = null;
        return returnValue;
    }

    @Override
    public boolean metTheEnemy(Enemy enemy, Hero hero) {

        enemyInfoLabel.setText(enemy.getHtml());
        enemyInfoLabel.setIcon(Util.pawn);
        showEvent("You met the enemy. Fight?");

        int n = JOptionPane.showConfirmDialog(
                enemyPanel,
                "Want to start a fight?",
                "Choose your destiny!",
                JOptionPane.YES_NO_OPTION);

        //TODO -1 если крестик кликнут

        enemyInfoLabel.setText("");
        enemyInfoLabel.setIcon(null);
        return n == 0;
    }

    public void setVisibleFrame(boolean isVisibleFrame) {
        jFrame.setVisible(isVisibleFrame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("CONSOLE"))
            step = "SWAP";
        else
            step = e.getActionCommand();
    }

    @Override
    public void luckInfo(String eventText) {
        showEvent(eventText);
        JOptionPane.showMessageDialog(enemyPanel,
                eventText,
                "Luck info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showFightResult(String eventText) {
        showEvent(eventText);
        JOptionPane.showMessageDialog(mapPanel,
                eventText,
                "Fight result info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void wonMessage(String s) {
        showEvent(s);
        JOptionPane.showMessageDialog(mapPanel,
                s,
                "You won!",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'd' || e.getKeyCode() == 39) {
            step = "East";
        } else if (e.getKeyChar() == 's' || e.getKeyCode() == 40) {
            step = "South";
        } else if (e.getKeyChar() == 'a' || e.getKeyCode() == 37) {
            step = "West";
        } else if (e.getKeyChar() == 'w' || e.getKeyCode() == 38) {
            step = "North";
        } else if (e.getKeyChar() == 'c') {
            step = "SWAP";
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
