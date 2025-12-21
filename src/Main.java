import javax.swing.*;

public class Main extends JFrame {
    public Main() {
        setTitle("SecurePass Manager");
        setSize(300, 250);
        setLayout(null);

        JButton gen = new JButton("Generate Password");
        JButton exit = new JButton("Keluar");

        gen.setBounds(50, 50, 200, 30);
        exit.setBounds(50, 100, 200, 30);

        add(gen); add(exit);

        gen.addActionListener(e -> new GeneratePassword());
        exit.addActionListener(e -> System.exit(0));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
