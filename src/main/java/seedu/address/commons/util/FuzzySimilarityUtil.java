package seedu.address.commons.util;

import java.util.List;

/**
 * Utility class that combines multiple {@link SimilarityMetric} implementations
 * to compute a fuzzy similarity score between two strings.
 * <p>
 * The class iterates over the provided similarity metrics and returns the highest
 * similarity score as the final result. It can also check if two strings are
 * considered similar based on a specified threshold.
 */
public class FuzzySimilarityUtil {

    private final List<SimilarityMetric> metrics;

    /**
     * Constructs a new {@code FuzzySimilarityUtil} with the given list of similarity metrics.
     *
     * @param metrics The list of {@link SimilarityMetric} instances to use
     * @throws IllegalAccessException if {@code metrics} is null or empty
     */
    public FuzzySimilarityUtil(List<SimilarityMetric> metrics) {
        if (metrics == null || metrics.isEmpty()) {
            throw new IllegalArgumentException("Metrics list must not be null or empty");
        }
        this.metrics = metrics;
    }

    /**
     * Computes the fuzzy similarity score between two strings
     * using all metrics provided.
     * <p>
     * The score returned is the maximum similarity value across all metrics.
     * @param first   The first string to compare
     * @param second  The second string to compare
     * @return the highest similarity score among all metrics,
     *          in the range of [0.0, 1.0]
     * @throws NullPointerException if {@code first} or {@code second} is null
     */
    public double computeSimilarity(String first, String second) {
        if (first == null || second == null) {
            throw new NullPointerException("Input strings must not be null");
        }
        double score = 0.0;

        for (SimilarityMetric metric : metrics) {
            score += metric.similarity(first, second);

        }

        return score / metrics.size();
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
        return computeSimilarity(first, second) >= threshold;
    }
}
