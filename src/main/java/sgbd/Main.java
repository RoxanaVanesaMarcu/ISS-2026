package sgbd;

import service.AutentificareService;
import service.GestiuneService;
import UI.LoginUI;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {

        }


        AutentificareService authService = new AutentificareService();
        GestiuneService gestiuneService = new GestiuneService();


        SwingUtilities.invokeLater(() -> {

            LoginUI loginFrame = new LoginUI(authService, gestiuneService);
            loginFrame.setVisible(true);
        });
    }
}