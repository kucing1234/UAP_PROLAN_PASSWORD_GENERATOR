import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class FileUtil {
    private static final String FILE = "password_data.csv";

    public static ArrayList<PasswordData> readData() {
        ArrayList<PasswordData> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                list.add(new PasswordData(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2],
                        LocalDate.parse(data[3])
                ));
            }
        } catch (Exception e) {
            System.out.println("File belum ada.");
        }
        return list;
    }

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
