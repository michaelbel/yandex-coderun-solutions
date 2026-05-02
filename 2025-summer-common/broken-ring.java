import java.io.*;
import java.util.*;

public class Solution {
    long calculateAnswer(int n, int[] a, int[] b) {
        boolean[] vis = new boolean[n];
        long total = 0;
        int cycles = 0;

        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                cycles++;
                int v = i;
                long mn = Long.MAX_VALUE;
                while (!vis[v]) {
                    vis[v] = true;
                    if (b[v] < mn) mn = b[v];
                    v = a[v] - 1;
                }
                total += mn;
            }
        }

        return cycles == 1 ? 0L : total;
    }

    public void solve() throws IOException {
        try (var input = new FastInput(System.in)) {
            try (var out = new PrintWriter(System.out)) {
                int t = input.readInt();
                for (int test = 0; test < t; ++test) {
                    int n = input.readInt();
                    int[] a = input.readIntArray(n);
                    int[] b = input.readIntArray(n);
                    long answer = calculateAnswer(n, a, b);
                    out.println(answer);
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
            try {
                return in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public String readToken() {
            while (!tok.hasMoreTokens()) {
                String nextLine = readLine();
                if (nextLine == null) return null;
                tok = new StringTokenizer(nextLine);
            }
            return tok.nextToken();
        }

        public int readInt() {
            return Integer.parseInt(readToken());
        }

        public int[] readIntArray(int size) {
            int[] array = new int[size];
            for (int i = 0; i < size; ++i) array[i] = readInt();
            return array;
        }

        @Override
        public void close() throws IOException {
            in.close();
        }
    }

    public static void main(String[] args) throws Exception {
        new Solution().solve();
    }
}
