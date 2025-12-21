import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class GeneratePassword extends JFrame {

    // Input fields
    private JTextField akunField;
    private JTextField lengthField;
    private JTextField resultField;

    // Checkbox pilihan karakter
    private JCheckBox upper;
    private JCheckBox lower;
    private JCheckBox number;
    private JCheckBox symbol;

    public GeneratePassword() {
        setTitle("Generate Password");
        setSize(420, 380);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ===== LABEL =====
        JLabel akunLabel = new JLabel("Nama Akun / Layanan");
        JLabel lengthLabel = new JLabel("Panjang Password (Jumlah Karakter)");
        JLabel resultLabel = new JLabel("Hasil Password");

        akunLabel.setBounds(20, 20, 200, 25);
        lengthLabel.setBounds(20, 60, 250, 25);
        resultLabel.setBounds(20, 190, 200, 25);

        // ===== INPUT =====
        akunField = new JTextField();
        lengthField = new JTextField();
        resultField = new JTextField();
        resultField.setEditable(false); // output saja

        akunField.setBounds(20, 40, 360, 25);
        lengthField.setBounds(20, 80, 360, 25);
        resultField.setBounds(20, 210, 360, 25);

        // ===== CHECKBOX =====
        upper = new JCheckBox("Huruf Besar (A–Z)");
        lower = new JCheckBox("Huruf Kecil (a–z)");
        number = new JCheckBox("Angka (0–9)");
        symbol = new JCheckBox("Simbol (!@#$%)");

        upper.setBounds(20, 110, 180, 25);
        lower.setBounds(200, 110, 180, 25);
        number.setBounds(20, 140, 180, 25);
        symbol.setBounds(200, 140, 180, 25);

        // ===== BUTTON =====
        JButton generateBtn = new JButton("Generate Password");
        JButton saveBtn = new JButton("Simpan Password");

        generateBtn.setBounds(60, 260, 140, 30);
        saveBtn.setBounds(220, 260, 140, 30);

        // ===== ADD COMPONENT =====
        add(akunLabel);
        add(lengthLabel);
        add(resultLabel);

        add(akunField);
        add(lengthField);
        add(resultField);

        add(upper);
        add(lower);
        add(number);
        add(symbol);

        add(generateBtn);
        add(saveBtn);

        // ===== ACTION =====
        generateBtn.addActionListener(e -> generatePassword());
        saveBtn.addActionListener(e -> savePassword());

        setVisible(true);
    }

    // ===== LOGIC GENERATE PASSWORD =====
    private void generatePassword() {
        try {
            // Validasi panjang password
            int length = Integer.parseInt(lengthField.getText());
            if (length < 6) {
                JOptionPane.showMessageDialog(this,
                        "Panjang password minimal 6 karakter!",
                        "Validasi",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validasi jenis karakter
            String characters = "";
            if (upper.isSelected()) characters += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            if (lower.isSelected()) characters += "abcdefghijklmnopqrstuvwxyz";
            if (number.isSelected()) characters += "0123456789";
            if (symbol.isSelected()) characters += "!@#$%^&*";

            if (characters.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Pilih minimal satu jenis karakter!",
                        "Validasi",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Generate password
            Random random = new Random();
            StringBuilder password = new StringBuilder();

            for (int i = 0; i < length; i++) {
                password.append(
                        characters.charAt(
                                random.nextInt(characters.length())
                        )
                );
            }

            // Tampilkan hasil
            resultField.setText(password.toString());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Panjang password harus berupa angka!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== LOGIC SIMPAN PASSWORD =====
    private void savePassword() {
        if (akunField.getText().isEmpty() || resultField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nama akun dan hasil password tidak boleh kosong!",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        ArrayList<PasswordData> list = FileUtil.readData();
        int id = list.size() + 1;

        list.add(new PasswordData(
                id,
                akunField.getText(),
                SecurityUtil.encrypt(resultField.getText()),
                LocalDate.now()
        ));

        FileUtil.writeData(list);
        JOptionPane.showMessageDialog(this,
                "Password berhasil disimpan !");
    }
}
