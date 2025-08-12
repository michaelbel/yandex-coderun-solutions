import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        String[] firstLine = reader.readLine().split(" ");
        int n = Integer.parseInt(firstLine[0]);
        int m = Integer.parseInt(firstLine[1]);
        JSONArray resultOffers = new JSONArray();
        JSONParser parser = new JSONParser();
        int count = 0;
        for (int i = 0; i < n && count < m; i++) {
            String line = reader.readLine();
            JSONObject feed = (JSONObject) parser.parse(line);
            JSONArray offers = (JSONArray) feed.get("offers");
            for (Object offer : offers) {
                if (count >= m) break;
                resultOffers.add(offer);
                count++;
            }
            if (count >= m) break;
        }
        JSONObject result = new JSONObject();
        result.put("offers", resultOffers);
        writer.write(result.toJSONString());
        writer.flush();
        reader.close();
        writer.close();
    }
}
