package fcup.pdm.myapp.util;

import com.datastax.oss.driver.api.core.CqlSession;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class CleanupTask extends TimerTask {

    private CqlSession session;

    public CleanupTask(CqlSession session) {
        this.session = session;
    }

    @Override
    public void run() {
        // Query Cassandra for m3u8 entries older than 10 minutes
        String query = "SELECT path, creation_time FROM m3u8_entries WHERE creation_time < ?";
        var resultSet = session.execute(query, tenMinutesAgo());

        resultSet.forEach(row -> {
            String path = row.getString("path");
            // Delete the m3u8 file and associated video segments
            deleteFiles(path);
            // Remove entry from Cassandra
            session.execute("DELETE FROM m3u8_entries WHERE path = ?", path);
        });
    }

    private void deleteFiles(String path) {
        try {
            // Delete m3u8 file
            Files.deleteIfExists(Paths.get(path));
            // Add logic to delete associated video segments
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long tenMinutesAgo() {
        return System.currentTimeMillis() - 600000; // 10 minutes in milliseconds
    }

    public static void main(String[] args) {
        try (CqlSession session = CqlSession.builder().build()) {
            Timer timer = new Timer();
            // Schedule the task to run every 10 minutes
            timer.schedule(new CleanupTask(session), 0, 600000);
        }
    }
}
