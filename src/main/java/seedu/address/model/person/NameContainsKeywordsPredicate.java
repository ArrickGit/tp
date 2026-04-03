package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    private final FuzzySimilarityUtil fuzzyUtil;

    /**
     * Constructs a predicate that matches persons whose names contain any of the
     * given keywords.
     *
     * @param keywords  The list of keywords to match
     */
    public NameContainsKeywordsPredicate(List<String> keywords) {
        requireNonNull(keywords);
        this.keywords = filterValidKeywords(keywords);
        List<SimilarityMetric> metrics = List.of(
                new LevenshteinDistanceUtil(),
                new CosineNGramUtil(2)
        );
        this.fuzzyUtil = new FuzzySimilarityUtil(metrics);
    }

    /**
     * Test whether the given {@code Person}'s name matches
     * any of the keywords.
     *
     * <ul>
     *     <li>Returns {@code false} if {@code person} is null </li>
     *     <li> Returns {@code false} if {@code person.getName()}
     *     or fullName is null</li>
     *     <li>Ignores empty keywords</li>
     * </ul>
     *
     * Matching strategy:
     * <ul>
     *     <li>Exact substring match</li>
     *     <li>Fuzzy similarity with dynamic thresholds</li>
     * </ul>
     *
     * @param person The {@code Person} to test
     * @return {@code true} if any keyword matches the
     *      person's name; {@code false} otherwise
     */
    @Override
    public boolean test(Person person) {
        assert keywords != null : "Keywords should never be null here";
        if (person == null
                || person.getName() == null
                || person.getName().fullName == null) {
            return false;
        }

        String fullName = person.getName().fullName.toLowerCase().trim();
        if (fullName.isEmpty()) {
            return false;
        }

        String[] words = fullName.split("\\s+");
        return keywords.stream()
                .anyMatch(keyword -> matchesAnyWord(words, keyword));
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
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    /**
     * Returns a similarity threshold for fuzzy matching based on the length of the
     * shorter compared word.
     * <p>
     * Short words are given a lower threshold to allow for
     * more leniency in fuzzy matches,
     * while longer words use a stricter threshold to reduce false positive.
     *
     * <ul>
     *     <li>Words of length 3 or less: threshold are set to be more lenient
     *     to 0.5</li>
     *     <li>Words of length between 4 and 5: threshold are set to be more lenient
     *     to 0.65</li>
     *     <li>Words of length long than 5: threshold are set to be stricter
     *     to 0.8</li>
     * </ul>
     *
     * @param minWordLength Length of the shorter of the two words being compared
     * @return the similarity threshold in the range [0.0, 1.0] for use in fuzzy search
     */
    private static double getThreshold(int minWordLength) {
        if (minWordLength <= 3) {
            return 0.5;
        } else if (minWordLength <= 5) {
            return 0.65;
        }
        return 0.8;
    }

    private boolean matchesAnyWord(String[] words, String keyword) {
        return Arrays.stream(words)
                .anyMatch(word -> matchesWord(word, keyword));
    }

    private boolean matchesWord(String word, String keyword) {
        double dynamicThreshold = getThreshold(Math.min(word.length(), keyword.length()));
        return word.contains(keyword) || fuzzyUtil.isSimilar(word, keyword, dynamicThreshold);
    }

    private static List<String> filterValidKeywords(List<String> keywords) {
        return keywords.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(keyword -> !keyword.isEmpty())
                .map(String::toLowerCase)
                .toList();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
