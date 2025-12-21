import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class GeneratePassword extends JFrame {

    JTextField akunField, lengthField, resultField;
    JCheckBox upper, lower, number, symbol;

    public GeneratePassword() {
        setTitle("Generate Password");
        setSize(420, 380);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel akunLabel = new JLabel("Nama Akun / Layanan");
        JLabel lengthLabel = new JLabel("Panjang Password (Jumlah Karakter)");
        JLabel resultLabel = new JLabel("Hasil Password");

        akunLabel.setBounds(20,20,250,25);
        lengthLabel.setBounds(20,60,300,25);
        resultLabel.setBounds(20,190,200,25);

        akunField = new JTextField();
        lengthField = new JTextField();
        resultField = new JTextField();
        resultField.setEditable(false);

        akunField.setBounds(20,40,360,25);
        lengthField.setBounds(20,80,360,25);
        resultField.setBounds(20,210,360,25);

        upper = new JCheckBox("Huruf Besar (A-Z)");
        lower = new JCheckBox("Huruf Kecil (a-z)");
        number = new JCheckBox("Angka (0-9)");
        symbol = new JCheckBox("Simbol (!@#$)");

        upper.setBounds(20,110,180,25);
        lower.setBounds(200,110,180,25);
        number.setBounds(20,140,180,25);
        symbol.setBounds(200,140,180,25);

        JButton genBtn = new JButton("Generate");
        JButton saveBtn = new JButton("Simpan");

        genBtn.setBounds(80,260,110,30);
        saveBtn.setBounds(220,260,110,30);

        add(akunLabel); add(lengthLabel); add(resultLabel);
        add(akunField); add(lengthField); add(resultField);
        add(upper); add(lower); add(number); add(symbol);
        add(genBtn); add(saveBtn);

        genBtn.addActionListener(e -> generate());
        saveBtn.addActionListener(e -> save());

        setVisible(true);
    }

    private void generate() {
        try {
            int len = Integer.parseInt(lengthField.getText());
            if (len < 6) throw new Exception();

            String chars = "";
            if (upper.isSelected()) chars += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            if (lower.isSelected()) chars += "abcdefghijklmnopqrstuvwxyz";
            if (number.isSelected()) chars += "0123456789";
            if (symbol.isSelected()) chars += "!@#$%^&*";

            if (chars.isEmpty()) throw new Exception();

            Random r = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++)
                sb.append(chars.charAt(r.nextInt(chars.length())));

            resultField.setText(sb.toString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Input tidak valid!");
        }
    }

    private void save() {
        if (akunField.getText().isEmpty() || resultField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data belum lengkap!");
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
        JOptionPane.showMessageDialog(this, "Data tersimpan!");
    }
}
