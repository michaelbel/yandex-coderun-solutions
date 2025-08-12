import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Main {
    private static final String BASE_URL = "http://127.0.0.1:7777";
    private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder builder;
    static {
        try {
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setExpandEntityReferences(false);
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.err.println("Failed to create DocumentBuilder: " + e.getMessage());
            System.exit(1);
        }
    }
    private static class Submission {
        private final int timestamp;
        private final String problem;
        private final String verdict;
        public Submission(int timestamp, String problem, String verdict) {
            this.timestamp = timestamp;
            this.problem = problem;
            this.verdict = verdict;
        }
        public int getTimestamp() { return timestamp; }
        public String getProblem() { return problem; }
        public String getVerdict() { return verdict; }
    }
    private static class ParticipantResult {
        private final String login;
        private final int score;
        private final int penalty;
        public ParticipantResult(String login, int score, int penalty) {
            this.login = login;
            this.score = score;
            this.penalty = penalty;
        }
        public String getLogin() { return login; }
        public int getScore() { return score; }
        public int getPenalty() { return penalty; }
    }
    private static String makeHttpRequest(String urlString) throws IOException {
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP GET request failed with error code : " + responseCode + " for URL: " + urlString);
            }
            try (InputStream inputStream = connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response.toString();
    }
    private static List<String> getLogins(String contest) throws IOException, SAXException {
        String url = BASE_URL + "/view/participants?contest=" + contest;
        String xmlResponse = makeHttpRequest(url);
        List<String> logins = new ArrayList<>();
        Document doc = builder.parse(new InputSource(new StringReader(xmlResponse)));
        doc.getDocumentElement().normalize();
        NodeList participantNodes = doc.getElementsByTagName("participant");
        for (int i = 0; i < participantNodes.getLength(); i++) {
            Element participantElement = (Element) participantNodes.item(i);
            logins.add(participantElement.getAttribute("login"));
        }
        return logins;
    }
    private static List<Submission> getSubmissions(String contest, String login) throws IOException, SAXException {
        String url = BASE_URL + "/view/submissions?contest=" + contest + "&login=" + login;
        String xmlResponse = makeHttpRequest(url);
        List<Submission> submissions = new ArrayList<>();
        Document doc = builder.parse(new InputSource(new StringReader(xmlResponse)));
        doc.getDocumentElement().normalize();
        NodeList submissionNodes = doc.getElementsByTagName("submission");
        for (int i = 0; i < submissionNodes.getLength(); i++) {
            Element submissionElement = (Element) submissionNodes.item(i);
            int timestamp = Integer.parseInt(submissionElement.getAttribute("timestamp"));
            String problem = submissionElement.getAttribute("problem");
            String verdict = submissionElement.getAttribute("verdict");
            submissions.add(new Submission(timestamp, problem, verdict));
        }
        return submissions;
    }
    public static void main(String[] args) throws IOException, SAXException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        String contest = reader.readLine();
        List<String> logins = getLogins(contest);
        List<ParticipantResult> scoreboard = new ArrayList<>();
        for (String login : logins) {
            List<Submission> submissions = getSubmissions(contest, login);
            submissions.sort(Comparator.comparingInt(Submission::getTimestamp));
            int score = 0;
            int penalty = 0;
            Set<String> solved = new HashSet<>();
            Map<String, Integer> attempts = new HashMap<>();
            for (Submission submission : submissions) {
                String problem = submission.getProblem();
                String verdict = submission.getVerdict();
                if (solved.contains(problem)) continue;
                if ("CE".equals(verdict)) continue;
                if ("OK".equals(verdict)) {
                    score++;
                    solved.add(problem);
                    penalty += submission.getTimestamp();
                    penalty += 20 * attempts.getOrDefault(problem, 0);
                } else {
                    attempts.put(problem, attempts.getOrDefault(problem, 0) + 1);
                }
            }
            scoreboard.add(new ParticipantResult(login, score, penalty));
        }
        scoreboard.sort((p1, p2) -> {
            int scoreCompare = Integer.compare(p2.getScore(), p1.getScore());
            if (scoreCompare != 0) return scoreCompare;
            return Integer.compare(p1.getPenalty(), p2.getPenalty());
        });
        List<String> winnerLogins = new ArrayList<>();
        if (!scoreboard.isEmpty()) {
            ParticipantResult top = scoreboard.get(0);
            int topScore = top.getScore();
            int topPenalty = top.getPenalty();
            winnerLogins.add(top.getLogin());
            for (int i = 1; i < scoreboard.size(); i++) {
                ParticipantResult current = scoreboard.get(i);
                if (current.getScore() == topScore && current.getPenalty() == topPenalty) {
                    winnerLogins.add(current.getLogin());
                } else {
                    break;
                }
            }
        }
        Collections.sort(winnerLogins);
        writer.println(winnerLogins.size());
        for (String winner : winnerLogins) {
            writer.println(winner);
        }
        reader.close();
        writer.close();
    }
}
