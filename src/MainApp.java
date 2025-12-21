import javax.swing.*;

public class MainApp extends JFrame {

    public MainApp() {
        setTitle("SecurePass Manager");
        setSize(300,250);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton genBtn = new JButton("Generate Password");
        JButton listBtn = new JButton("Daftar Password");
        JButton exitBtn = new JButton("Keluar");

        genBtn.setBounds(50,40,200,30);
        listBtn.setBounds(50,90,200,30);
        exitBtn.setBounds(50,140,200,30);

        add(genBtn);
        add(listBtn);
        add(exitBtn);

        genBtn.addActionListener(e -> new GeneratePassword());
        listBtn.addActionListener(e -> new PasswordList());
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainApp();
    }
}
