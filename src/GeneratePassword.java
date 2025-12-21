import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class GeneratePassword extends JFrame {

    private JTextField akunField;
    private JTextField lengthField;
    private JTextField resultField;

    private JCheckBox upper, lower, number, symbol;
    private ArrayList<PasswordData> list;

    public GeneratePassword() {
        list = FileUtil.loadData();

        setTitle("Generate Password");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Nama Akun"));
        akunField = new JTextField();
        add(akunField);

        add(new JLabel("Panjang Password"));
        lengthField = new JTextField();
        add(lengthField);

        upper = new JCheckBox("Huruf Besar", true);
        lower = new JCheckBox("Huruf Kecil", true);
        number = new JCheckBox("Angka");
        symbol = new JCheckBox("Simbol");

        add(upper);
        add(lower);
        add(number);
        add(symbol);

        resultField = new JTextField();
        resultField.setEditable(false);
        add(resultField);

        JButton genBtn = new JButton("Generate");
        JButton saveBtn = new JButton("Simpan");

        add(genBtn);
        add(saveBtn);

        genBtn.addActionListener(e -> generate());
        saveBtn.addActionListener(e -> save());

        setVisible(true);
    }

    private void generate() {
        try {
            int length = Integer.parseInt(lengthField.getText());
            resultField.setText(SecurityUtil.generate(length,
                    upper.isSelected(), lower.isSelected(),
                    number.isSelected(), symbol.isSelected()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Panjang harus angka!");
        }
    }

    private void save() {
        if (akunField.getText().isEmpty() || resultField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data belum lengkap!");
            return;
        }

        int id = FileUtil.getNextId(list);
        list.add(new PasswordData(
                id,
                akunField.getText(),
                resultField.getText(),
                LocalDate.now()
        ));

        FileUtil.saveData(list);
        JOptionPane.showMessageDialog(this, "Password tersimpan!");
        dispose();
    }
}
