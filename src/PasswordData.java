import java.time.LocalDate;

public class PasswordData {
    public int id;
    public String akun;
    public String password;
    public LocalDate tanggal;

    public PasswordData(int id, String akun, String password, LocalDate tanggal) {
        this.id = id;
        this.akun = akun;
        this.password = password;
        this.tanggal = tanggal;
    }
}
