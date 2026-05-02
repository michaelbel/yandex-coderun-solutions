import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Solution {

    public static void main(String[] args) throws IOException {
        new Solution().solve();
    }

    static record Query(int i, int l, int r, int k) {}
    static record Rng(int gcd, int l, int r2) {}

    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }
        return a;
    }

    static class SparseTableGCD {
        private final int[][] rmqGcd;
        private final int[] msbTable;

        public SparseTableGCD(int[] a, int n) {
            int logN = (n == 0) ? 0 : 31 - Integer.numberOfLeadingZeros(n);
            rmqGcd = new int[logN + 1][n];
            msbTable = new int[n + 1];

            if (n > 0) {
                System.arraycopy(a, 0, rmqGcd[0], 0, n);
            }

            for (int h = 1; h <= logN; ++h) {
                for (int i = 0; i + (1 << h) <= n; ++i) {
                    rmqGcd[h][i] = gcd(rmqGcd[h - 1][i], rmqGcd[h - 1][i + (1 << (h - 1))]);
                }
            }

            for (int i = 2; i <= n; ++i) {
                msbTable[i] = msbTable[i / 2] + 1;
            }
        }

        public int getGcd(int l, int r) {
            if (l > r) return 0;
            int len = r - l + 1;
            int msb = msbTable[len];
            return gcd(rmqGcd[msb][l], rmqGcd[msb][r - (1 << msb) + 1]);
        }
    }

    static class SegTree {
        private final int n;
        private final SegNode[] t;
        
        static record SegNode(int cnt, long sumI, long sumA, int minA, int maxA) {
            public SegNode() {
                this(0, 0L, 0L, Integer.MAX_VALUE, Integer.MIN_VALUE);
            }
        }

        public SegTree(int size) {
            int pow2 = 1;
            while (pow2 < size) pow2 *= 2;
            this.n = pow2;
            this.t = new SegNode[2 * n];
            Arrays.fill(t, new SegNode());
        }

        private SegNode pull(SegNode lc, SegNode rc) {
            return new SegNode(
                lc.cnt + rc.cnt,
                lc.sumI + rc.sumI,
                lc.sumA + rc.sumA,
                Math.min(lc.minA, rc.minA),
                Math.max(lc.maxA, rc.maxA)
            );
        }

        public void update(int i, int value) {
            update(1, 0, n - 1, i, value);
        }

        private void update(int x, int lx, int rx, int i, int value) {
            if (lx == rx) {
                t[x] = new SegNode(1, lx, value, value, value);
                return;
            }
            int m = lx + (rx - lx) / 2;
            if (i <= m) {
                update(x * 2, lx, m, i, value);
            } else {
                update(x * 2 + 1, m + 1, rx, i, value);
            }
            t[x] = pull(t[x * 2], t[x * 2 + 1]);
        }
        
        public long query(int ql, int qr) {
             if (ql > qr) return 0L;
             return query(1, 0, n - 1, ql, qr);
        }

        private long query(int x, int lx, int rx, int ql, int qr) {
            if (qr < lx || rx < ql || t[x].cnt == 0) {
                return 0L;
            }
            if (ql <= lx && rx <= qr) {
                SegNode node = t[x];
                if (node.minA > qr) {
                    return (long)(qr + 1) * node.cnt - node.sumI;
                }
                if (node.maxA <= qr) {
                    return node.sumA - node.sumI + node.cnt;
                }
            }
            int m = lx + (rx - lx) / 2;
            return query(x * 2, lx, m, ql, qr) + query(x * 2 + 1, m + 1, rx, ql, qr);
        }
    }

    public void solve() throws IOException {
        try (var input = new FastInput(System.in)) {
            try (var out = new PrintWriter(System.out)) {
                int n = input.readInt();
                int q = input.readInt();
                int[] a = input.readIntArray(n);

                int[] d = new int[n];
                for (int i = 1; i < n; i++) {
                    d[i] = Math.abs(a[i] - a[i - 1]);
                }

                Query[] queries = new Query[q];
                for (int i = 0; i < q; i++) {
                    int l = input.readInt() - 1;
                    int r = input.readInt() - 1;
                    int k = input.readInt();
                    queries[i] = new Query(i, l, r, k);
                }
                Arrays.sort(queries, (q1, q2) -> Integer.compare(q2.k, q1.k));

                SparseTableGCD rmq = new SparseTableGCD(d, n);

                List<Rng> rngs = new ArrayList<>();
                for (int i = 1; i < n; i++) {
                    int j = i;
                    while (j < n) {
                        int g = rmq.getGcd(i, j);
                        if (g == 0) g = Integer.MAX_VALUE;
                        if (g == 1) break;

                        int lo = j, hi = n - 1;
                        int end = j;
                        while (lo <= hi) {
                            int mid = lo + (hi - lo) / 2;
                            int midGcd = rmq.getGcd(i, mid);
                            if (midGcd == 0) midGcd = Integer.MAX_VALUE;
                            
                            if (midGcd >= g) {
                                end = mid;
                                lo = mid + 1;
                            } else {
                                hi = mid - 1;
                            }
                        }
                        rngs.add(new Rng(g, i, end));
                        j = end + 1;
                    }
                }
                rngs.sort((r1, r2) -> Integer.compare(r2.gcd, r1.gcd));

                SegTree tree = new SegTree(n);
                long[] res = new long[q];
                int ri = 0;
                for (int qi = 0; qi < q; ) {
                    int k = queries[qi].k;

                    if (k > 1) {
                        while (ri < rngs.size() && rngs.get(ri).gcd >= k) {
                            Rng rng = rngs.get(ri);
                            tree.update(rng.l, rng.r2);
                            ri++;
                        }
                    }

                    while (qi < q && queries[qi].k == k) {
                        Query query = queries[qi];
                        if (query.l == query.r) {
                            res[query.i] = 0L;
                        } else if (k == 1) {
                            long len = (long)query.r - query.l;
                            res[query.i] = (len + 1) * len / 2;
                        } else {
                            res[query.i] = tree.query(query.l + 1, query.r);
                        }
                        qi++;
                    }
                }

                StringBuilder sb = new StringBuilder();
                for (long ans : res) {
                    sb.append(ans).append("\n");
                }
                out.print(sb);
            }
        }
    }

    static class FastInput implements AutoCloseable {
        private final BufferedReader in;
        private StringTokenizer tok;

        public FastInput(InputStream in) {
            this.in = new BufferedReader(new InputStreamReader(in));
            this.tok = new StringTokenizer("");
        }

        private String readToken() {
            while (!tok.hasMoreTokens()) {
                try {
                    String nextLine = in.readLine();
                    if (null == nextLine) return null;
                    tok = new StringTokenizer(nextLine);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tok.nextToken();
        }

        public int readInt() {
            return Integer.parseInt(readToken());
        }

        public int[] readIntArray(int size) {
            int[] array = new int[size];
            for (int i = 0; i < size; ++i) {
                array[i] = readInt();
            }
            return array;
        }

        @Override
        public void close() throws IOException {
            in.close();
        }
    }
}
