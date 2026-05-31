import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {

    static class Edge {
        int to;
        long weight;

        Edge(int to, long weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    static class State implements Comparable<State> {
        int node;
        int usedCoupon;
        long cost;

        State(int node, int usedCoupon, long cost) {
            this.node = node;
            this.usedCoupon = usedCoupon;
            this.cost = cost;
        }

        @Override
        public int compareTo(State other) {
            return Long.compare(this.cost, other.cost);
        }
    }

    public static void main(String[] args) throws Exception {
        System.setIn(new FileInputStream("dados/entrada_do_problema.txt"));

        FastScanner sc = new FastScanner();

        int n = sc.nextInt();
        int m = sc.nextInt();

        List<List<Edge>> graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int u = sc.nextInt() - 1;
            int v = sc.nextInt() - 1;
            long w = sc.nextLong();
            graph.get(u).add(new Edge(v, w));
        }

        long[][] dist = new long[n][2];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Long.MAX_VALUE);
        }

        dist[0][0] = 0;

        PriorityQueue<State> pq = new PriorityQueue<>();
        pq.add(new State(0, 0, 0));

        while (!pq.isEmpty()) {
            State current = pq.poll();
            int u = current.node;
            int state = current.usedCoupon;
            long d = current.cost;

            if (d > dist[u][state]) continue;

            for (Edge edge : graph.get(u)) {
                int v = edge.to;
                long w = edge.weight;

                if (dist[v][state] > d + w) {
                    dist[v][state] = d + w;
                    pq.add(new State(v, state, dist[v][state]));
                }

                if (state == 0) {
                    long discountedWeight = w / 2;
                    if (dist[v][1] > d + discountedWeight) {
                        dist[v][1] = d + discountedWeight;
                        pq.add(new State(v, 1, dist[v][1]));
                    }
                }
            }
        }

        long ans = Math.min(dist[n - 1][0], dist[n - 1][1]);
        System.out.println(ans);
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        public FastScanner() {
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

        long nextLong() {
            return Long.parseLong(next());
        }
    }
}