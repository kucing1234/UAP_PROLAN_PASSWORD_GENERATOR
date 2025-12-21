import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

public class MainApp extends JFrame {
    private final JPanel mainPanel;
    private final CardLayout cardLayout;

    private JPanel formPanel, dataPanel;
    private JTextField txtApp, txtUsername, txtLength;

    private JTable table;
    private DefaultTableModel model;

    private final Color colorDark = new Color(30, 40, 30);
    private final Color colorOrange = new Color(255, 140, 0);
    private final Color colorWhite = Color.WHITE;
    private final Color colorGrey = new Color(240, 240, 240);

    private final String fontName = "Comic Sans MS";

    public MainApp() {
        setTitle("GeneratorPassword");
        setSize(700, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        initFormPage();
        initDataPage();

        mainPanel.add(formPanel, "form");
        mainPanel.add(dataPanel, "data");

        add(mainPanel);
        setVisible(true);
    }

    private void initFormPage() {
        formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(colorGrey);

        JPanel header = new JPanel();
        header.setBackground(colorDark);
        header.setPreferredSize(new Dimension(700, 90));
        header.setLayout(new GridBagLayout());

        JLabel title = new JLabel("GENERATOR PASSWORD");
        title.setFont(new Font(fontName, Font.BOLD, 30));
        title.setForeground(colorOrange);
        header.add(title);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(colorWhite);
        center.setBorder(new EmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtApp = styleTextField();
        txtUsername = styleTextField();
        txtLength = styleTextField();

        JLabel lblApp = createLabel("NAMA APLIKASI:");
        JLabel lblUser = createLabel("USERNAME AKUN:");
        JLabel lblPass = createLabel("PANJANG PASSWORD (ANGKA):");

        addToGrid(center, lblApp, 0, 0, gbc);
        addToGrid(center, txtApp, 1, 0, gbc);
        addToGrid(center, lblUser, 0, 1, gbc);
        addToGrid(center, txtUsername, 1, 1, gbc);
        addToGrid(center, lblPass, 0, 2, gbc);
        addToGrid(center, txtLength, 1, 2, gbc);

        JButton btnGenerate = new RoundedButton("DATABASE", colorOrange, colorWhite);
        JButton btnToTable = new RoundedButton("GENERATE & SAVE", colorDark, colorOrange);

        btnToTable.addActionListener(_ -> saveData());
        btnGenerate.addActionListener(_ -> cardLayout.show(mainPanel, "data"));

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(colorWhite);
        btnPanel.setBorder(new EmptyBorder(20, 0, 40, 0));
        btnPanel.add(btnGenerate);
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(btnToTable);

        formPanel.add(header, BorderLayout.NORTH);
        formPanel.add(center, BorderLayout.CENTER);
        formPanel.add(btnPanel, BorderLayout.SOUTH);
    }

    private void initDataPage() {
        dataPanel = new JPanel(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(colorDark);
        header.setPreferredSize(new Dimension(700, 70));
        header.setLayout(new GridBagLayout());

        JLabel title = new JLabel("STORED PASSWORDS");
        title.setForeground(colorOrange);
        title.setFont(new Font(fontName, Font.BOLD, 22));
        header.add(title);

        String[] cols = {"Aplikasi", "Username", "Password"};

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(40);
        table.setFont(new Font(fontName, Font.PLAIN, 14));
        table.setSelectionBackground(colorOrange);
        table.setSelectionForeground(colorWhite);
        table.setShowVerticalLines(false);
        table.setFocusable(false);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row >= 0 && col == 2) {
                    String pass = (String) table.getValueAt(row, col);
                    StringSelection selection = new StringSelection(pass);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, null);
                    JOptionPane.showMessageDialog(dataPanel, "Password copied to clipboard!");
                }
            }
        });

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(colorDark);
        tableHeader.setForeground(colorWhite);
        tableHeader.setFont(new Font(fontName, Font.BOLD, 14));
        tableHeader.setPreferredSize(new Dimension(100, 40));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(colorWhite);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel bottomNav = new JPanel();
        bottomNav.setBackground(colorWhite);
        bottomNav.setBorder(new EmptyBorder(20, 0, 20, 0));

        JButton btnBack = new RoundedButton("BACK", colorDark, colorWhite);
        JButton btnEdit = new RoundedButton("EDIT", colorOrange, colorWhite);
        JButton btnDelete = new RoundedButton("DELETE", new Color(192, 57, 43), colorWhite);

        btnBack.addActionListener(_ -> cardLayout.show(mainPanel, "form"));
        btnDelete.addActionListener(_ -> deleteData());
        btnEdit.addActionListener(_ -> updateData());

        bottomNav.add(btnBack);
        bottomNav.add(Box.createHorizontalStrut(10));
        bottomNav.add(btnEdit);
        bottomNav.add(Box.createHorizontalStrut(10));
        bottomNav.add(btnDelete);

        dataPanel.add(header, BorderLayout.NORTH);
        dataPanel.add(scroll, BorderLayout.CENTER);
        dataPanel.add(bottomNav, BorderLayout.SOUTH);
    }

    private void saveData() {
        if (txtApp.getText().isEmpty() || txtUsername.getText().isEmpty() || txtLength.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap isi semua kolom!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int len = Integer.parseInt(txtLength.getText());
            if (len <= 0) {
                JOptionPane.showMessageDialog(this, "Panjang harus lebih dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String pass = generateRandomPass(len);
            model.addRow(new Object[]{txtApp.getText(), txtUsername.getText(), pass});

            JOptionPane.showMessageDialog(this, "Data tersimpan!");
            clearForm();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Panjang harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang mau diedit dulu.");
            return;
        }

        String currentApp = (String) model.getValueAt(row, 0);
        String currentUser = (String) model.getValueAt(row, 1);

        String newApp = JOptionPane.showInputDialog(this, "Edit Nama Aplikasi:", currentApp);
        String newUser = JOptionPane.showInputDialog(this, "Edit Username:", currentUser);

        if (newApp != null && newUser != null && !newApp.isEmpty() && !newUser.isEmpty()) {
            model.setValueAt(newApp, row, 0);
            model.setValueAt(newUser, row, 1);
            JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
        }
    }

    private void deleteData() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.removeRow(row);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel dulu!");
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
        gbc.gridx = x;
        gbc.gridy = y;
        p.add(c, gbc);
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
        txtApp.setText("");
        txtUsername.setText("");
        txtLength.setText("");
    }

    static class RoundedButton extends JButton {
        private final Color bgColor;
        private final Color fgColor;

        public RoundedButton(String text, Color bg, Color fg) {
            super(text);
            this.bgColor = bg;
            this.fgColor = fg;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(fg);
            setFont(new Font("Comic Sans MS", Font.BOLD, 13));
            setPreferredSize(new Dimension(160, 45));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(bgColor.darker());
            } else {
                g2.setColor(bgColor);
            }

            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));

            g2.setColor(fgColor);
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}