import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

    private static final String INPUT_FILE = "input.txt";
    private static final String RESULT_FILE = "result.csv";

    public static void main(String[] args) {
        ImageColorsAnalyzer analyzer = new ImageColorsAnalyzer();
        try (Stream<String> stream = Files.lines(Paths.get(INPUT_FILE));
             BufferedWriter topColors = new BufferedWriter(new FileWriter(RESULT_FILE))) {
            CompletableFuture[] analyzeTasks = stream.map(line ->
                    analyzer.analyze(line)
                            .thenAccept(result -> {
                                if (result != null) {
                                    writeResult(topColors, result);
                                }
                            })
            ).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(analyzeTasks).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeResult(BufferedWriter topColors, AnalysisResult result) {
        try {
            topColors.write(result.toString());
            topColors.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
