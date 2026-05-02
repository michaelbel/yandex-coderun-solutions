import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.lang.StringBuilder;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine());
        StringBuilder resultBuilder = new StringBuilder();
        while (t-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            resultBuilder.append(new Solver(r, c, a, b).solve()).append("\n");
        }
        System.out.print(resultBuilder.toString());
    }
}

class Solver {
    private int r, c, a, b;
    private char[] dirs = {'L', 'U', 'R', 'D'};
    private StringBuilder path = new StringBuilder();

    public Solver(int r, int c, int a, int b) {
        this.r = r;
        this.c = c;
        this.a = a;
        this.b = b;
    }

    private void append(char ch, int count) {
        for (int i = 0; i < count; i++) {
            path.append(ch);
        }
    }
    
    public String solve() {
        if (a == 1 || (b == c && a != r)) {
            int tempDim = r;
            r = c;
            c = tempDim;

            int tempCoord = a;
            a = b;
            b = tempCoord;

            char tempDir;
            tempDir = dirs[0]; dirs[0] = dirs[1]; dirs[1] = tempDir;
            tempDir = dirs[2]; dirs[2] = dirs[3]; dirs[3] = tempDir;
        }

        if (r == a) {
            if (b > 2) {
                append(dirs[3], r - 1);
                path.append(dirs[2]);

                a = b - 1;
                b = 1;

                int tempDim = r;
                r = c - 1;
                c = tempDim;

                char tempDir = dirs[0];
                dirs[0] = dirs[3];
                dirs[3] = dirs[2];
                dirs[2] = dirs[1];
                dirs[1] = tempDir;
            } else {
                 if (b != 1) {
                    if (c == 2) {
                        path.append(dirs[2]).append(dirs[3]);
                        a--;
                        b = 1;
                        r--;
                        char tempDir = dirs[0]; dirs[0] = dirs[2]; dirs[2] = tempDir;
                        if (a == 1) {
                            path.append(dirs[2]);
                            r = 2; c = 1; a = 2; b = 1;
                            dirs[3] = dirs[0];
                        }
                    } else {
                        append(dirs[2], c - 1);
                        append(dirs[3], r - 1);
                        path.append(dirs[0]);
                        a = b;
                        b = 1;
                        int tempDim = r;
                        r = c - 1;
                        c = tempDim - 1;
                        char tempDir = dirs[0];
                        dirs[0] = dirs[3];
                        dirs[3] = dirs[2];
                        dirs[2] = dirs[1];
                        dirs[1] = tempDir;

                        if (a == r) {
                            path.append(dirs[1]);
                        } else {
                            a = r - a + 1;
                            tempDir = dirs[1]; dirs[1] = dirs[3]; dirs[3] = tempDir;
                        }
                    }
                }
            }
        } else {
            for (int l = 1; l < b; l++) {
                append(dirs[1 + (l % 2 * 2)], r - 1);
                path.append(dirs[2]);
            }
            if (b % 2 == 0) {
                a = r - a + 1;
                char tempDir = dirs[1]; dirs[1] = dirs[3]; dirs[3] = tempDir;
            }
            c = c - b + 1;
            b = 1;
        }

        for (int l = 1 + (a % 2 * 2); l < a; l++) {
            append(dirs[l % 2 * 2], c - 1);
            path.append(dirs[3]);
        }
        if (a % 2 != 0) {
            for (int l = 1; l < c; l++) {
                path.append(dirs[1 + (l % 2 * 2)]).append(dirs[2]);
            }
            if (c % 2 == 0) path.append(dirs[1]);
            if(r > 1) path.append(dirs[3]).append(dirs[3]);
        }

        for (int l = r - a - ((r - a) % 2); l > 0; l--) {
            append(dirs[l % 2 * 2], c - 2);
            path.append(dirs[3]);
        }
        if ((r - a) % 2 == 0) {
            if (c - 1 != 0) append(dirs[0], c - 1);
        } else {
            for (int l = 1; l < c; l++) {
                path.append(dirs[1 + (l % 2 * 2)]).append(dirs[0]);
            }
            if (c % 2 != 0) path.append(dirs[3]);
        }
        if (r - a != 0) append(dirs[1], r - a);

        return path.toString();
    }
}
