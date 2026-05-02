import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Solution {

    static final long MOD = 998244353;
    static final long INV2 = 499122177;

    static long modSum(long a, long b) { return (a + b) % MOD; }
    static long modSub(long a, long b) { return (a - b + MOD) % MOD; }
    static long modMul(long a, long b) { return (a * b) % MOD; }
    static long modPow(long a, long x) {
        long ret = 1;
        a %= MOD;
        while (x > 0) {
            if ((x & 1) == 1) ret = (ret * a) % MOD;
            x >>= 1;
            a = (a * a) % MOD;
        }
        return ret;
    }
    static long modInv(long a) { return modPow(a, MOD - 2); }
    static long modDiv(long a, long b) { return modMul(a, modInv(b)); }

    private long[] dp;
    private long[] pSuccessPerMask;
    private Map<Integer, Long>[] pTransition;

    static int strToInt(String s) {
        int x = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '1') {
                x |= (1 << i);
            }
        }
        return x;
    }

    private long rec(int mask) {
        if (dp[mask] != -1) {
            return dp[mask];
        }

        long sumOfTransitions = 0;
        long selfLoopProb = 0;

        if (pTransition[mask] != null) {
            for (Map.Entry<Integer, Long> entry : pTransition[mask].entrySet()) {
                int nextMask = entry.getKey();
                long prob = entry.getValue();
                if (nextMask == mask) {
                    selfLoopProb = prob;
                } else {
                    long expectedStepsNext = rec(nextMask);
                    sumOfTransitions = modSum(sumOfTransitions, modMul(prob, expectedStepsNext));
                }
            }
        }

        long numerator = modSum(1, sumOfTransitions);
        long denominator = modSub(1, selfLoopProb);
        
        return dp[mask] = modDiv(numerator, denominator);
    }
    
    private long probOfSuccess(int s_int, int currentMask, int l, int r) {
        long p_not = 1;
        while (l < r) {
            boolean l_is_random = (currentMask & (1 << l)) == 0;
            boolean r_is_random = (currentMask & (1 << r)) == 0;

            if (l_is_random || r_is_random) {
                p_not = modMul(p_not, INV2);
            } else {
                boolean l_is_one = (s_int & (1 << l)) != 0;
                boolean r_is_one = (s_int & (1 << r)) != 0;
                if (l_is_one == r_is_one) {
                    return 1;
                }
            }
            l++;
            r--;
        }
        return modSub(1, p_not);
    }

    public void solve() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(System.out)) {
            
            String s = br.readLine();
            int n = s.length();
            int s_int = strToInt(s);

            long n_subs = 0;
            for (int len = 2; len <= n; len += 2) {
                n_subs += (n - len + 1);
            }
            long n_subs_inv = modInv(n_subs);

            int n_masks = 1 << n;
            int initMask = (1 << n) - 1;

            pTransition = new HashMap[n_masks];
            pSuccessPerMask = new long[n_masks];

            Queue<Integer> q = new LinkedList<>();
            boolean[] visited = new boolean[n_masks];
            
            q.add(initMask);
            visited[initMask] = true;

            while (!q.isEmpty()) {
                int m1 = q.poll();
                pTransition[m1] = new HashMap<>();

                for (int l = 0; l < n; ++l) {
                    for (int r = l + 1; r < n; r += 2) {
                        long p_succ = probOfSuccess(s_int, m1, l, r);
                        long p_fail = modSub(1, p_succ);

                        pSuccessPerMask[m1] = modSum(pSuccessPerMask[m1], modMul(p_succ, n_subs_inv));
                        
                        if (p_succ == 1) continue;

                        int fixedBitsMask = ((1 << l) - 1) | (~((1 << (r + 1)) - 1));
                        int m2 = m1 & fixedBitsMask;
                        
                        long currentProb = pTransition[m1].getOrDefault(m2, 0L);
                        pTransition[m1].put(m2, modSum(currentProb, modMul(p_fail, n_subs_inv)));

                        if (!visited[m2]) {
                            q.add(m2);
                            visited[m2] = true;
                        }
                    }
                }
            }
            
            dp = new long[n_masks];
            Arrays.fill(dp, -1);
            
            if (pSuccessPerMask[0] != 0) {
                 dp[0] = modInv(pSuccessPerMask[0]);
            } else {
                 dp[0] = 0;
            }

            long result = rec(initMask);
            out.println(result);
        }
    }

    public static void main(String[] args) throws IOException {
        new Solution().solve();
    }
}
