package fcup.pdm.myapp.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * The VideoConverter class provides a utility for converting video files to HLS format using FFmpeg.
 */
public class VideoConverter {

    private static final Logger logger = LogManager.getLogger(DBConnection.class);

    /**
     * Converts a video file to HLS format.
     *
     * @param inputFilePath The path to the input video file.
     * @param movieId       The unique identifier for the movie.
     * @param resolution    The desired resolution for the HLS output.
     * @return true if the conversion is successful, false otherwise.
     */
    public static boolean convertToHLS(String inputFilePath, int movieId, String resolution) {
        logger.info("Creating the .m3u8 file for the movieId " + movieId + " with the resolution " + resolution);

        String outputDirectory = AppConstants.HLS_OUTPUT_PATH;
        String outputFileName = movieId + "_" + resolution;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("ffmpeg", "-i", inputFilePath, "-codec: copy", "-start_number", "0", "-hls_time", "10", "-hls_list_size", "0", "-f", "hls", outputDirectory + "/" + outputFileName + ".m3u8");

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            logger.info("Successfully created the .m3u8 file for the movieId " + movieId + " with the resolution " + resolution);
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            logger.error("Error closing Cassandra session", e);
            return false;
        }
    }
}

