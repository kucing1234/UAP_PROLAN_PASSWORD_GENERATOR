import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {

    public MainApp() {
        setTitle("SecurePass Manager");
        setSize(420, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel title = new JLabel("SecurePass Manager", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JButton genBtn = new JButton("Generate Password");
        JButton listBtn = new JButton("Daftar Password");
        JButton exitBtn = new JButton("Keluar");

        Dimension btnSize = new Dimension(220, 40);
        genBtn.setMaximumSize(btnSize);
        listBtn.setMaximumSize(btnSize);
        exitBtn.setMaximumSize(btnSize);

        genBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        listBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(genBtn);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(listBtn);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(exitBtn);

        add(centerPanel, BorderLayout.CENTER);

        genBtn.addActionListener(e -> new GeneratePassword());
        listBtn.addActionListener(e -> new PasswordList());
        exitBtn.addActionListener(e -> System.exit(0));

        setMinimumSize(new Dimension(350, 260));
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainApp();
    }
}
