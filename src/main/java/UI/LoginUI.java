package UI;

import domain.*;
import service.AutentificareService;
import service.GestiuneService;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {
    private final AutentificareService authService;
    private final GestiuneService gestiuneService;

    private JTextField fieldUser;
    private JPasswordField fieldPass;
    private JComboBox<String> comboRolLogin; // Rol pentru Autentificare

    private final Color ROZ_FUNDAL = new Color(255, 240, 245);
    private final Color ROZ_BUTON = new Color(255, 182, 193);
    private final Color ROZ_TEXT = new Color(153, 0, 76);
    private final Font FONT_NORMAL = new Font("Times New Roman", Font.PLAIN, 14);

    public LoginUI(AutentificareService authService, GestiuneService gestiuneService) {
        this.authService = authService;
        this.gestiuneService = gestiuneService;

        configurareFereastra();
        initUI();
    }

    private void configurareFereastra() {
        setTitle("Biblioteca - Login & Register");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ROZ_FUNDAL);
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblLogin = new JLabel("AUTENTIFICARE", SwingConstants.CENTER);
        lblLogin.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblLogin.setForeground(ROZ_TEXT);
        add(lblLogin, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        add(new JLabel("Utilizator:"), gbc);
        fieldUser = new JTextField(15);
        gbc.gridx = 1; add(fieldUser, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Parolă:"), gbc);
        fieldPass = new JPasswordField(15);
        gbc.gridx = 1; add(fieldPass, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Rol:"), gbc);
        comboRolLogin = new JComboBox<>(new String[]{"Cititor", "Bibliotecar"});
        gbc.gridx = 1; add(comboRolLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JButton btnLogin = creazaButonRoz("Loghează-te");
        add(btnLogin, gbc);


        gbc.gridy = 5;
        add(new JSeparator(), gbc);

        gbc.gridy = 6;
        JButton btnRegister = creazaButonRoz("Creează Cont Nou");
        add(btnRegister, gbc);

        btnLogin.addActionListener(e -> proceseazaLogin());
        btnRegister.addActionListener(e -> deschideDialogInregistrare());
    }

    private void proceseazaLogin() {
        String user = fieldUser.getText();
        String pass = new String(fieldPass.getPassword());
        String rolSelectat = (String) comboRolLogin.getSelectedItem();

        Utilizator u = authService.login(user, pass);

        if (u != null) {

            if (!u.getRol().equalsIgnoreCase(rolSelectat)) {
                JOptionPane.showMessageDialog(this, "Eroare: Acest cont nu are rolul de " + rolSelectat);
                return;
            }


            if (u instanceof Bibliotecar) {
                String cod = JOptionPane.showInputDialog(this, "Cod acces bibliotecă:");
                if (cod != null && ((Bibliotecar) u).verificaCod(cod)) {
                    deschideMainUI(u);
                } else {
                    JOptionPane.showMessageDialog(this, "Cod incorect!");
                }
            } else {
                deschideMainUI(u);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Date incorecte!");
        }
    }

    private void deschideDialogInregistrare() {
        JTextField fUser = new JTextField();
        JTextField fEmail = new JTextField();
        JPasswordField fPass = new JPasswordField();
        String[] optiuniRol = {"Cititor", "Bibliotecar"};
        JComboBox<String> fRol = new JComboBox<>(optiuniRol);
        JTextField fCod = new JTextField();

        Object[] msg = {
                "Username:", fUser,
                "Email:", fEmail,
                "Parolă:", fPass,
                "Alege Rolul:", fRol,
                "Cod Acces (doar dacă ai ales Bibliotecar):", fCod
        };

        if (JOptionPane.showConfirmDialog(this, msg, "Înregistrare", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String rol = (String) fRol.getSelectedItem();
            String cod = fCod.getText().trim();



            if (rol.equals("Bibliotecar") && !"biblioteca1234".equals(cod)) {
                JOptionPane.showMessageDialog(this, "Nu poți crea cont de Bibliotecar fără codul corect!");
                return;
            }

            boolean succes = gestiuneService.inregistrareUtilizator(
                    fUser.getText().trim(),
                    fEmail.getText().trim(),
                    new String(fPass.getPassword()),
                    rol,
                    new Bibliotecar(0, "system", "", "")
            );

            if (succes) JOptionPane.showMessageDialog(this, "Cont creat!");
            else JOptionPane.showMessageDialog(this, "Eroare la creare.");
        }
    }

    private void deschideMainUI(Utilizator u) {
        new LibraryUI(u, gestiuneService).setVisible(true);
        this.dispose();
    }

    private JButton creazaButonRoz(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ROZ_BUTON);
        btn.setForeground(ROZ_TEXT);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 14));
        return btn;
    }
}