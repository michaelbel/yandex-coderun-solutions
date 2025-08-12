import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String server = reader.readLine().trim();
        String port = reader.readLine().trim();
        String a = reader.readLine().trim();
        String b = reader.readLine().trim();
        reader.close();
        String urlString = server + ":" + port + "/?a=" + a + "&b=" + b;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(response.toString());
        ArrayList<Integer> positiveNumbers = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            int num = ((Long) jsonArray.get(i)).intValue();
            if (num > 0) {
                positiveNumbers.add(num);
            }
        }
        Collections.sort(positiveNumbers);
        Collections.reverse(positiveNumbers);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        for (Integer num : positiveNumbers) {
            writer.write(num.toString());
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }
}
