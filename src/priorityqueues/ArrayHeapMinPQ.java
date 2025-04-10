package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;
    private final Map<T, Integer> indexMap;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        items.add(null);
        indexMap = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
        indexMap.put(items.get(a).getItem(), a);
        indexMap.put(items.get(b).getItem(), b);
    }
    private int parent(int i) {
        return i / 2;
    }
    private int left(int i) {
        return 2 * i;
    }
    private int right(int i) {
        return 2 * i + 1;
    }

    private void swim(int i) {
        while (i > START_INDEX) {
            int parent = parent(i);
            if (items.get(i).getPriority() >= items.get(parent).getPriority()) {
                break;
            }
            swap(i, parent);
            i = parent;
        }
    }

    // Adds an item with the given priority value.
    @Override
    public void add(T item, double priority) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null.");
        }
        if (contains(item)) {
            throw new IllegalArgumentException("Item is already present in the Priority queue.");
        }

        PriorityNode<T> node = new PriorityNode<>(item, priority);
        items.add(node);
        indexMap.put(item, size());
        swim(size());
    }

    // Returns true if the PQ contains the given item; false otherwise.
    @Override
    public boolean contains(T item) {
        return indexMap.containsKey(item);
    }

    // Returns the item with least-valued priority.
    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty.");
        }
        return items.get(START_INDEX).getItem();
    }

    // Removes and returns the item with least-valued priority.
    @Override
    public T removeMin() {
        // Check whether the priority queue is empty
        if (isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty.");
        }
        // Get the value of the root node
        T minvalue = items.get(START_INDEX).getItem();
        // Get the index of the last element
        int lastIndex = size();
        // Switch the root node to the last node
        swap(START_INDEX, lastIndex);
        // Remove the last node
        items.remove(lastIndex);
        // Remove the smallest element from the index
        indexMap.remove(minvalue);
        // If the queue is not empty, adjust it
        if (!isEmpty()) {
            int currentNode = START_INDEX;
            // Whether the tag needs to be adjusted further
            boolean adjustment = true;
            // Cycle to adjust the heap
            while (adjustment) {
                int leftNode = 2 * currentNode;
                int rightNode = 2 * currentNode + 1;
                int minimum = currentNode;
                boolean leftEfficient= leftNode <= size();
                boolean rightEfficient = rightNode <= size();
                // Update the smallest node if the left child node exists and has a lower priority
                if (leftEfficient) {
                    boolean isLeftPriorityLower =
                        items.get(leftNode).getPriority()
                            < items.get(minimum).getPriority();
                    if (isLeftPriorityLower) {
                        minimum = leftNode;
                    }
                }
                // Update the smallest node if the right child node exists and has a lower priority
                if (rightEfficient) {
                    boolean isRightPriorityLower =
                        items.get(rightNode).getPriority()
                            < items.get(minimum).getPriority();
                    if (isRightPriorityLower) {
                        minimum = rightNode;
                    }
                }
                // If the smallest node is not the current node, swap them and continue adjusting
                if (minimum != currentNode) {
                    swap(currentNode, minimum);
                    currentNode = minimum;
                } else {
                    adjustment = false;
                }
            }
        }

        return minvalue;
    }

    // Changes the priority of the given item.
    @Override
    public void changePriority(T item, double priority) {
        // Check whether the element exists in the index
        if (!indexMap.containsKey(item)) {
            throw new NoSuchElementException("Item not found: " + item);
        }
        // Get the index of the element
        int index = indexMap.get(item);
        // Get the old priority of the element
        double previous = items.get(index).getPriority();
        // Set a new priority for the element
        items.get(index).setPriority(priority);
        // If the new priority is lower than the old priority, adjust the heap
        if (priority < previous) {
            while (index > START_INDEX) {
                // Get the parent node index
                int parentIndex = index / 2;
                // If the priority of the current node is >= the priority of the parent node, the adjustment stops
                if (items.get(index).getPriority() >= items.get(parentIndex).getPriority()) {
                    break;
                }
                // Switch the current node and parent node
                swap(index, parentIndex);
                // Update the current node as the parent node
                index = parentIndex;
            }
        }
        // If the new priority is higher than the old priority, adjust the heap
        else if (priority > previous) {
            boolean adjustment = true;
            // Cycle to adjust the heap
            while (adjustment) {
                // Compute the index of the left and right child nodes
                int leftNode = 2 * index;
                int rightNode = 2 * index + 1;
                // Assume that the current node is the smallest node
                int minimum = index;
                boolean leftEfficient= leftNode <= size();
                boolean rightEfficient = rightNode <= size();
                // Update the smallest node if the left child node exists and has a lower priority
                if (leftEfficient) {
                    boolean isLeftPriorityLower =
                        items.get(leftNode).getPriority()
                            < items.get(minimum).getPriority();
                    if (isLeftPriorityLower) {
                        minimum = leftNode;
                    }
                }
                // Update the smallest node if the right child node exists and has a lower priority
                if (rightEfficient) {
                    boolean isRightPriorityLower =
                        items.get(rightNode).getPriority()
                            < items.get(minimum).getPriority();
                    if (isRightPriorityLower) {
                        minimum = rightNode;
                    }
                }
                // If the smallest node is not the current node, swap them and continue adjusting
                if (minimum != index) {
                    swap(index, minimum);
                    index = minimum;
                } else {
                    adjustment = false;
                }
            }
        }
    }

    // Returns the number of items in the PQ.
    @Override
    public int size() {
        return items.size() - 1;
    }
}
