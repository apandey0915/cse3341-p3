import java.io.*;
import java.util.*;

final class DataInput {
    private static final Deque<Integer> q = new ArrayDeque<>();
    
    static void init(String dataFilename) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFilename))) {
            String s;
            while ((s = br.readLine()) != null) {
                for (String tok : s.trim().split("\\s+")) {
                    if (!tok.isEmpty()) q.addLast(Integer.parseInt(tok));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read data file: " + e.getMessage());
        }
    }

    static int readInt() {
        if (q.isEmpty()) {
            throw new RuntimeException("Runtime error: read() with no remaining input");
        }
        return q.removeFirst();
    }
}