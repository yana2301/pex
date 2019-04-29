import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ImageColorsAnalyzer {

    public CompletableFuture<AnalysisResult> analyze(String imageUrl) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Integer, Integer> imageColors = getImageColorMap(imageUrl);
            List<Integer> topColors = getTop3Colors(imageColors.entrySet());
            return new AnalysisResult(imageUrl, topColors);
        }).exceptionally(e -> null);
    }

    private List<Integer> getTop3Colors(Set<Map.Entry<Integer, Integer>> imageColors) {
        return imageColors.stream()
                .sorted((o1, o2) -> -Integer.compare(o1.getValue(), o2.getValue()))
                .limit(3)
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private Map<Integer, Integer> getImageColorMap(String imageUrl) {
        try {
            BufferedImage image = ImageIO.read(new URL(imageUrl));
            Map<Integer, Integer> imageColors = new HashMap<>();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int color = image.getRGB(x, y);
                    imageColors.compute(color, (key, val) -> val == null ? 1 : val + 1);
                }
            }
            return imageColors;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
