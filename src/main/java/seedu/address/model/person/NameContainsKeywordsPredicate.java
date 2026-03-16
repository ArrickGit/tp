package seedu.address.model.person;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.CosineNGramUtil;
import seedu.address.commons.util.LevenshteinDistanceUtil;
import seedu.address.commons.util.SimilarityMetric;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;
    private final SimilarityMetric levenshtein;
    private final SimilarityMetric nGram;
    private final double threshold;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this(keywords, 0.8);
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
        this.levenshtein = new LevenshteinDistanceUtil();
        this.nGram = new CosineNGramUtil(3);
        this.threshold = threshold;
    }

    @Override
    public boolean test(Person person) {
        String[] name = person.getName().fullName.toLowerCase().split("\\s+");
        return keywords.stream()
                .anyMatch(keyword -> {
                    String normalizedKeyword = keyword.toLowerCase();
                    return Arrays.stream(name)
                            .anyMatch(word -> levenshtein.similarity(word, normalizedKeyword) >= threshold
                                                    || nGram.similarity(word, normalizedKeyword) >= threshold);
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
