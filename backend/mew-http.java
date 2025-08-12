import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Main {
    private static final String SERVER_URL = "http://127.0.0.1:7777/";
    private static List<String> ask(String... variableNames) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(SERVER_URL);
            connection = (HttpURLConnection) url.openConnection();
            try {
                connection.setRequestMethod("MEW");
            } catch (ProtocolException pe) {
                try {
                    java.lang.reflect.Field methodField = HttpURLConnection.class.getDeclaredField("method");
                    methodField.setAccessible(true);
                    methodField.set(connection, "MEW");
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new IOException("Failed to set MEW method using standard or reflection.", pe);
                }
            }
            connection.setDoOutput(false);
            connection.setDoInput(true);
            String headerValue = String.join(", ", variableNames);
            connection.setRequestProperty("X-Cat-Variable", headerValue);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
            }
            List<String> values = new ArrayList<>();
            Map<String, List<String>> headers = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                if (entry.getKey() != null && entry.getKey().equalsIgnoreCase("X-Cat-Value")) {
                    values.addAll(entry.getValue());
                }
            }
            Collections.sort(values);
            return values;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        String a = reader.readLine();
        String b = reader.readLine();
        String c = reader.readLine();
        String d = reader.readLine();
        String aa = "";
        String bb = "";
        String cc = "";
        String dd = "";
        List<String> q1 = ask(a, b);
        List<String> q2 = ask(b, c);
        if (q1.equals(q2)) {
            List<String> q3 = ask(a, c, d);
            if (q3.get(0).equals(q3.get(1))) {
                aa = q3.get(0);
                cc = q3.get(1);
                dd = q3.get(2);
            } else {
                dd = q3.get(0);
                aa = q3.get(1);
                cc = q3.get(2);
            }
            if (aa.equals(q1.get(0))) {
                bb = q1.get(1);
            } else {
                bb = q1.get(0);
            }
        } else {
            if (q1.get(0).equals(q2.get(0))) {
                bb = q1.get(0);
                aa = q1.get(1);
                cc = q2.get(1);
            } else if (q1.get(0).equals(q2.get(1))) {
                bb = q1.get(0);
                aa = q1.get(1);
                cc = q2.get(0);
            } else if (q1.get(1).equals(q2.get(0))) {
                bb = q1.get(1);
                aa = q1.get(0);
                cc = q2.get(1);
            } else {
                bb = q1.get(1);
                aa = q1.get(0);
                cc = q2.get(0);
            }
            List<String> q3 = ask(d);
            dd = q3.get(0);
        }
        writer.write(aa);
        writer.newLine();
        writer.write(bb);
        writer.newLine();
        writer.write(cc);
        writer.newLine();
        writer.write(dd);
        writer.newLine();
        reader.close();
        writer.close();
    }
}
