import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Main {
    static class Subscription {
        List<List<String>> triggers;
        List<List<String>> shipments;
        Subscription(List<List<String>> triggers, List<List<String>> shipments) {
            this.triggers = triggers;
            this.shipments = shipments;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        String[] header = reader.readLine().split(" ");
        int n = Integer.parseInt(header[0]);
        int m = Integer.parseInt(header[1]);
        List<Subscription> subscriptions = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] parts = reader.readLine().split(" ");
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);
            List<List<String>> triggers = new ArrayList<>();
            List<List<String>> shipments = new ArrayList<>();
            for (int j = 0; j < a; j++) {
                triggers.add(Arrays.asList(parts[2 + j].split("\\.")));
            }
            for (int j = 0; j < b; j++) {
                shipments.add(Arrays.asList(parts[2 + a + j].split("\\.")));
            }
            subscriptions.add(new Subscription(triggers, shipments));
        }
        LinkedHashMap<String, ObjectNode> db = new LinkedHashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < m; i++) {
            ObjectNode msgNode = (ObjectNode) mapper.readTree(reader.readLine());
            String traceId = msgNode.get("trace_id").asText();
            ObjectNode offerPatch = (ObjectNode) msgNode.get("offer");
            String offerId = offerPatch.get("id").asText();
            ObjectNode oldOffer = db.containsKey(offerId) ? db.get(offerId).deepCopy() : mapper.createObjectNode();
            ObjectNode existing = db.getOrDefault(offerId, mapper.createObjectNode());
            mergeObjects(existing, offerPatch, mapper);
            db.put(offerId, existing);
            ObjectNode newOffer = existing;
            List<ObjectNode> results = new ArrayList<>();
            for (int j = 0; j < subscriptions.size(); j++) {
                Subscription sub = subscriptions.get(j);
                boolean triggered = false;
                for (List<String> path : sub.triggers) {
                    JsonNode oldSub = getSubtree(oldOffer, path);
                    JsonNode newSub = getSubtree(newOffer, path);
                    if (changed(oldSub, newSub)) {
                        triggered = true;
                        break;
                    }
                }
                if (triggered) {
                    Set<String> unionPaths = new LinkedHashSet<>();
                    for (List<String> path : sub.triggers) unionPaths.add(String.join(".", path));
                    for (List<String> path : sub.shipments) unionPaths.add(String.join(".", path));
                    ObjectNode resultOffer = mapper.createObjectNode();
                    resultOffer.put("id", offerId);
                    for (String p : unionPaths) {
                        List<String> pathList = Arrays.asList(p.split("\\."));
                        copyPath(newOffer, resultOffer, pathList, mapper);
                    }
                    ObjectNode outNode = mapper.createObjectNode();
                    outNode.put("trace_id", traceId);
                    outNode.set("offer", resultOffer);
                    results.add(outNode);
                }
            }
            for (ObjectNode res : results) {
                writer.write(res.toString());
                writer.newLine();
            }
        }
        writer.flush();
        reader.close();
        writer.close();
    }
    static void mergeObjects(ObjectNode target, ObjectNode patch, ObjectMapper mapper) {
        patch.fieldNames().forEachRemaining(key -> {
            JsonNode value = patch.get(key);
            if (value.isObject()) {
                JsonNode child = target.get(key);
                if (child != null && child.isObject()) {
                    mergeObjects((ObjectNode) child, (ObjectNode) value, mapper);
                } else {
                    target.set(key, value.deepCopy());
                }
            } else {
                target.set(key, value.deepCopy());
            }
        });
    }
    static JsonNode getSubtree(JsonNode node, List<String> path) {
        JsonNode cur = node;
        for (String p : path) {
            if (cur == null || !cur.has(p)) return null;
            cur = cur.get(p);
        }
        return cur;
    }
    static boolean changed(JsonNode oldNode, JsonNode newNode) {
        if (oldNode == null && newNode == null) return false;
        if (oldNode == null || newNode == null) return true;
        return !oldNode.equals(newNode);
    }
    static void copyPath(JsonNode src, ObjectNode dst, List<String> path, ObjectMapper mapper) {
        if (path.isEmpty()) return;
        JsonNode curSrc = src;
        for (String p : path) {
            if (curSrc == null || !curSrc.has(p)) return;
            curSrc = curSrc.get(p);
        }
        if (curSrc == null) return;
        setPath(dst, path, curSrc.deepCopy(), mapper);
    }
    static void setPath(ObjectNode dst, List<String> path, JsonNode value, ObjectMapper mapper) {
        if (path.size() == 1) {
            dst.set(path.get(0), value);
            return;
        }
        String key = path.get(0);
        JsonNode child = dst.get(key);
        ObjectNode childObj;
        if (child != null && child.isObject()) {
            childObj = (ObjectNode) child;
        } else {
            childObj = mapper.createObjectNode();
            dst.set(key, childObj);
        }
        setPath(childObj, path.subList(1, path.size()), value, mapper);
    }
}
