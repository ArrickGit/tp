package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class FuzzySimilarUtilTest {

    private static class DummyMetric implements SimilarityMetric {
        private final double score;

        public DummyMetric(double score) {
            this.score = score;
        }

        @Override
        public double similarity(String first, String second) {
            return score;
        }
    }

    @Test
    public void computeSimilarity_returnsMaxScore() {
        FuzzySimilarityUtil util = new FuzzySimilarityUtil(List.of(
                new DummyMetric(0.2),
                new DummyMetric(0.7),
                new DummyMetric(0.5)

        ));

        double expectedMean = (0.2 + 0.7 + 0.5) / 3;
        assertEquals(expectedMean, util.computeSimilarity("a", "b"));
    }

    @Test
    public void isSimilar_returnsFalseWhenBelowThreshold() {
        FuzzySimilarityUtil util = new FuzzySimilarityUtil(List.of(
                new DummyMetric(0.4)
        ));
        assertFalse(util.isSimilar("a", "b", 0.5));
    }

    @Test
    public void isSimilar_returnsTrueWhenAboveThreshold() {
        FuzzySimilarityUtil util = new FuzzySimilarityUtil(List.of(
                new DummyMetric(0.8)
        ));
        assertTrue(util.isSimilar("a", "b", 0.5));
    }

    @Test
    public void threshold_edgeCase_zero() {
        FuzzySimilarityUtil util = new FuzzySimilarityUtil(List.of(
                new DummyMetric(0.0)
        ));
        assertTrue(util.isSimilar("a", "b", 0.0));
    }

    @Test
    public void threshold_edgeCase_one() {
        FuzzySimilarityUtil util = new FuzzySimilarityUtil(List.of(
                new DummyMetric(0.8),
                new DummyMetric(1.0)
        ));
        assertFalse(util.isSimilar("a", "b", 1.0));
    }

}
