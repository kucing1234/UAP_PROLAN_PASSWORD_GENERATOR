import java.time.LocalDate;

public class PasswordData {
    private int id;
    private String akun;
    private String password;
    private LocalDate tanggal;

    public PasswordData(int id, String akun, String password, LocalDate tanggal) {
        this.id = id;
        this.akun = akun;
        this.password = password;
        this.tanggal = tanggal;
    }

    public int getId() {
        return id;
    }

    public String getAkun() {
        return akun;
    }

    public void setAkun(String akun) {
        this.akun = akun;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public String toCSV() {
        return id + "," + akun + ",\"" + password + "\"," + tanggal;
    }
}
