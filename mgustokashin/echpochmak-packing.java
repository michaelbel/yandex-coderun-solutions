import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Main {
    
    private record Pt(int x, int y) {}
		private record Tri(long mask, int[] out) {}

    private static int N, M, S;
    private static Pt[][] mini;
    private static final int INF = 1000;
    private static long FULL;
    private static Map<Long, Integer> memo = new HashMap<>();
    private static Map<Long, Integer> choice = new HashMap<>();
    private static List<Tri> tris = new ArrayList<>();
    private static List<Integer>[] buckets;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        S = 4 * N * M;
        FULL = (S == 64) ? -1L : (1L << S) - 1;

        initializeAtomicTriangles();
        
        long occupied = 0L;
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            Pt a = new Pt(Integer.parseInt(st.nextToken()) * 2, Integer.parseInt(st.nextToken()) * 2);
            Pt b = new Pt(Integer.parseInt(st.nextToken()) * 2, Integer.parseInt(st.nextToken()) * 2);
            Pt c = new Pt(Integer.parseInt(st.nextToken()) * 2, Integer.parseInt(st.nextToken()) * 2);
            occupied |= maskOfTri(a, b, c);
        }

        generateAllPossibleTriangles();
        
        buildBuckets();

        int need = dfs(occupied);
        
        long cur = occupied;
        List<int[]> ans = new ArrayList<>();
        while (cur != FULL) {
            Integer idx = choice.get(cur);
            if (idx == null) {
                break; 
            }
            Tri chosenTri = tris.get(idx);
            ans.add(chosenTri.out());
            cur |= chosenTri.mask();
        }

        System.out.println(need);
        StringBuilder sb = new StringBuilder();
        for (int[] a : ans) {
            sb.setLength(0);
            for (int i = 0; i < a.length; i++) {
                sb.append(a[i]);
                if (i < a.length - 1) {
                    sb.append(" ");
                }
            }
            System.out.println(sb.toString());
        }
    }

    private static void initializeAtomicTriangles() {
        mini = new Pt[S][3];
        for (int j = 0; j < M; j++) {
            for (int i = 0; i < N; i++) {
                Pt bl = new Pt(2 * i, 2 * j);
                Pt br = new Pt(2 * i + 2, 2 * j);
                Pt tl = new Pt(2 * i, 2 * j + 2);
                Pt tr = new Pt(2 * i + 2, 2 * j + 2);
                Pt cc = new Pt(2 * i + 1, 2 * j + 1);
                mini[atomId(i, j, 0)] = new Pt[]{bl, br, cc};
                mini[atomId(i, j, 1)] = new Pt[]{br, tr, cc};
                mini[atomId(i, j, 2)] = new Pt[]{tr, tl, cc};
                mini[atomId(i, j, 3)] = new Pt[]{tl, bl, cc};
            }
        }
    }

    private static void generateAllPossibleTriangles() {
        List<Pt> pts = new ArrayList<>();
        for (int y = 0; y <= M; y++) {
            for (int x = 0; x <= N; x++) {
                pts.add(new Pt(x * 2, y * 2));
            }
        }
        int P = pts.size();
        Set<Long> seenMask = new HashSet<>();
        
        for (int i = 0; i < P; i++) {
            for (int j = i + 1; j < P; j++) {
                for (int l = j + 1; l < P; l++) {
                    Pt a = pts.get(i);
                    Pt b = pts.get(j);
                    Pt c = pts.get(l);

                    if (cross(a, b, c) == 0) continue;

                    if (!axisOrDiag(b.x - a.x, b.y - a.y) || 
                        !axisOrDiag(c.x - b.x, c.y - b.y) || 
                        !axisOrDiag(a.x - c.x, a.y - c.y)) {
                        continue;
                    }
                    
                    long d2_ab = (long)(b.x - a.x) * (b.x - a.x) + (long)(b.y - a.y) * (b.y - a.y);
                    long d2_bc = (long)(c.x - b.x) * (c.x - b.x) + (long)(c.y - b.y) * (c.y - b.y);
                    long d2_ca = (long)(a.x - c.x) * (a.x - c.x) + (long)(a.y - c.y) * (a.y - c.y);

                    if (!(d2_ab == d2_bc || d2_bc == d2_ca || d2_ab == d2_ca)) {
                        continue;
                    }

                    long m = maskOfTri(a, b, c);
                    if (m == 0 || seenMask.contains(m)) continue;
                    
                    seenMask.add(m);
                    int[] out = {a.x() / 2, a.y() / 2, b.x() / 2, b.y() / 2, c.x() / 2, c.y() / 2};
                    tris.add(new Tri(m, out));
                }
            }
        }
    }
    
    private static void buildBuckets() {
        buckets = new ArrayList[S];
        for (int i = 0; i < S; i++) {
            buckets[i] = new ArrayList<>();
        }
        for (int idx = 0; idx < tris.size(); idx++) {
            Tri t = tris.get(idx);
            long m = t.mask();
            while (m != 0) {
                int b = Long.numberOfTrailingZeros(m);
                buckets[b].add(idx);
                m &= m - 1;
            }
        }
    }
    
    private static int dfs(long ms) {
        if (ms == FULL) return 0;
        if (memo.containsKey(ms)) return memo.get(ms);

        int bit = Long.numberOfTrailingZeros(~ms & FULL);

        int best = INF;
        int bestIdx = -1;

        for (int idx : buckets[bit]) {
            Tri t = tris.get(idx);
            if ((t.mask() & ms) != 0) continue;
            
            int cur = 1 + dfs(ms | t.mask());
            if (cur < best) {
                best = cur;
                bestIdx = idx;
                if (best == 1) break;
            }
        }

        memo.put(ms, best);
        if (bestIdx != -1) {
            choice.put(ms, bestIdx);
        }
        return best;
    }

    private static long cross(Pt a, Pt b, Pt c) {
        return (long)(b.x - a.x) * (c.y - a.y) - (long)(b.y - a.y) * (c.x - a.x);
    }

    private static boolean inside(Pt p, Pt a, Pt b, Pt c) {
        long c1 = cross(a, b, p);
        long c2 = cross(b, c, p);
        long c3 = cross(c, a, p);
        return (c1 >= 0 && c2 >= 0 && c3 >= 0) || (c1 <= 0 && c2 <= 0 && c3 <= 0);
    }
    
    private static int atomId(int i, int j, int q) {
        return ((j * N) + i) << 2 | q;
    }

    private static long maskOfTri(Pt a, Pt b, Pt c) {
        long m = 0L;
        for (int t = 0; t < S; t++) {
            if (inside(mini[t][0], a, b, c) && inside(mini[t][1], a, b, c) && inside(mini[t][2], a, b, c)) {
                m |= (1L << t);
            }
        }
        return m;
    }

    private static boolean axisOrDiag(int dx, int dy) {
        return dx == 0 || dy == 0 || Math.abs(dx) == Math.abs(dy);
    }
}
