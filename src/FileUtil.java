import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class FileUtil {

    private static final String FILE_NAME = "password_data.csv";

    public static ArrayList<PasswordData> loadData() {
        ArrayList<PasswordData> list = new ArrayList<>();

        File file = new File(FILE_NAME);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                int id = Integer.parseInt(data[0]);
                String akun = data[1];
                String password = data[2].replace("\"", "");
                LocalDate tanggal = LocalDate.parse(data[3]);

                list.add(new PasswordData(id, akun, password, tanggal));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void saveData(ArrayList<PasswordData> list) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            pw.println("id,akun,password,tanggal");

            list.sort(Comparator.comparingInt(PasswordData::getId));
            for (PasswordData p : list) {
                pw.println(p.toCSV());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getNextId(ArrayList<PasswordData> list) {
        return list.stream().mapToInt(PasswordData::getId).max().orElse(0) + 1;
    }
}
