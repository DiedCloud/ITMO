package UserInterface.graphics;

import common.LocalizationKeys;
import managers.LocalizationManager;
import modules.InteractiveMode;
import modules.Notifier;
import result.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.Map;

public class StartForm extends JFrame {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton submitLogin;
    private JButton submitRegistration;
    private JTextField loginLoginField;
    private JPasswordField loginPasswordField;
    private JPasswordField passwordField_r1;
    private JPasswordField passwordField_r2;
    private JTextField registrationLoginField;
    private JPanel loginFieldsPanel;
    private JPanel loginButtonPanel;
    private JPanel loginPanel;
    private JPanel registrationPanel;
    private JPanel registrationFieldsPanel;
    private JPanel registrationButtonPanel;
    private JComboBox<String> languageComboBox;
    private JLabel loginLoginLabel;
    private JLabel loginPasswordLabel;
    private JLabel registrationLoginLabel;
    private JLabel registrationPassword1Label;
    private JLabel registrationPassword2Label;
    private JLabel languageLabel;

    private final Map<String, String> localizationMap;
    private final LocalizationManager localizationManager;
    private final InteractiveMode im;
    private String language;
    private Notifier n;

    public StartForm(Map<String, String> localizationMap, InteractiveMode im, Notifier n) {
        language = "en_AU";
        localizationManager = new LocalizationManager(language, language);
        this.localizationMap = localizationMap;
        this.im = im;
        this.n = n;

        setTitle("Music Bands");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(panel1);
        setVisible(true);

        for (String key : this.localizationMap.keySet()) {
            languageComboBox.addItem(key);
        }
        languageComboBox.addItemListener(this::ChangeLanguage);
        languageComboBox.setSelectedItem("Australian-English");

        submitLogin.addActionListener(this::ActionLoginSubmit);
        submitRegistration.addActionListener(this::ActionRegistrationSubmit);
    }

    public void ActionLoginSubmit(ActionEvent e) {
        String login = loginLoginField.getText();
        String password = loginPasswordField.getText();
        Result<?> result = im.loginByText(login, password);
        if (result.isSuccess()) {
            // Вывод сообщения об успешном входе
            JOptionPane.showMessageDialog(this, localizationManager.getLine(result.getMessage()), localizationManager.getLine(LocalizationKeys.SUCCESS), JOptionPane.INFORMATION_MESSAGE);

            this.setVisible(false);
            MainForm form = new MainForm(localizationMap, im, login, language);
            n.addObserver(form);
            form.pack();
        } else {
            // Вывод сообщения об ошибке при неудачном входе
            JOptionPane.showMessageDialog(this,  localizationManager.getLine(result.getMessage()), localizationManager.getLine(LocalizationKeys.AUTH_ERROR), JOptionPane.ERROR_MESSAGE);
        }
    }

    public void ActionRegistrationSubmit(ActionEvent e) {
        String login = registrationLoginField.getText();
        String password1 = passwordField_r1.getText();
        String password2 = passwordField_r2.getText();
        if (!password1.equals(password2)) {
            // Вывод сообщения об ошибке при несовпадении паролей
            JOptionPane.showMessageDialog(this, localizationManager.getLine(LocalizationKeys.DIFFERENT_PASSWORDS),  localizationManager.getLine(LocalizationKeys.AUTH_ERROR), JOptionPane.ERROR_MESSAGE);
        } else {
            Result<?> result = im.registerByText(login, password1);
            if (result.isSuccess()) {
                // Вывод сообщения об успешном входе
                JOptionPane.showMessageDialog(this, localizationManager.getLine(result.getMessage()), localizationManager.getLine(LocalizationKeys.SUCCESS), JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Вывод сообщения об ошибке при неудачном входе
                JOptionPane.showMessageDialog(this, localizationManager.getLine(result.getMessage()), localizationManager.getLine(LocalizationKeys.AUTH_ERROR), JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public void ChangeLanguage(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            ChangeLanguage((String) e.getItem());
        }
    }

    public void ChangeLanguage(String language) {
        this.language = language;
        localizationManager.setLanguage(localizationMap.get(language));
        loginLoginLabel.setText(localizationManager.getLine(LocalizationKeys.ENTER_LOGIN));
        loginPasswordLabel.setText(localizationManager.getLine(LocalizationKeys.ENTER_PASSWORD));
        registrationLoginLabel.setText(localizationManager.getLine(LocalizationKeys.ENTER_LOGIN));
        registrationPassword1Label.setText(localizationManager.getLine(LocalizationKeys.ENTER_PASSWORD));
        registrationPassword2Label.setText(localizationManager.getLine(LocalizationKeys.ENTER_PASSWORD));
        languageLabel.setText(localizationManager.getLine(LocalizationKeys.LANGUAGE));
        submitLogin.setText(localizationManager.getLine(LocalizationKeys.SUBMIT));
        submitRegistration.setText(localizationManager.getLine(LocalizationKeys.SUBMIT));
        tabbedPane1.setTitleAt(0, localizationManager.getLine(LocalizationKeys.LOGGING_IN));
        tabbedPane1.setTitleAt(1, localizationManager.getLine(LocalizationKeys.REGISTRATION));
    }

}
