package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class LevenshteinDistanceUtilTest {

    private final LevenshteinDistanceUtil levenshteinDistanceUtil =
            new LevenshteinDistanceUtil();

    @Test
    public void similarity_sameString_distanceZero() {
        assertEquals(1.0, levenshteinDistanceUtil.similarity("hello", "hello"));
    }

    @Test
    public void similarity_singleCharacterSame() {
        assertEquals(1.0, levenshteinDistanceUtil.similarity("a", "a"));
    }

    @Test
    public void similarity_singleCharacterDifferent() {
        assertEquals(0.0, levenshteinDistanceUtil.similarity("a", "b"));
    }

    @Test
    public void similarity_oneCharacterDifference() {
        // distance = 1, maxLength = 4
        assertEquals(0.8, levenshteinDistanceUtil.similarity("apple", "aprle"));
    }

    @Test
    public void similarity_insertionCase() {
        assertEquals(0.75, levenshteinDistanceUtil.similarity("cat", "cats"));
    }

    @Test
    public void similarity_deletionCase() {
        assertEquals(0.75, levenshteinDistanceUtil.similarity("cats", "cat"));
    }

    @Test
    public void similarity_substitutionCase() {
        assertEquals(1 - (1.0 / 3),
                levenshteinDistanceUtil.similarity("cat", "bat"));
    }

    @Test
    public void similarity_multipleEdits() {
        assertEquals(1 - (3.0 / 7),
                levenshteinDistanceUtil.similarity("kitten", "sitting"));
    }

    @Test
    public void similarity_completelyDifferentString() {
        assertEquals(0.0,
                levenshteinDistanceUtil.similarity("alice", "james"));
    }

    @Test
    public void similarity_caseInsensitive() {
        assertEquals(1.0,
                levenshteinDistanceUtil.similarity("Hello", "hello"));
    }

    @Test
    public void similarity_repeatedCharacters() {
        assertEquals(0.8,
                levenshteinDistanceUtil.similarity("aaaaa", "aabaa"));
    }

    @Test
    public void similarity_bothEmptyString() {
        assertEquals(1.0,
                levenshteinDistanceUtil.similarity("", ""));
    }

    @Test
    public void similarity_firstStringEmpty() {
        assertEquals(0.0,
                levenshteinDistanceUtil.similarity("cat", ""));
    }

    @Test
    public void similarity_secondStringEmpty() {
        assertEquals(0.0,
                levenshteinDistanceUtil.similarity("", "cat"));
    }

    @Test
    public void similarity_bothNull() {
        assertThrows(AssertionError.class, () ->
                levenshteinDistanceUtil.similarity(null, null));
    }

    @Test
    public void similarity_firstNull() {
        assertThrows(AssertionError.class, () ->
                levenshteinDistanceUtil.similarity(null, "cat"));
    }

    @Test
    public void similarity_secondNull() {
        assertThrows(AssertionError.class, () ->
            levenshteinDistanceUtil.similarity("cat", null));
    }

    @Test
    public void similarity_longStrings() {
        StringBuilder string1 = new StringBuilder();
        StringBuilder string2 = new StringBuilder();

        for (int i = 0; i < 1000; i++) {
            string1.append("a");
            string2.append("a");
        }

        string2.setCharAt(500, 'b');
        assertEquals(0.999,
                levenshteinDistanceUtil.similarity(string1.toString(),
                        string2.toString()));
    }

    @Test
    public void similarity_longInsertion() {
        String string1 = "a".repeat(1000);
        String string2 = "a".repeat(1000) + "b";

        assertEquals(1
                        - (1.0 / Math.max(string1.length(), string2.length())),
                levenshteinDistanceUtil.similarity(string1, string2));
    }

    @Test
    public void similarity_specialCharacter() {
        assertEquals(1 - (1.0 / 6),
                levenshteinDistanceUtil.similarity("hello!", "hello"));
    }

    @Test
    public void similarity_specialCharacterDifferent() {
        assertEquals(1 - (1.0 / 6),
                levenshteinDistanceUtil.similarity("hello!", "hello."));
    }

    @Test
    public void similarity_stringsWithSpaces() {
        assertEquals(1 - (1.0 / 11),
                levenshteinDistanceUtil.similarity("hello world", "helloworld"));
    }

    @Test
    public void similarity_stringsWithMultipleSpaces() {
        assertEquals(1 - (2.0 / 12),
                levenshteinDistanceUtil.similarity("hello  world", "helloworld"));
    }

    @Test
    public void similarity_spaceVsCharacters() {
        assertEquals(1 - (1.0 / 11),
                levenshteinDistanceUtil.similarity("hello world", "hello-world"));
    }

    @Test
    public void similarity_transpositionCountsAsTwo() {
        assertEquals(0.0, levenshteinDistanceUtil.similarity("ab", "ba"));
    }

    @Test
    public void similarity_distanceSymmetric() {
        double d1 = levenshteinDistanceUtil.similarity("kitten", "sitting");
        double d2 = levenshteinDistanceUtil.similarity("sitting", "kitten");
        assertEquals(d1, d2);
    }
}
