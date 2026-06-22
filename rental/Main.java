package rental;

import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        RentalView view = new RentalView();
        RentalController controller = new RentalController(view);
        view.setVisible(true);
    }
}