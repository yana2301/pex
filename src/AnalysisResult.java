import java.util.List;
import java.util.stream.Collectors;

public class AnalysisResult {
    private static final String CSV_SEPARATOR = ",";
    private final String url;
    private final List<Integer> colors;

    public AnalysisResult(String url, List<Integer> colors) {
        this.url = url;
        this.colors = colors;
    }

    public String toString() {
        StringBuilder result = new StringBuilder(url);
        result.append(CSV_SEPARATOR);
        String colorsStr = colors.stream().map(AnalysisResult::rgbToString)
                .collect(Collectors.joining(CSV_SEPARATOR));
        result.append(colorsStr);
        return result.toString();
    }

    private static String rgbToString(Integer rgbInt) {
        return "#" + Integer.toHexString(rgbInt & 0xffffff).toUpperCase();
    }
}
