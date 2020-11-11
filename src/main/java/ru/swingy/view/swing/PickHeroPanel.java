package ru.swingy.view.swing;


import ru.swingy.model.Hero;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

public class PickHeroPanel extends JPanel implements ListSelectionListener {

    private JLabel picture;
    private JPanel myPanel;
    private List<Hero> heroes;
    private Hero pickedHero = null;

    public PickHeroPanel(List<Hero> heroes) {

        myPanel = new JPanel();
        this.heroes = heroes;

        String[] heroNames = new String[heroes.size()];

        for (int i = 0; i < heroes.size(); i++) {
            heroNames[i] = heroes.get(i).getName();
        }

        JList list = new JList(heroNames);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);

        JScrollPane listScrollPane = new JScrollPane(list);
        picture = new JLabel();

        JPanel mainInnerPanel = new JPanel(new BorderLayout());
        mainInnerPanel.add(picture, BorderLayout.CENTER);

        JScrollPane pictureScrollPane = new JScrollPane(mainInnerPanel);

        //Create a split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, pictureScrollPane);
        splitPane.setDividerLocation(150);

        //Provide a preferred size for the split pane.
        splitPane.setPreferredSize(new Dimension(700, 400));
        updateLabel(list.getSelectedIndex());

        myPanel.add(splitPane);
        myPanel.setVisible(true);
    }

    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();
        updateLabel(list.getSelectedIndex());
    }

    protected void updateLabel(Integer index) {
        if (heroes != null && heroes.size() > 0 && heroes.size() > index) {
            pickedHero = heroes.get(index);
            picture.setText(prepareHeroText(heroes.get(index)));
            picture.setIcon(heroes.get(index).getIcon());

//        picture.setHorizontalAlignment(JLabel.LEFT);
//        picture.setVerticalAlignment(JLabel.TOP);
//
//        picture.setIconTextGap(50);
//
//        picture.setVerticalTextPosition(JLabel.BOTTOM);
//
//        picture.setHorizontalTextPosition(JLabel.CENTER);


//        picture.setIconTextGap(20);
//        picture.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
    }

    private String prepareHeroText(Hero hero) {
        return hero.getHtmlInfo(false);
    }

    public Hero waitPickHero() {

        int result = JOptionPane.showConfirmDialog(
                null,
                myPanel,
                "Please pick hero",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == 0) {
            return pickedHero;
        } else {
            return null;
        }
    }
}
