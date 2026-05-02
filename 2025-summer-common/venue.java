import java.io.*;
import java.util.*;

public class Solution {
    class Point {
        public int x, y;
        public Point(int x, int y) { this.x = x; this.y = y; }
    }

    class Pair {
        int first, second;
        Pair(int first, int second) { this.first = first; this.second = second; }
    }

    Pair calculateAnswer(int n, Point[] points) {
        int[] xs = new int[n];
        int[] ys = new int[n];
        int[] id = new int[n];
        for (int i = 0; i < n; i++) {
            xs[i] = points[i].x;
            ys[i] = points[i].y;
            id[i] = i + 1;
        }

        if (n == 2) return new Pair(1, 2);

        for (int iter = 0; iter < 64; iter++) {
            int i0 = -1, i1 = -1;
            for (int i = 0; i < n; i++) {
                int color = (xs[i] + ys[i]) & 1;
                if (color == 0) { if (i0 == -1) i0 = i; }
                else { if (i1 == -1) i1 = i; }
                if (i0 != -1 && i1 != -1) break;
            }
            if (i0 != -1 && i1 != -1) return new Pair(id[i0], id[i1]);

            int parity = (xs[0] + ys[0]) & 1;
            if (parity == 1) {
                for (int i = 0; i < n; i++) xs[i]++;
            }
            for (int i = 0; i < n; i++) {
                long nx = (long) xs[i] + ys[i];
                long ny = (long) xs[i] - ys[i];
                xs[i] = (int) (nx / 2);
                ys[i] = (int) (ny / 2);
            }
        }
        return new Pair(0, 0);
    }

    public void solve() throws IOException {
        try (var input = new FastInput(System.in)) {
            try (var out = new PrintWriter(System.out)) {
                int n = input.readInt();
                Point[] points = new Point[n];
                for (int i = 0; i < n; ++i) {
                    int x = input.readInt();
                    int y = input.readInt();
                    points[i] = new Point(x, y);
                }
                Pair answer = calculateAnswer(n, points);
                out.println(answer.first + " " + answer.second);
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
            try { return in.readLine(); }
            catch (IOException e) { throw new RuntimeException(e); }
        }

        public String readToken() {
            while (!tok.hasMoreTokens()) {
                String nextLine = readLine();
                if (null == nextLine) return null;
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
