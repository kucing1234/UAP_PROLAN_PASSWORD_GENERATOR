import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.time.LocalDate;
import java.util.Random;

public class PasswordManagerApp extends JFrame {
    private final JPanel mainPanel;
    private final CardLayout cardLayout;
    private JPanel dashboardPanel, formPanel, dataPanel, aboutPanel;

    private JTextField txtApp, txtUsername, txtLength;

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch;

    private final String CSV_FILE = "password_data.csv";

    private final Color colorDark = new Color(30, 40, 30);
    private final Color colorOrange = new Color(255, 140, 0);
    private final Color colorWhite = Color.WHITE;
    private final Color colorGrey = new Color(240, 240, 240);
    private final String fontName = "Comic Sans MS";

    public PasswordManagerApp() {
        setTitle("UAP Password Manager 2025");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        initDashboard();
        initFormPage();
        initDataPage();
        initAboutPage();

        mainPanel.add(dashboardPanel, "dashboard");
        mainPanel.add(formPanel, "form");
        mainPanel.add(dataPanel, "data");
        mainPanel.add(aboutPanel, "about");

        add(mainPanel);

        loadDataFromCSV();

        setVisible(true);
    }

    private void initDashboard() {
        dashboardPanel = new JPanel(new GridBagLayout());
        dashboardPanel.setBackground(colorDark);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("SECURE PASS MANAGER", SwingConstants.CENTER);
        title.setFont(new Font(fontName, Font.BOLD, 32));
        title.setForeground(colorOrange);

        JLabel subtitle = new JLabel("Simpan passwordmu dengan aman (File Based)", SwingConstants.CENTER);
        subtitle.setFont(new Font(fontName, Font.PLAIN, 14));
        subtitle.setForeground(colorWhite);

        JButton btnGoInput = new RoundedButton("TAMBAH PASSWORD BARU", colorOrange, colorWhite);
        JButton btnGoTable = new RoundedButton("LIHAT DATABASE", colorWhite, colorDark);
        JButton btnGoAbout = new RoundedButton("TENTANG APLIKASI", colorGrey, colorDark);
        JButton btnExit = new RoundedButton("KELUAR", new Color(192, 57, 43), colorWhite);

        btnGoInput.addActionListener(e -> cardLayout.show(mainPanel, "form"));
        btnGoTable.addActionListener(e -> cardLayout.show(mainPanel, "data"));
        btnGoAbout.addActionListener(e -> cardLayout.show(mainPanel, "about"));
        btnExit.addActionListener(e -> System.exit(0));

        gbc.gridy = 0; dashboardPanel.add(title, gbc);
        gbc.gridy = 1; dashboardPanel.add(subtitle, gbc);
        gbc.gridy = 2; dashboardPanel.add(Box.createVerticalStrut(30), gbc);
        gbc.gridy = 3; dashboardPanel.add(btnGoInput, gbc);
        gbc.gridy = 4; dashboardPanel.add(btnGoTable, gbc);
        gbc.gridy = 5; dashboardPanel.add(btnGoAbout, gbc);
        gbc.gridy = 6; dashboardPanel.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy = 7; dashboardPanel.add(btnExit, gbc);
    }

    private void initFormPage() {
        formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(colorGrey);

        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(colorDark);
        header.setPreferredSize(new Dimension(800, 80));
        JLabel title = new JLabel("INPUT DATA BARU");
        title.setFont(new Font(fontName, Font.BOLD, 24));
        title.setForeground(colorOrange);
        header.add(title);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(colorWhite);
        center.setBorder(new EmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtApp = styleTextField();
        txtUsername = styleTextField();
        txtLength = styleTextField();

        addToGrid(center, createLabel("NAMA APLIKASI:"), 0, 0, gbc);
        addToGrid(center, txtApp, 1, 0, gbc);
        addToGrid(center, createLabel("USERNAME:"), 0, 1, gbc);
        addToGrid(center, txtUsername, 1, 1, gbc);
        addToGrid(center, createLabel("PANJANG PASS (ANGKA):"), 0, 2, gbc);
        addToGrid(center, txtLength, 1, 2, gbc);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(colorWhite);
        btnPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        JButton btnSave = new RoundedButton("GENERATE & SIMPAN", colorOrange, colorWhite);
        JButton btnBack = new RoundedButton("KEMBALI KE MENU", colorDark, colorWhite);

        btnSave.addActionListener(e -> saveData());
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));

        btnPanel.add(btnBack);
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(btnSave);

        formPanel.add(header, BorderLayout.NORTH);
        formPanel.add(center, BorderLayout.CENTER);
        formPanel.add(btnPanel, BorderLayout.SOUTH);
    }

    private void initDataPage() {
        dataPanel = new JPanel(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        header.setBackground(colorDark);
        header.setPreferredSize(new Dimension(800, 80));

        JLabel title = new JLabel("DATABASE PASSWORD");
        title.setForeground(colorOrange);
        title.setFont(new Font(fontName, Font.BOLD, 20));

        JLabel lblSearch = new JLabel("Cari:");
        lblSearch.setForeground(colorWhite);
        lblSearch.setFont(new Font(fontName, Font.BOLD, 14));

        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font(fontName, Font.PLAIN, 14));

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String text = txtSearch.getText();
                if (text.trim().length() == 0) sorter.setRowFilter(null);
                else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        header.add(title);
        header.add(Box.createHorizontalStrut(30));
        header.add(lblSearch);
        header.add(txtSearch);

        String[] cols = {"Aplikasi", "Username", "Password", "Tanggal Dibuat"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setSelectionBackground(colorOrange);
        table.setSelectionForeground(colorWhite);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String pass = (String) table.getValueAt(row, 2);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(new StringSelection(pass), null);
                        JOptionPane.showMessageDialog(dataPanel, "Password disalin ke clipboard!");
                    }
                }
            }
        });

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(colorDark);
        tableHeader.setForeground(colorWhite);
        tableHeader.setFont(new Font(fontName, Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(table);

        JPanel bottomNav = new JPanel();
        bottomNav.setBackground(colorWhite);
        bottomNav.setBorder(new EmptyBorder(15, 0, 15, 0));

        JButton btnBack = new RoundedButton("MENU UTAMA", colorDark, colorWhite);
        JButton btnEdit = new RoundedButton("EDIT DATA", colorOrange, colorWhite);
        JButton btnDelete = new RoundedButton("HAPUS DATA", new Color(192, 57, 43), colorWhite);

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        btnEdit.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());

        bottomNav.add(btnBack);
        bottomNav.add(Box.createHorizontalStrut(10));
        bottomNav.add(btnEdit);
        bottomNav.add(Box.createHorizontalStrut(10));
        bottomNav.add(btnDelete);

        dataPanel.add(header, BorderLayout.NORTH);
        dataPanel.add(scroll, BorderLayout.CENTER);
        dataPanel.add(bottomNav, BorderLayout.SOUTH);
    }

    private void initAboutPage() {
        aboutPanel = new JPanel(new BorderLayout());
        aboutPanel.setBackground(colorWhite);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(colorWhite);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.insets = new Insets(5,5,5,5);

        JLabel lblTitle = new JLabel("TENTANG APLIKASI");
        lblTitle.setFont(new Font(fontName, Font.BOLD, 28));
        lblTitle.setForeground(colorOrange);

        JTextArea txtInfo = new JTextArea(
                "Aplikasi Manager Password v1.0\n" +
                        "Dibuat untuk memenuhi tugas UAP Pemrograman Lanjut.\n\n" +
                        "Fitur:\n" +
                        "- CRUD Password\n" +
                        "- Penyimpanan File (CSV)\n" +
                        "- Enkripsi Sederhana (Generate Random)\n" +
                        "- Sorting & Searching Data"
        );
        txtInfo.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtInfo.setEditable(false);
        txtInfo.setBackground(colorGrey);
        txtInfo.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JButton btnBack = new RoundedButton("KEMBALI", colorDark, colorWhite);
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));

        gbc.gridy = 0; content.add(lblTitle, gbc);
        gbc.gridy = 1; content.add(Box.createVerticalStrut(20), gbc);
        gbc.gridy = 2; content.add(txtInfo, gbc);
        gbc.gridy = 3; content.add(Box.createVerticalStrut(20), gbc);
        gbc.gridy = 4; content.add(btnBack, gbc);

        aboutPanel.add(content, BorderLayout.CENTER);
    }

    private void saveData() {
        if (txtApp.getText().isEmpty() || txtUsername.getText().isEmpty() || txtLength.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap isi semua kolom!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int len = Integer.parseInt(txtLength.getText());
            if (len <= 0) throw new NumberFormatException();

            String pass = generateRandomPass(len);
            String date = LocalDate.now().toString();

            model.addRow(new Object[]{txtApp.getText(), txtUsername.getText(), pass, date});

            rewriteCSVFile();

            JOptionPane.showMessageDialog(this, "Data tersimpan & tercatat di CSV!");
            clearForm();
            cardLayout.show(mainPanel, "data");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Panjang harus angka positif!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang mau diedit dulu.");
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);

        String currentApp = (String) model.getValueAt(modelRow, 0);
        String currentUser = (String) model.getValueAt(modelRow, 1);

        String newApp = JOptionPane.showInputDialog(this, "Edit Nama Aplikasi:", currentApp);
        String newUser = JOptionPane.showInputDialog(this, "Edit Username:", currentUser);

        if (newApp != null && newUser != null && !newApp.isEmpty() && !newUser.isEmpty()) {
            model.setValueAt(newApp, modelRow, 0);
            model.setValueAt(newUser, modelRow, 1);
            rewriteCSVFile();
            JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
        }
    }

    private void deleteData() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus permanen?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int modelRow = table.convertRowIndexToModel(row);
                model.removeRow(modelRow);
                rewriteCSVFile();
                JOptionPane.showMessageDialog(this, "Data terhapus.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data dulu!");
        }
    }

    private void loadDataFromCSV() {
        File file = new File(CSV_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if(data.length == 4) {
                    model.addRow(data);
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal load data: " + e.getMessage());
        }
    }

    private void rewriteCSVFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String line = model.getValueAt(i, 0) + "," +
                        model.getValueAt(i, 1) + "," +
                        model.getValueAt(i, 2) + "," +
                        model.getValueAt(i, 3);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan file: " + e.getMessage());
        }
    }

    private String generateRandomPass(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789#@!";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for(int i=0; i<length; i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    private void addToGrid(JPanel p, Component c, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x; gbc.gridy = y; p.add(c, gbc);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font(fontName, Font.BOLD, 12));
        lbl.setForeground(colorDark);
        return lbl;
    }

    private JTextField styleTextField() {
        JTextField tf = new JTextField(20);
        tf.setFont(new Font(fontName, Font.PLAIN, 14));
        tf.setPreferredSize(new Dimension(200, 30));
        return tf;
    }

    private void clearForm() {
        txtApp.setText(""); txtUsername.setText(""); txtLength.setText("");
    }
}