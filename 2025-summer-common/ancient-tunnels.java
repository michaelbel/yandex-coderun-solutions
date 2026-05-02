import java.io.*;
import java.util.*;

public class Solution {
    void calculateAnswer(int n, int[] a) {
        int[] to = new int[n];
        int[] indeg = new int[n];
        boolean[] removed = new boolean[n];
        long[] cnt = new long[n];
        for (int i = 0; i < n; i++) {
            to[i] = a[i] - 1;
            if (a[i] == -1) to[i] = -1;
            if (to[i] != -1) indeg[to[i]]++;
            cnt[i] = 1;
        }

        int[] q = new int[n];
        int h = 0, t = 0;
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q[t++] = i;

        while (h < t) {
            int u = q[h++];
            removed[u] = true;
            int v = to[u];
            if (v != -1) {
                cnt[v] += cnt[u];
                if (--indeg[v] == 0) q[t++] = v;
            }
        }

        long[] ans = new long[n];

        boolean[] inCycle = new boolean[n];
        for (int i = 0; i < n; i++) if (!removed[i] && to[i] != -1) inCycle[i] = true;

        boolean[] seen = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (inCycle[i] && !seen[i]) {
                long sum = 0;
                int v = i;
                ArrayList<Integer> cyc = new ArrayList<>();
                while (!seen[v]) {
                    seen[v] = true;
                    cyc.add(v);
                    sum += cnt[v];
                    v = to[v];
                }
                for (int u : cyc) ans[u] = sum;
            }
        }

        for (int i = 0; i < n; i++) if (!inCycle[i]) ans[i] = cnt[i];

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (i > 0) sb.append(' ');
            sb.append(ans[i]);
        }
        sb.append('\n');
        System.out.print(sb.toString());
    }

    public void solve() throws IOException {
        try (var input = new FastInput(System.in)) {
            try (var out = new PrintWriter(System.out)) {
                int t = input.readInt();
                for (int test = 0; test < t; ++test) {
                    int n = input.readInt();
                    int[] a = input.readIntArray(n);
                    calculateAnswer(n, a);
                }
            }
        }
    }

    static class FastInput implements AutoCloseable {
        BufferedReader in;
        StringTokenizer tok;

        public FastInput(InputStream in) {
            this.in = new BufferedReader(new InputStreamReader(in));
            this.tok = new StringTokenizer("");
        }
        public String readLine() {
            try { return in.readLine(); } catch (IOException e) { throw new RuntimeException(e); }
        }
        public String readToken() {
            while (!tok.hasMoreTokens()) {
                String nextLine = readLine();
                if (nextLine == null) return null;
                tok = new StringTokenizer(nextLine);
            }
            return tok.nextToken();
        }
        public int readInt() { return Integer.parseInt(readToken()); }
        public int[] readIntArray(int size) {
            int[] array = new int[size];
            for (int i = 0; i < size; ++i) array[i] = readInt();
            return array;
        }
        @Override public void close() throws IOException { in.close(); }
    }

    public static void main(String[] args) throws Exception {
        new Solution().solve();
    }
}
