package seedu.address.commons.util;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;

/**
 * Utility class that combines multiple {@link SimilarityMetric} implementations
 * to compute a fuzzy similarity score between two strings.
 * <p>
 * The class iterates over the provided similarity metrics and returns the highest
 * similarity score as the final result. It can also check if two strings are
 * considered similar based on a specified threshold.
 */
public class FuzzySimilarityUtil {

    private static final double EPSILON = 1e-06;

    private final List<SimilarityMetric> metrics;
    private final Logger logger =
            LogsCenter.getLogger(FuzzySimilarityUtil.class);


    /**
     * Constructs a new {@code FuzzySimilarityUtil} with the given list of similarity metrics.
     *
     * @param metrics The list of {@link SimilarityMetric} instances to use
     * @throws IllegalArgumentException if {@code metrics} is null or empty
     * @throws NullPointerException if any element in {@code metrics} is null
     */
    public FuzzySimilarityUtil(List<SimilarityMetric> metrics) {
        if (metrics == null || metrics.isEmpty()) {
            throw new IllegalArgumentException("Metrics list must not be null or empty");
        }
        this.metrics = List.copyOf(metrics);

        logger.info("FuzzySimilarityUtil initialized with " + this.metrics.size() + " metrics.");
    }

    /**
     * Computes the fuzzy similarity score between two strings
     * using all metrics provided.
     * <p>
     * The score returned is the maximum similarity value across all metrics.
     * @param first   The first string to compare
     * @param second  The second string to compare
     * @return the maximum similarity score among all metrics,
     *          in the range of [0.0, 1.0]
     * @throws NullPointerException if {@code first} or {@code second} is null
     */
    public double computeSimilarity(String first, String second) {
        if (first == null || second == null) {
            throw new NullPointerException("Input strings must not be null");
        }

        double maxScore = 0.0;

        for (SimilarityMetric metric : metrics) {
            try {
                double current = metric.similarity(first, second);
                if (current > maxScore) {
                    maxScore = current;
                }
            } catch (IllegalArgumentException | ArithmeticException e) {
                logger.log(Level.WARNING,
                        String.format("Metric %s failed for '%s' vs '%s'", metric, first, second), e);
            }
        }

        return maxScore;
    }

    /**
     * Determines if two strings are similar based on a specified threshold.
     * <p>
     * This method internally calls {@link #computeSimilarity(String, String)} and compares the result with the
     * provided threshold.
     *
     * @param first      The first string to compare
     * @param second     The second string to compare
     * @param threshold  The minimum similarity required for the strings to be considered similar
     * @return {@code true} if the similarity score is greater than or equals to {@code threshold},
     *          {@code false} otherwise
     * @throws IllegalArgumentException if {@code threshold} is not in range [0.0, 1.0]
     */
    public boolean isSimilar(String first, String second,
                             double threshold)
            throws IllegalArgumentException {

        if (threshold < 0.0 || threshold > 1.0) {
            throw new IllegalArgumentException("Threshold must be between 0.0 and 1.0");
        }
        return computeSimilarity(first, second) + EPSILON >= threshold;
    }
}
