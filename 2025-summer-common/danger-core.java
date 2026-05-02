import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Solution {

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

    private static int calculateMexFromRange(int[] a, int start, int end) {
        if (start > end) {
            return 0;
        }
        int length = end - start + 1;
        boolean[] seen = new boolean[length + 1];
        for (int i = start; i <= end; i++) {
            if (a[i] >= 0 && a[i] < seen.length) {
                seen[a[i]] = true;
            }
        }
        int mex = 0;
        while (mex < seen.length && seen[mex]) {
            mex++;
        }
        return mex;
    }

    private static boolean check(int V, int n, int[] a) {
        int l0 = -1, r0 = -1;
        for (int i = 0; i < n; i++) {
            if (a[i] > V) {
                if (l0 == -1) {
                    l0 = i;
                }
                r0 = i;
            }
        }

        if (l0 == -1) {
            boolean hasNonZero = false;
            for (int x : a) {
                if (x != 0) {
                    hasNonZero = true;
                    break;
                }
            }
            return hasNonZero || V >= 1;
        } else {
            int m = calculateMexFromRange(a, l0, r0);
            return m <= V;
        }
    }

    public static void solve() {
        FastReader sc = new FastReader();
        PrintWriter out = new PrintWriter(System.out);
        int t = sc.nextInt();
        while (t-- > 0) {
            int n = sc.nextInt();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = sc.nextInt();
            }

            if (n == 1) {
                int mex = (a[0] == 0) ? 1 : 0;
                out.println(mex + " " + mex + " " + mex + " " + mex);
                continue;
            }

            int low = 0, high = n;
            int minMax = n;
            while (low <= high) {
                int mid = low + (high - low) / 2;
                if (check(mid, n, a)) {
                    minMax = mid;
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }

            int mex_a = calculateMexFromRange(a, 0, n - 1);
            int max_a = 0;
            for (int x : a) {
                if (x > max_a) {
                    max_a = x;
                }
            }
            int maxMax = Math.max(max_a, mex_a);
            int minMin = 0;
            int maxMin = mex_a;

            out.println(minMax + " " + maxMax + " " + minMin + " " + maxMin);
        }
        out.flush();
    }

    public static void main(String[] args) {
        solve();
    }
}
