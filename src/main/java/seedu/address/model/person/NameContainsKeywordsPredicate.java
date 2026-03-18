package seedu.address.model.person;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.CosineNGramUtil;
import seedu.address.commons.util.FuzzySimilarityUtil;
import seedu.address.commons.util.LevenshteinDistanceUtil;
import seedu.address.commons.util.SimilarityMetric;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;
    private final double threshold;
    private final FuzzySimilarityUtil fuzzyUtil;


    public NameContainsKeywordsPredicate(List<String> keywords) {
        this(keywords, 0.5);
    }

    /**
     * Constructs a predicate with a custom similarity threshold.
     * @param keywords  The list of keywords to match
     * @param threshold Minimum similarity of range [0.0, 1.0] to
     *                  be considered a match
     */
    public NameContainsKeywordsPredicate(List<String> keywords,
                                         double threshold) {
        this.keywords = keywords;
        List<SimilarityMetric> metrics = List.of(
                new LevenshteinDistanceUtil(),
                new CosineNGramUtil(3)
        );
        this.fuzzyUtil = new FuzzySimilarityUtil(metrics);
        this.threshold = threshold;
    }

    @Override
    public boolean test(Person person) {
        String[] name = person.getName().fullName.toLowerCase().split("\\s+");
        return keywords.stream()
                .map(String::toLowerCase)
                .anyMatch(keyword -> {
                    String normalizedKeyword = keyword.toLowerCase();
                    return Arrays.stream(name)
                            .anyMatch(word ->
                                    fuzzyUtil.isSimilar(word, keyword, threshold));
                });
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (NameContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords)
                && threshold
                == otherNameContainsKeywordsPredicate.threshold;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
