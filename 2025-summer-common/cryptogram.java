import java.io.*;
import java.util.*;

public class Solution {
    long calculateAnswer(int n, int k, int[] a) {
        int maxV = n / k;
        if (maxV <= 0) return 0L;

        int[] freq = new int[maxV + 1];
        int total = 0;
        for (int v : a) {
            if (v % k == 0) {
                int x = v / k;
                if (x <= maxV) {
                    freq[x]++;
                    total++;
                }
            }
        }
        if (total < 2) return 0L;

        int[] mu = mobius(maxV);

        long[] cnt = new long[maxV + 1];
        for (int d = 1; d <= maxV; d++) {
            long s = 0;
            for (int m = d; m <= maxV; m += d) s += freq[m];
            cnt[d] = s;
        }

        long ans = 0;
        for (int d = 1; d <= maxV; d++) {
            long c = cnt[d];
            if (c >= 2 && mu[d] != 0) {
                ans += (long) mu[d] * (c * (c - 1) / 2);
            }
        }
        return ans;
    }

    private int[] mobius(int n) {
        int[] mu = new int[n + 1];
        boolean[] comp = new boolean[n + 1];
        int[] primes = new int[n + 1];
        int pc = 0;

        mu[1] = 1;
        for (int i = 2; i <= n; i++) {
            if (!comp[i]) {
                primes[pc++] = i;
                mu[i] = -1;
            }
            for (int j = 0; j < pc; j++) {
                int p = primes[j];
                long v = (long) i * p;
                if (v > n) break;
                int ip = (int) v;
                comp[ip] = true;
                if (i % p == 0) {
                    mu[ip] = 0;
                    break;
                } else {
                    mu[ip] = -mu[i];
                }
            }
        }
        return mu;
    }

    public void solve() throws IOException {
        try (var input = new FastInput(System.in)) {
            try (var out = new PrintWriter(System.out)) {
                int t = input.readInt();
                for (int test = 0; test < t; ++test) {
                    int n = input.readInt();
                    int k = input.readInt();
                    int[] a = input.readIntArray(n);
                    long answer = calculateAnswer(n, k, a);
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
