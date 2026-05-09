package UI;

import domain.*;
import service.GestiuneService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class LibraryUI extends JFrame {
    private final GestiuneService service;
    private final Utilizator loggedInUser;

    private JTable tabelCarti;
    private DefaultTableModel modelCarti;


    private final Color ROZ_FUNDAL = new Color(255, 240, 245);
    private final Color ROZ_TABEL = new Color(255, 182, 193);
    private final Color ROZ_HEADER = new Color(255, 105, 180);
    private final Color ROZ_TEXT = new Color(153, 0, 76);
    private final Font FONT_BOLD = new Font("Times New Roman", Font.BOLD, 14);

    public LibraryUI(Utilizator user, GestiuneService service) {
        this.loggedInUser = user;
        this.service = service;

        configurareFereastra();
        initUI();
        incarcaDateInitial();
    }

    private void configurareFereastra() {
        setTitle("Sistem Biblioteca - " + loggedInUser.getRol() + ": " + loggedInUser.getUsername());
        setSize(1100, 600); // Am mărit puțin lățimea pentru butoanele extra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ROZ_FUNDAL);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(ROZ_FUNDAL);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelCarti = new DefaultTableModel(new String[]{"ID", "Titlu", "Autor", "ISBN", "Stoc", "An"}, 0);
        tabelCarti = creazaTabel(modelCarti);
        panelCentral.add(creazaGrup(tabelCarti, "Inventar Cărți Disponibile"), BorderLayout.CENTER);

        JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelSud.setBackground(ROZ_FUNDAL);

        JButton btnCauta = creazaButon("Caută/Filtrează");
        JButton btnRefresh = creazaButon("Refresh");
        JButton btnLogout = creazaButon("Logout");

        btnCauta.addActionListener(e -> dialogCautare());
        btnRefresh.addActionListener(e -> incarcaDateInitial());
        btnLogout.addActionListener(e -> dispose());

        panelSud.add(btnCauta);
        panelSud.add(btnRefresh);


        if (loggedInUser instanceof Bibliotecar) {
            JButton btnAdauga = creazaButon("Adaugă Carte");
            JButton btnModificaStoc = creazaButon("Modifică Stoc");
            JButton btnSterge = creazaButon("Șterge Carte");
            JButton btnAdaugaUser = creazaButon("Adaugă Utilizator");

            btnAdauga.addActionListener(e -> deschideDialogAdaugare());
            btnModificaStoc.addActionListener(e -> deschideDialogStoc());
            btnSterge.addActionListener(e -> stergeCarteSelectata());
            btnAdaugaUser.addActionListener(e -> deschideDialogAdaugareUser());

            panelSud.add(new JLabel("|"));
            panelSud.add(btnAdauga);
            panelSud.add(btnModificaStoc);
            panelSud.add(btnSterge);
            panelSud.add(new JLabel("|"));
            panelSud.add(btnAdaugaUser);
        }

        panelSud.add(new JLabel("|"));
        panelSud.add(btnLogout);

        add(panelCentral, BorderLayout.CENTER);
        add(panelSud, BorderLayout.SOUTH);
    }



    private void deschideDialogAdaugareUser() {
        JTextField fUser = new JTextField();
        JTextField fEmail = new JTextField();
        JPasswordField fPass = new JPasswordField();
        String[] roluri = {"Cititor", "Bibliotecar"};
        JComboBox<String> comboRol = new JComboBox<>(roluri);

        Object[] msg = {
                "Username Nou:", fUser,
                "Email:", fEmail,
                "Parolă:", fPass,
                "Rol Utilizator:", comboRol
        };

        if (JOptionPane.showConfirmDialog(this, msg, "Înregistrare Utilizator (UC-6)", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String username = fUser.getText().trim();
            String email = fEmail.getText().trim();
            String parola = new String(fPass.getPassword());
            String rol = (String) comboRol.getSelectedItem();

            if (username.isEmpty() || parola.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Toate câmpurile (inclusiv email) sunt obligatorii!");
                return;
            }


            boolean succes = service.inregistrareUtilizator(username, email, parola, rol, loggedInUser);

            if (succes) {
                JOptionPane.showMessageDialog(this, "Contul pentru " + username + " a fost creat!");
            } else {
                JOptionPane.showMessageDialog(this, "Eroare: Username-ul ar putea exista deja sau date invalide.");
            }
        }
    }


    private void incarcaDateInitial() {
        modelCarti.setRowCount(0);
        List<Carte> toate = service.obtineToateCartile();
        for (Carte c : toate) {
            modelCarti.addRow(new Object[]{c.getTitlu(), c.getAutor(), c.getIsbn(), c.getStoc(), c.getAnPublicare()});
        }
    }

    private void deschideDialogAdaugare() {
        JTextField fTitlu = new JTextField();
        JTextField fAutor = new JTextField();
        JTextField fIsbn = new JTextField();
        JTextField fStoc = new JTextField();
        JTextField fAn = new JTextField();
        Object[] msg = {"Titlu:", fTitlu, "Autor:", fAutor, "ISBN:", fIsbn, "Stoc:", fStoc, "An:", fAn};
        if (JOptionPane.showConfirmDialog(this, msg, "Adaugă Carte", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                service.adaugaCarte(new Carte(0, fTitlu.getText(), fAutor.getText(), fIsbn.getText(),
                        Integer.parseInt(fStoc.getText()), Integer.parseInt(fAn.getText())), loggedInUser);
                incarcaDateInitial();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Eroare date!"); }
        }
    }

    private void deschideDialogStoc() {
        int row = tabelCarti.getSelectedRow();
        if (row == -1) return;
        String noulStoc = JOptionPane.showInputDialog(this, "Noul stoc:");
        if (noulStoc != null) {
            try {

                service.modificaStoc(row, Integer.parseInt(noulStoc), loggedInUser);
                incarcaDateInitial();
            } catch (Exception ex) {}
        }
    }

    private void dialogCautare() {
        String query = JOptionPane.showInputDialog(this, "Introduceți textul pentru căutare:");
        if (query != null) {
            modelCarti.setRowCount(0);
            for (Carte c : service.cauta("titlu", query)) {
                modelCarti.addRow(new Object[]{c.getTitlu(), c.getAutor(), c.getIsbn(), c.getStoc(), c.getAnPublicare()});
            }
        }
    }

    private void stergeCarteSelectata() {
        int row = tabelCarti.getSelectedRow();
        if (row != -1) {
            service.stergeCarte(row, loggedInUser);
            incarcaDateInitial();
        }
    }



    private JTable creazaTabel(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(ROZ_TABEL);
        table.setGridColor(ROZ_TABEL);
        table.setRowHeight(25);
        JTableHeader header = table.getTableHeader();
        header.setBackground(ROZ_HEADER);
        header.setForeground(Color.WHITE);
        header.setFont(FONT_BOLD);
        return table;
    }

    private JScrollPane creazaGrup(JTable table, String titlu) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ROZ_HEADER, 2), titlu, 0, 0, FONT_BOLD, ROZ_TEXT));
        scroll.setBackground(ROZ_FUNDAL);
        return scroll;
    }

    private JButton creazaButon(String txt) {
        JButton btn = new JButton(txt);
        btn.setBackground(ROZ_TABEL);
        btn.setForeground(ROZ_TEXT);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        return btn;
    }
}