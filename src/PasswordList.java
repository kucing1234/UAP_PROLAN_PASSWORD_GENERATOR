import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PasswordList extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private ArrayList<PasswordData> list;

    public PasswordList() {
        list = FileUtil.loadData();

        setTitle("Daftar Password");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(
                new String[]{"ID", "Akun", "Password", "Tanggal"}, 0
        );

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        loadTable();

        setVisible(true);
    }

    private void loadTable() {
        model.setRowCount(0);
        for (PasswordData p : list) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getAkun(),
                    p.getPassword(),
                    p.getTanggal()
            });
        }
    }
}
