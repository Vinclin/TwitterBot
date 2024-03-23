package org.cis1200;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.StringReader;
import java.io.BufferedReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** Tests for FileLineIterator */
class FileLineIteratorTest {

    /*
     * Here's a test to help you out, but you still need to write your own.
     */

    @Test
    void testHasNextAndNext() {

        // Note we don't need to create a new file here in order to test out our
        // FileLineIterator if we do not want to. We can just create a
        // StringReader to make testing easy!
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        Assertions.assertTrue(li.hasNext(), "");
        Assertions.assertEquals("0, The end should come here.", li.next(), "");
        Assertions.assertTrue(li.hasNext(), "");
        Assertions.assertEquals("1, This comes from data with no duplicate words!", li.next(), "");
        Assertions.assertFalse(li.hasNext(), "");
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */
    @Test
    void testReaderNull() {
        try {
            BufferedReader br = null;
            new FileLineIterator(br);
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("");
    }

    @Test
    void testFileNull() {
        try {
            BufferedReader br = FileLineIterator.fileToReader(null);
            new FileLineIterator(br);
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("");
    }

    @Test
    void testFileDoesNotExist() {
        try {
            BufferedReader br = FileLineIterator.fileToReader("abc");
            new FileLineIterator(br);
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("");
    }

    @Test
    void testReadFileEmpty() {
        Iterator<String> li = new FileLineIterator("files/empty.csv");
        Assertions.assertFalse(li.hasNext(), "");
    }

    @Test
    void testFile() {
        FileLineIterator li = new FileLineIterator("files/dog_feelings_tweets.csv");
        Assertions.assertTrue(li.hasNext(), "");
        li.next();
        Assertions.assertTrue(li.hasNext(), "");
        Assertions.assertEquals(
                "dog_feelings,2023-03-22 17:51:15,@dog_rates love. and loss", li.next(), ""
        );
        Assertions.assertTrue(li.hasNext(), "");
        Assertions.assertEquals(
                "dog_feelings,2023-03-07 22:33:49,RT @deberlin88:" +
                        " Probably my all-time favorite Tweet. @dog_feelings https://t.co/tFHhtRKrYI",
                li.next(),
                ""
        );
    }

    @Test
    void testHasNextAndNextError() {
        try {
            String words = "0, The end should come here.\n"
                    + "1, This comes from data with no duplicate words!";
            StringReader sr = new StringReader(words);
            BufferedReader br = new BufferedReader(sr);
            Iterator<String> li = new FileLineIterator(br);
            Assertions.assertTrue(li.hasNext(), "");
            Assertions.assertEquals("0, The end should come here.", li.next(), "");
            Assertions.assertTrue(li.hasNext(), "");
            Assertions.assertEquals(
                    "1, This comes from data with no duplicate words!", li.next(), ""
            );
            Assertions.assertFalse(li.hasNext(), "");
            Assertions.assertEquals("abc", li.next(), "");
        } catch (NoSuchElementException e) {
            return;
        }
        Assertions.fail("");
    }
}
