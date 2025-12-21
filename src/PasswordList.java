import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Comparator;

public class PasswordList extends JFrame {

    JTable table;
    DefaultTableModel model;
    ArrayList<PasswordData> list;

    public PasswordList() {
        setTitle("Daftar Password");
        setSize(500, 400);
        setLayout(null);

        list = FileUtil.readData();

        model = new DefaultTableModel(new String[]{
                "ID", "Nama Akun", "Password (Encrypted)", "Tanggal"
        }, 0);

        table = new JTable(model);
        loadTable();

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20,20,440,250);

        JButton deleteBtn = new JButton("Hapus");
        JButton sortBtn = new JButton("Sort Tanggal");

        deleteBtn.setBounds(100,300,120,30);
        sortBtn.setBounds(260,300,120,30);

        add(sp); add(deleteBtn); add(sortBtn);

        deleteBtn.addActionListener(e -> deleteData());
        sortBtn.addActionListener(e -> sortData());

        setVisible(true);
    }

    private void loadTable() {
        model.setRowCount(0);
        for (PasswordData p : list) {
            model.addRow(new Object[]{
                    p.id, p.akun, p.password, p.tanggal
            });
        }
    }

    private void deleteData() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            list.remove(row);
            FileUtil.writeData(list);
            loadTable();
        }
    }

    private void sortData() {
        list.sort(Comparator.comparing(p -> p.tanggal));
        loadTable();
    }
}
