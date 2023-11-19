package UserInterface.graphics;

import common.LocalizationKeys;
import common.descriptions.LoadDescription;
import loaders.FromListLoader;
import managers.LocalizationManager;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ArgumentsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel fieldsPanel;
    ArrayList<JLabel> labels;
    ArrayList<JTextField> fields;
    private final LocalizationManager localizationManager;
    private FromListLoader loader;
    private final LoadDescription<?> loadDescription;
    private int y;
    private MainForm mf;

    public ArgumentsDialog(LoadDescription<?> loadDescription, LocalizationManager localizationManager, MainForm mf) {
        super(mf, "Music Bands", true);
        this.localizationManager = localizationManager;
        this.loadDescription = loadDescription;
        this.mf = mf;
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Music Bands");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        labels = new ArrayList<>();
        fields = new ArrayList<>();
        y = 0;
        createFields(loadDescription);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        setContentPane(contentPane);
        pack();
        setVisible(true);
    }

    private void onOK() {
        loader = new FromListLoader(fields.stream().
                map(JTextComponent::getText).
                collect(Collectors.toCollection(ArrayList::new)));
        try {
            loader.enter(loadDescription);
            dispose();
        } catch (Exception e) {
            String s = mf.parseResult(e.getMessage());
            JOptionPane.showMessageDialog(this, localizationManager.getLine(LocalizationKeys.WRONG_VALUE) + "\n" +  s, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        loadDescription.setValue(null);
        dispose();
    }

    private void createFields(LoadDescription<?> ld) {
        GridBagConstraints gbc;
        if (ld.getFields().size() == 0) {
            JLabel label = new JLabel();
            JTextField textField = new JTextField();

            label.setText(localizationManager.getLine(ld.getDescription()));

            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = y++;
            gbc.anchor = GridBagConstraints.WEST;
            fieldsPanel.add(label, gbc);

            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = y++;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            fieldsPanel.add(textField, gbc);

            labels.add(label);
            fields.add(textField);

        } else {
            for (LoadDescription<?> ld_ : ld.getFields()) {
                createFields(ld_);
            }
        }
    }

}