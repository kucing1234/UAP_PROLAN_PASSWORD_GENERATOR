import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class FileUtil {

    private static final String FILE = "password_data.csv";

    // READ
    public static ArrayList<PasswordData> readData() {
        ArrayList<PasswordData> list = new ArrayList<>();
        File file = new File(FILE);

        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                list.add(new PasswordData(
                        Integer.parseInt(d[0]),
                        d[1],
                        d[2],
                        LocalDate.parse(d[3])
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // WRITE (Create / Update / Delete)
    public static void writeData(ArrayList<PasswordData> list) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            pw.println("id,akun,password,tanggal");
            for (PasswordData p : list) {
                pw.println(p.id + "," + p.akun + "," + p.password + "," + p.tanggal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
