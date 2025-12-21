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
        setSize(450, 380);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 10, 8, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        add(new JLabel("Nama Akun"), c);

        c.gridx = 1;
        akunField = new JTextField();
        add(akunField, c);

        c.gridx = 0; c.gridy++;
        add(new JLabel("Panjang Password"), c);

        c.gridx = 1;
        lengthField = new JTextField();
        add(lengthField, c);

        c.gridx = 0; c.gridy++;
        upper = new JCheckBox("Huruf Besar", true);
        add(upper, c);

        c.gridx = 1;
        lower = new JCheckBox("Huruf Kecil", true);
        add(lower, c);

        c.gridx = 0; c.gridy++;
        number = new JCheckBox("Angka");
        add(number, c);

        c.gridx = 1;
        symbol = new JCheckBox("Simbol");
        add(symbol, c);

        c.gridx = 0; c.gridy++;
        add(new JLabel("Hasil Password"), c);

        c.gridx = 1;
        resultField = new JTextField();
        resultField.setEditable(false);
        add(resultField, c);

        c.gridy++;
        c.gridx = 0;
        JButton genBtn = new JButton("Generate");
        add(genBtn, c);

        c.gridx = 1;
        JButton saveBtn = new JButton("Simpan");
        add(saveBtn, c);

        genBtn.addActionListener(e -> generate());
        saveBtn.addActionListener(e -> save());

        setMinimumSize(new Dimension(420, 360));
        setVisible(true);
    }

    private void generate() {
        try {
            int length = Integer.parseInt(lengthField.getText());
            resultField.setText(
                    SecurityUtil.generate(
                            length,
                            upper.isSelected(),
                            lower.isSelected(),
                            number.isSelected(),
                            symbol.isSelected()
                    )
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Panjang harus berupa angka!");
        }
    }

    private void save() {
        if (akunField.getText().isEmpty() || resultField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data belum lengkap!");
            return;
        }

        list.add(new PasswordData(
                FileUtil.getNextId(list),
                akunField.getText(),
                resultField.getText(),
                LocalDate.now()
        ));

        FileUtil.saveData(list);
        JOptionPane.showMessageDialog(this, "Password berhasil disimpan");
        dispose();
    }
}
