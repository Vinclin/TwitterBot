package org.cis1200;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/** Tests for TweetParser */
class TweetParserTest {

    // A helper function to create a singleton list from a word
    private static List<String> singleton() {
        List<String> l = new LinkedList<>();
        l.add("abc");
        return l;
    }

    // A helper function for creating lists of strings
    private static List<String> listOfArray(String[] words) {
        List<String> l = new LinkedList<>(Arrays.asList(words));
        return l;
    }

    // Cleaning and filtering tests -------------------------------------------
    @Test
    void removeURLsTest() {
        Assertions.assertEquals(
                "abc . def.", TweetParser.removeURLs("abc https://www.cis.upenn.edu. def."), ""
        );
        Assertions.assertEquals("abc", TweetParser.removeURLs("abc"), "");
        Assertions
                .assertEquals("abc ", TweetParser.removeURLs("abc https://www.cis.upenn.edu"), "");
        Assertions.assertEquals(
                "abc .", TweetParser.removeURLs("abc https://www.cis.upenn.edu."), ""
        );
        Assertions.assertEquals(" abc ", TweetParser.removeURLs("https:// abc http:ala34?#?"), "");
        Assertions.assertEquals(
                " abc  def", TweetParser.removeURLs("https:// abc http:ala34?#? def"), ""
        );
        Assertions.assertEquals(
                " abc  def", TweetParser.removeURLs("https:// abc https``\":ala34?#? def"), ""
        );
        Assertions.assertEquals("abchttp", TweetParser.removeURLs("abchttp"), "");
    }

    @Test
    void testCleanWord() {
        Assertions.assertEquals("abc", TweetParser.cleanWord("abc"), "");
        Assertions.assertEquals("abc", TweetParser.cleanWord("ABC"), "");
        Assertions.assertNull(TweetParser.cleanWord("@abc"), "");
        Assertions.assertEquals("ab'c", TweetParser.cleanWord("ab'c"), "");
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */

    /* **** ****** ***** **** EXTRACT COLUMN TESTS **** **** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    void testExtractColumnGetsCorrectColumn() {
        Assertions.assertEquals(
                " This is a tweet.",
                TweetParser.extractColumn(
                        "wrongColumn, wrong column, wrong column!, This is a tweet.", 3
                ),
                ""
        );
    }

    @Test
    void testExtractColumnOutOfBounds() {
        Assertions.assertEquals(
                null,
                TweetParser.extractColumn(
                        "wrongColumn, wrong column, wrong column!, This is a tweet.", 10
                ),
                ""
        );
    }

    @Test
    void testExtractColumnNull() {
        Assertions.assertNull((Object) null, "");
    }

    /* **** ****** ***** ***** CSV DATA TO TWEETS ***** **** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    void testCsvDataToTweetsSimpleCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 1);
        List<String> expected = new LinkedList<>();
        expected.add(" The end should come here.");
        expected.add(" This comes from data with no duplicate words!");
        Assertions.assertEquals(expected, tweets, "");
    }

    @Test
    void testCsvDataToTweetsNoIndex() {
        StringReader sr = new StringReader(
                "0\n" +
                        "1, The end should come here.\n" +
                        "2, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 1);
        List<String> expected = new LinkedList<>();
        expected.add(" The end should come here.");
        expected.add(" This comes from data with no duplicate words!");
        Assertions.assertEquals(expected, tweets, "");
    }

    /* **** ****** ***** ** PARSE AND CLEAN SENTENCE ** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    void parseAndCleanSentenceNonEmptyFiltered() {
        List<String> sentence = TweetParser.parseAndCleanSentence("abc #@#F");
        List<String> expected = new LinkedList<>();
        expected.add("abc");
        Assertions.assertEquals(expected, sentence, "");
    }

    @Test
    void parseAndCleanSentenceBadRegexWords() {
        List<String> sentence = TweetParser.parseAndCleanSentence("... #@#F  abc");
        List<String> expected = new ArrayList<>();
        expected.add("abc");
        Assertions.assertEquals(expected, sentence, "");
    }

    @Test
    void parseAndCleanSentenceBadSpaces() {
        List<String> sentence = TweetParser.parseAndCleanSentence("   ABC     }}}");
        List<String> expected = new ArrayList<>();
        expected.add("abc");
        Assertions.assertEquals(expected, sentence, "");
    }

    /* **** ****** ***** **** PARSE AND CLEAN TWEET *** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    void testParseAndCleanTweetRemovesURLS1() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("abc https://www.cis.upenn.edu");
        List<List<String>> expected = new LinkedList<>();
        expected.add(singleton());
        Assertions.assertEquals(expected, sentences, "");
    }

    @Test
    void testParseAndCleanTweetRemovesURLs() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("https://www.cis.upenn.edu abc");
        List<List<String>> expected = new ArrayList<>();
        expected.add(singleton());
        Assertions.assertEquals(expected, sentences, "");
    }

    /* **** ****** ***** ** CSV DATA TO TRAINING DATA ** ***** ****** **** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    void testCsvDataToTrainingDataSimpleCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<>();
        expected.add(listOfArray("the end should come here".split(" ")));
        expected.add(listOfArray("this comes from data with no duplicate words".split(" ")));
        Assertions.assertEquals(expected, tweets, "");
    }

    @Test
    void testCsvDataToTrainingDataBadRegexWordsCSV() {
        StringReader sr = new StringReader(
                "0, The end should}}} come here.\n" +
                        "1, This comes}}} from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<>();
        expected.add(listOfArray("the end come here".split(" ")));
        expected.add(listOfArray("this from data with no duplicate words".split(" ")));
        Assertions.assertEquals(expected, tweets, "");
    }

    @Test
    void testCsvDataToTrainingDataSpaces() {
        StringReader sr = new StringReader(
                "0,   The end should   come here.\n" +
                        "1,   This comes from   data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<>();
        expected.add(listOfArray("the end should come here".split(" ")));
        expected.add(listOfArray("this comes from data with no duplicate words".split(" ")));
        Assertions.assertEquals(expected, tweets, "");
    }

    @Test
    void testCsvDataToTrainingDataCSVFile() throws FileNotFoundException {
        FileReader fr = new FileReader("./files/simple_test_data.csv");
        BufferedReader br = new BufferedReader(fr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<>();
        expected.add(listOfArray("the end should come here".split(" ")));
        expected.add(listOfArray("this comes from data with no duplicate words".split(" ")));
        Assertions.assertEquals(expected, tweets, "");
    }
}
