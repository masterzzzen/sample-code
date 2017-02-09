import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * The purpose of this queue is to store the N most recently
 * inserted elements. The queue should have the following properties:
 * 		The queue implements the interface java.util.Queue<E>
 * 		The queue is bounded in size. The total capacity of the queue must be passed into the constructor.
 * 		New elements are added to the tail of the queue
 * 		The queue is traversed from head to tail
 * 		The queue must always accept new elements. If the queue is already full (Queue#size() == capacity), the oldest element that was
 * 		    inserted (the head) should be evicted, and then the new element can be added at the tail.
 *
 * Queue<Integer> queue = new MostRecentlyInsertedQueue<Integer>(3);
 * queue.size(): 0, contents (head -> tail): [ ]
 * queue.offer(1); // queue.size(): 1, contents (head -> tail): [ 1 ]
 * queue.offer(2); // queue.size(): 2, contents (head -> tail): [ 1, 2 ]
 * queue.offer(3); // queue.size(): 3, contents (head -> tail): [ 1, 2, 3 ]
 * queue.offer(4); // queue.size(): 3, contents (head -> tail): [ 2, 3, 4 ]
 * queue.offer(5); // queue.size(): 3, contents (head -> tail): [ 3, 4, 5 ]
 * int poll1 = queue.poll(); // queue.size(): 2, contents (head -> tail): [ 4, 5 ], poll1 = 3
 * int poll2 = queue.poll(); // queue.size(): 1, contents (head -> tail): [ 5 ], poll2 = 4
 * queue.clear(); // queue.size(): 0, contents (head -> tail): [ ]
 * 
 * 
 * Some implementation notes:
 * Because maximum size is fixed, I decided to use a Java array to represent the storage internal storage,
 * while tracking its effective size (int size), as well as its head and tail indices, separately.
 *
 * Internal storage is implemented with a linked list because
 * 1. We do not need to remove or insert from the middle of the storage
 * 2. Plain arrays enjoy spatial locality that linked lists don't
 *
 * poll(), offer(), peek(), size(), and clear() should all be O(1)
 *
 */

public class MostRecentlyInsertedQueue<E> extends AbstractQueue<E> {

    private int initialSize;
    private E[] storage;

    private int head; // the position of the queue's head in terms of internal storage
    private int tail; // tail is the index of the queue's last element. If the storage is (5, 3), then tail is at position 1.
    private int size;

    public MostRecentlyInsertedQueue(int initialSize) {

        if (initialSize <= 0)
            throw new IllegalArgumentException("Initial size cannot be 0 or negative");

        this.initialSize = initialSize;
        size = 0;
        storage = (E[]) new Object[initialSize];
        head = 0;
        tail = -1;
    }

    @Override
    public Iterator<E> iterator() {
        return new QueueIterator<>();
    }

    private class QueueIterator<E> implements Iterator<E> {
        private int cursor = head;
        private int numIterated = 0;

        @Override
        public boolean hasNext() {
            return numIterated < size;
        }

        @Override
        public E next() {
            while (hasNext()) {
                E el = (E) storage[cursor];
                cursor = ++cursor % initialSize;
                numIterated++;
                return el;
            }
            return null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean offer(E e) {

        tail = ++tail % initialSize;
        storage[tail] = e;
        if (size < initialSize) {
            size++;
        }

        // If storage is now full after the insertion, shift right by 1
        if (size == initialSize && head == tail) {
            head = ++head % initialSize;
        }

        return true;
    }

    @Override
    public E poll() {

        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }

        E el = storage[head];
        storage[head] = null;
        head = ++head % initialSize;
        size--;
        return el;
    }

    @Override
    public E peek() {

        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }

        return storage[head];
    }

    public void clear() {
        storage = (E[]) new Object[initialSize];
        size = 0;
        head = 0;
        tail = -1;
    }

    @Override
    public String toString() {
        if (storage == null || size == 0) {
            return "[]";
        }

        String ret = "[";
        Iterator<E> iter = this.iterator();
        while (iter.hasNext()) {
            ret += iter.next() + ", ";
        }
        ret = ret.substring(0, ret.length() - 2) + "]";
        return ret;
    }

    /*
        For a MostRecentlyInsertedQueue of size 2 and initialSize 3, whose storage contents are [0, 1] and
        whose's head variable points at index 2 of its internal storage array, this method will output [1, null, 0]
     */
    private String internalStorage() {
        String ret = "[";
        for (int i = 0; i < storage.length; i++) {
            ret += storage[i] + ", ";
        }
        ret = ret.substring(0, ret.length() - 2) + "]";
        return ret;
    }
}
