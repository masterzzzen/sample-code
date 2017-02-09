import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

/**
 *
 */
public class TestMostRecentlyInsertedQueue extends TestCase {

    MostRecentlyInsertedQueue<Integer> queue;

    @Before
    public void setUp(){
        queue = new MostRecentlyInsertedQueue<Integer>(3);
    }

    public void testOffer(){
        assertEquals("[]", queue.toString());
        queue.offer(1);
        assertEquals("[1]", queue.toString());
        queue.offer(2);
        queue.offer(3);
        assertEquals("[1, 2, 3]", queue.toString());
        queue.offer(4);
        queue.offer(5);
        assertEquals("[3, 4, 5]", queue.toString());
        queue.offer(4);
        queue.offer(5);
        queue.offer(4);
        queue.offer(5);
        assertEquals("[5, 4, 5]", queue.toString());

    }

    public void testPollEmpty(){
        boolean thrown = false;
        try {
            queue.poll();
        } catch (NoSuchElementException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    public void testOfferAndPoll(){
        queue.offer(1);
        assertEquals(1, (int) queue.poll());
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        assertEquals(2, (int) queue.poll());
        queue.offer(5);
        assertEquals(3, (int) queue.poll());
        assertEquals(2, queue.size());
    }

    public void testPeek(){
        queue.offer(1);
        assertEquals(1, (int) queue.peek());
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        assertEquals(2, (int) queue.peek());
        queue.offer(5);
        assertEquals(3, (int) queue.peek());
        assertEquals(3, (int) queue.peek());
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        assertEquals(2, (int) queue.peek());
        assertEquals(3, queue.size());
    }

    public void testClear(){
        queue.offer(2);
        queue.offer(3);
        queue.clear();
        assertEquals("[]", queue.toString());
    }



}
