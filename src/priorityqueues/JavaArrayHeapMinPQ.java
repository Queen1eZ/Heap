package priorityqueues;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * This implementation simply piggybacks off of Java's built-in array-based heap.
 * Helpful for testing.
 */
public class JavaArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    PriorityQueue<PriorityNode<T>> pq;
    Map<T, PriorityNode<T>> itemToNode;

    static class PriorityNodeComparable<T> extends PriorityNode<T> implements Comparable<PriorityNode<T>> {
        PriorityNodeComparable(T e, double p) {
            super(e, p);
        }

        PriorityNodeComparable(PriorityNode<T> node) {
            super(node.getItem(), node.getPriority());
        }

        @Override
        public int compareTo(PriorityNode<T> o) {
            return Double.compare(this.getPriority(), o.getPriority());
        }
    }

    public JavaArrayHeapMinPQ() {
        pq = new PriorityQueue<>();
        itemToNode = new HashMap<>();
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Item already exists");
        }
        PriorityNode<T> node = new PriorityNodeComparable<>(item, priority);
        pq.add(node);
        itemToNode.put(item, node);
    }

    @Override
    public boolean contains(T item) {
        return itemToNode.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        return pq.peek().getItem();
    }

    @Override
    public T removeMin() {
        T ret = peekMin();
        pq.remove();
        itemToNode.remove(ret);
        return ret;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("PQ does not contain " + item);
        }
        PriorityNode<T> node = itemToNode.get(item);
        pq.remove(node);
        node.setPriority(priority);
        pq.add(node);
        itemToNode.put(item, node);
    }

    @Override
    public int size() {
        return pq.size();
    }
}
