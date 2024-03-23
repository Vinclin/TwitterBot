package org.cis1200;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

/** Tests for MarkovChain */
class MarkovChainTest {

    /*
     * Writing tests for Markov Chain can be a little tricky.
     * We provide a few tests below to help you out, but you still need
     * to write your own.
     */

    /* **** ****** **** **** ADD BIGRAMS TESTS **** **** ****** **** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    void testAddBigram() {
        MarkovChain mc = new MarkovChain();
        mc.addBigram("1", "2");
        Assertions.assertTrue(mc.chain.containsKey("1"), "");
        ProbabilityDistribution<String> pd = mc.chain.get("1");
        Assertions.assertTrue(pd.getRecords().containsKey("2"), "");
        Assertions.assertEquals(1, pd.count("2"), "");
    }

    @Test
    public void testAddBigramNull1() {
        try {
            MarkovChain mc = new MarkovChain();
            mc.addBigram(null, "2");
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("");
    }

    @Test
    public void testAddBigramNull2() {
        try {
            MarkovChain mc = new MarkovChain();
            mc.addBigram("1", null);
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("");
    }

    /* ***** ****** ***** ***** TRAIN TESTS ***** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    void testTrain() {
        MarkovChain mc = new MarkovChain();
        String sentence = "1 2 3";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        Assertions.assertEquals(3, mc.chain.size(), "");
        ProbabilityDistribution<String> pd1 = mc.chain.get("1");
        Assertions.assertTrue(pd1.getRecords().containsKey("2"), "");
        Assertions.assertEquals(1, pd1.count("2"), "");
        ProbabilityDistribution<String> pd2 = mc.chain.get("2");
        Assertions.assertTrue(pd2.getRecords().containsKey("3"), "");
        Assertions.assertEquals(1, pd2.count("3"), "");
        ProbabilityDistribution<String> pd3 = mc.chain.get("3");
        Assertions.assertTrue(pd3.getRecords().containsKey(MarkovChain.END_TOKEN), "");
        Assertions.assertEquals(1, pd3.count(MarkovChain.END_TOKEN), "");
    }

    @Test
    public void testTrainNull() {
        MarkovChain mc = new MarkovChain();
        try {
            mc.train(null);
            Assertions.assertEquals(3, mc.chain.size());
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("");
    }

    @Test
    public void testTrainEmpty() {
        MarkovChain mc = new MarkovChain();
        String sentence = "";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        Assertions.assertEquals("{}", mc.chain.toString(), "");
        Assertions.assertEquals(0, mc.startWords.getTotal(), "");
    }

    /* **** ****** ****** MARKOV CHAIN CLASS TESTS ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    void testWalk() {
        /*
         * Using the sentences "CIS 1200 rocks" and "CIS 1200 beats CIS 1600",
         * we're going to put some bigrams into the Markov Chain.
         *
         * While in the real world, we want the sentence we output to be random,
         * we don't want this in testing. For testing, we want to modify our
         * ProbabilityDistribution such that it will output a predictable chain
         * of words.
         *
         * Luckily, we've provided a `fixDistribution` method that will do this
         * for you! By calling `fixDistribution` with a list of words that you
         * expect to be output, the ProbabilityDistributions will be modified to
         * output your words in that order.
         *
         * See our below test for an example of how to use this.
         */

        String[] expectedWords = { "CIS", "1200", "beats", "CIS", "1200", "rocks" };
        MarkovChain mc = new MarkovChain();

        String sentence1 = "CIS 1200 rocks";
        String sentence2 = "CIS 1200 beats CIS 1600";
        mc.train(Arrays.stream(sentence1.split(" ")).iterator());
        mc.train(Arrays.stream(sentence2.split(" ")).iterator());

        mc.reset("CIS"); // we start with "CIS" since that's the word our desired walk starts with
        mc.fixDistribution(new ArrayList<>(Arrays.asList(expectedWords)));

        for (String expectedWord : expectedWords) {
            Assertions.assertTrue(mc.hasNext(), "");
            Assertions.assertEquals(expectedWord, mc.next(), "");
        }
    }
}
