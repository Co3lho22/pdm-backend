package fcup.pdm.myapp.util;

import fcup.pdm.myapp.dao.MovieLinksDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
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

//    /**
//     * Converts a video file to HLS format.
//     *
//     * @param inputFilePath The path to the input video file.
//     * @param movieId       The unique identifier for the movie.
//     * @param resolution    The desired resolution for the HLS output.
//     * @return true if the conversion is successful, false otherwise.
//     */
//    public static String convertToHLS(String inputFilePath, int movieId, String resolution) {
//        String outputDirectory = AppConstants.HLS_OUTPUT_PATH;
//        String outputFileName = movieId + "_" + resolution;
//        String outputFilePath = outputDirectory + "/" + outputFileName + ".m3u8";
//        List<String> command = Arrays.asList("ffmpeg", "-i", inputFilePath, "-codec", "copy", "-start_number", "0",
//                "-hls_time", "10", "-hls_list_size", "0", "-f", "hls", outputFilePath);
//
//        logger.info("Executing command: " + String.join(" ", command));
//
//        ProcessBuilder processBuilder = new ProcessBuilder(command);
//        processBuilder.redirectErrorStream(true);
//
//        try {
//            Process process = processBuilder.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                logger.info(line);
//            }
//            int exitCode = process.waitFor();
//            if(exitCode == 0){
//                return outputFilePath;
//            }
//        } catch (IOException | InterruptedException e) {
//            logger.error("Error executing FFmpeg command", e);
//        }
//        return null;
//    }
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public interface ConversionCallback {
        void onSuccess(String outputFilePath);
        void onFailure(Exception e);
    }

    public static void convertToHLS(String inputFilePath, int movieId, String resolution, ConversionCallback callback) {
        executorService.submit(() -> {
            MovieLinksDAO movieLinksDAO = new MovieLinksDAO();
            try {
                movieLinksDAO.updateConversionStatus(movieId, resolution, "pending");
                String outputFilePath = executeFFmpegCommand(inputFilePath, movieId, resolution);
                if (outputFilePath != null) {
                    movieLinksDAO.updateConversionStatus(movieId, resolution, "completed");
                    callback.onSuccess(outputFilePath);
                } else {
                    movieLinksDAO.updateConversionStatus(movieId, resolution, "failed");
                    callback.onFailure(new RuntimeException("Conversion failed"));
                }
            } catch (Exception e) {
                movieLinksDAO.updateConversionStatus(movieId, resolution, "failed");
                callback.onFailure(e);
            }
        });
    }

    private static String executeFFmpegCommand(String inputFilePath, int movieId, String resolution) {
        String outputDirectory = AppConstants.HLS_OUTPUT_PATH;
        String outputFileName = movieId + "_" + resolution;
        String outputFilePath = outputDirectory + "/" + outputFileName + ".m3u8";
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

