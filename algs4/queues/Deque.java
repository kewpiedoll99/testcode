import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * User: barclayadunn
 * Date: 9/28/14
 * Time: 4:05 PM
 */
public class Deque<Item> implements Iterable<Item> {

    Item [] queue;
    int count;

    // construct an empty deque
    public Deque() {
        count = 0;
        Object [] newQueue = new Object[count];
        queue = (Item []) newQueue;
    }                 

    // is the deque empty?
    public boolean isEmpty() {
        return (count == 0);
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    // insert the item at the front
    public void addFirst(Item item) {
        if (item == null)
            throw new NullPointerException("You may not add a null Item to the deque.");
        if (queue.length == count)
            resize(count * 2);
        Item holder1, holder2;
        holder1 = queue[0];
        // insert at front and move everything down 1
        for (int i = 0; i < queue.length; i++) {
            if (queue[i] == null) {
                count = i;      // set new value of count
                break;
            }
            holder2 = queue[i]; // save curr val of present cell
            queue[i] = holder1; // change present cell to prev cell value
            holder1 = holder2;  // change prev cell holder value to this cell's prev value
        }
    }

    // insert the item at the end
    public void addLast(Item item) {
        if (item == null)
            throw new NullPointerException("You may not add a null Item to the deque.");

        if (queue.length == count)
            resize(count * 2);
        queue[count++] = item;
    }

    // delete and return the item at the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("The queue is empty: nothing to remove.");

        Item holder = queue[0];
        for (int i = 0; i < queue.length; i++) {
            count = i; // set new value of count
            queue[i] = queue[i+1];
            if (queue[i+1] == null)
                break;
        }

        if (count <= queue.length/4)
            resize(queue.length/2);

        return holder;
    }

    // delete and return the item at the end
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("The queue is empty: nothing to remove.");
        Item holder = queue[count];
        queue[count--] = null;     // set prev last value to null; then count is decremented

        if (count <= queue.length/4)
            resize(queue.length/2);

        return holder;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    // unit testing
    public static void main(String[] args) {
    }

    private void resize(int capacity) {
        Object [] copy = new Object[capacity];
        for (int i = 0; i < queue.length; i++)
            copy[i] = queue[1];
        queue = (Item []) copy;
    }

    class ArrayIterator<Item> implements Iterator<Item> {
        private int i = 0;

        public boolean hasNext() {
            return i < count;
        }

        public Item next() {
            if (queue[i] == null)
                throw new NoSuchElementException("No more Items in iterator.");

            return (Item) queue[i++];
        }

        public void remove() {
            // not implemented
            throw new UnsupportedOperationException("method remove() not implemented.");
        }
    }
}
