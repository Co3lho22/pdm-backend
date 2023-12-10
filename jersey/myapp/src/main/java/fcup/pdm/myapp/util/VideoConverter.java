package fcup.pdm.myapp.util;

import fcup.pdm.myapp.dao.MovieLinksCassandraDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The VideoConverter class provides a utility for converting video files to HLS format using FFmpeg.
 */
public class VideoConverter {

    private static final Logger logger = LogManager.getLogger(DBConnection.class);
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * The ConversionCallback interface defines callbacks for the video conversion process.
     */
    public interface ConversionCallback {

        /**
         * Called when video conversion is successful.
         *
         * @param outputFilePath The path to the converted video.
         */
        void onSuccess(String outputFilePath);

        /**
         * Called when video conversion fails.
         *
         * @param e The exception representing the failure.
         */
        void onFailure(Exception e);
    }

    /**
     * Converts a video file to HLS format asynchronously.
     *
     * @param inputFilePath The path to the input video file.
     * @param movieId       The unique identifier for the movie.
     * @param resolution    The desired resolution.
     * @param callback      The callback to handle conversion success or failure.
     */
    public static void convertToHLS(String inputFilePath, int movieId, String resolution, ConversionCallback callback) {
        executorService.submit(() -> {
            MovieLinksCassandraDAO movieLinksCassandraDAO = new MovieLinksCassandraDAO();
            try {
                movieLinksCassandraDAO.updateConversionStatus(movieId, resolution, "pending");
                String outputFilePath = executeFFmpegCommand(inputFilePath, movieId, resolution);
                if (outputFilePath != null) {
                    movieLinksCassandraDAO.updateConversionStatus(movieId, resolution, "completed");
                    callback.onSuccess(outputFilePath);
                } else {
                    movieLinksCassandraDAO.updateConversionStatus(movieId, resolution, "failed");
                    callback.onFailure(new RuntimeException("Conversion failed"));
                }
            } catch (Exception e) {
                movieLinksCassandraDAO.updateConversionStatus(movieId, resolution, "failed");
                callback.onFailure(e);
            }
        });
    }

    /**
     * Executes an FFmpeg command to convert a video file to HLS format.
     *
     * @param inputFilePath The path to the input video file.
     * @param movieId       The unique identifier for the movie.
     * @param resolution    The desired resolution.
     * @return The path to the converted video file in HLS format, or null if conversion fails.
     */
    private static String executeFFmpegCommand(String inputFilePath, int movieId, String resolution) {
        String outputFileName = movieId + "_" + resolution;
        String outputDirectory = AppConstants.HLS_OUTPUT_PATH + "/" + outputFileName;;
        String outputFilePath = outputDirectory + "/" + outputFileName + ".m3u8";

        File subDirectory = new File(outputDirectory);
        if (!subDirectory.exists()) {
            boolean dirCreated = subDirectory.mkdirs();
            if (!dirCreated) {
                logger.error("Failed to create directory: " + outputDirectory);
                return null;
            }
        }

        List<String> command = Arrays.asList("ffmpeg", "-i", inputFilePath, "-codec", "copy", "-start_number", "0",
                "-hls_time", "10", "-hls_list_size", "0", "-f", "hls", outputFilePath);


        logger.info("Executing command: " + String.join(" ", command));

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
            int exitCode = process.waitFor();
            if(exitCode == 0){
                return outputFilePath;
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error executing FFmpeg command", e);
        }
        return null;
    }
}

