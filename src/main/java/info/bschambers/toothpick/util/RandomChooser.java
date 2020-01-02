package info.bschambers.toothpick.util;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * <p>Randomly chooses items from a collection with optional item-weighting and
 * changeability setting.</p>
 *
 * <p>Changeability is the measure of how likely a new random choice is to be
 * made each time {@link get} is called. If changeability is less than
 * {@code 1.0} it means that there is a chance that the previous item may be
 * returned again, and a value of {@code 0} means that the same item will be
 * returned <em>every</em> time, with a random choice only being made on the
 * first {@code get}. Note that even with maximum changeability there is still a
 * chance of getting the same item again, and especially if some items have more
 * weight than others.</p>
 *
 * <p>Item weights work as a fraction of the total combined weight. Note
 * that an item with weight zero will never be picked, even if there are no
 * other items (an exception will result instead).</p>
 *
 * <pre>
 * {@code
 * EXAMPLE 1: two items with combined weight of 3
 *                 +--------------------|
 *                 | CHANCE             |
 * |------+--------+----------+---------|
 * | ITEM | WEIGHT | FRACTION | DECIMAL |
 * |------+--------+----------+---------|
 * | A    |      1 | 1/3      |  0.333. |
 * | B    |      2 | 2/3      |  0.666. |
 * |------+--------+----------+---------|
 *
 *
 * EXAMPLE 2: four items with combined weight of 10 (including zero-weight item)
 *                 +--------------------|
 *                 | CHANCE             |
 * |------+--------+----------+---------|
 * | ITEM | WEIGHT | FRACTION | DECIMAL |
 * |------+--------+----------+---------|
 * | A    |      1 | 1/10     |     0.1 |
 * | B    |      2 | 1/5      |     0.2 |
 * | C    |      7 | 7/10     |     0.7 |
 * | D    |      0 | 0        |     0.0 |
 * |------+--------+----------+---------|
 * }
 * </pre>
 */
public class RandomChooser<T> implements Iterable<T> {

    private Random rand = new Random();
    private List<ChooserItem> items = new ArrayList<>();
    private final double changeability;
    private T defaultItem = null;
    private T currentItem = null;
    private int combinedWeight = 0;

    /**
     * <p>Makes a new RandomChooser with changeability of {@code 1.0}.</p>
     *
     * @param defaultItem Item to return if there are no other items.
     */
    public RandomChooser(T defaultItem) {
        this(defaultItem, 1);
    }

    /**
     * @param defaultItem Item to return if there are no other items.
     * @param changeability How likely is it that a new item will be chosen each
     * time {@link get} is called. A value of {@code 1.0} means that a new item
     * will be chosen every time. Value of zero means that a new item will never
     * be chosen (except for the very first get).
     */
    public RandomChooser(T defaultItem, double changeability) {
        this.defaultItem = defaultItem;
        this.changeability = changeability;
    }

    @Override
    public String toString() {
        String text = "RandomChooser: changeability=" + changeability
            + "\n... num-items=" + items.size()
            + " combined-weight=" + getCombinedWeight();
        for (ChooserItem ci : items)
            text += "\n... ITEM: " + ci.item.getClass().getSimpleName()
                + " (weight=" + ci.weight + ")";
        return text;
    }

    /**
     * <p>Returns true, if there is no items, or if the combined weight of all
     * items is zero.</p>
     */
    public boolean isEmpty() {
        return getCombinedWeight() == 0;
    }

    /**
     * <p>Adds item with a weight of 1.</p>
     */
    public void add(T item) {
        add(1, item);
    }

    /**
     * <p>Adds item with the specified weight.</p>
     */
    public void add(int weight, T item) {
        add("", weight, item);
    }

    /**
     * <p>Adds item with the specified weight.</p>
     */
    public void add(String description, int weight, T item) {
        if (weight < 0)
            weight = 0;
        items.add(new ChooserItem(description, weight, item));
        combinedWeight += weight;
    }

    /**
     * <p>Returns an item from the collection - the <em>changeability</em> value
     * passed into the constructor determines the likelihood of whether a random
     * item will be picked, or whether the previous item will be returned
     * again.</p>
     *
     * @see getRandom
     */
    public T get() {
        if (isEmpty())
            return defaultItem;
        if (currentItem == null ||
            Math.random() < changeability) {
            // choose a random item
            chooseRandom();
        }
        return currentItem;
    }

    /**
     * <p>Returns an item from the collection, always making a new random choice
     * to set the currentItem and ignoring the <em>changeability</em>
     * value. Note that the same item may still be returned again, especially if
     * it is heavily weighted.</p>
     *
     * @see get
     */
    public T getRandom() {
        if (isEmpty())
            return defaultItem;
        chooseRandom();
        return currentItem;
    }

    /**
     * <p>Chooses a new currentTime at random, or does nothing, if there are no
     * items to choose from.</p>
     */
    private void chooseRandom() {
        if (isEmpty())
            return;
        int n = 1 + rand.nextInt(getCombinedWeight());
        for (ChooserItem ci : items) {
            if (n <= ci.weight) {
                currentItem = ci.item;
                return;
            }
            n -= ci.weight;
        }
    }

    public int getCombinedWeight() {
        return combinedWeight;
    }

    public class ChooserItem {

        private String description = "";
        public final T item;
        public int weight;

        public ChooserItem(String description, int weight, T item) {
            this.description = description;
            this.weight = weight;
            this.item = item;
        }

        public String getDescription() {
            if (description.isEmpty())
                return item.getClass().getSimpleName();
            return description;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ItemIterator();
    }

    private class ItemIterator implements Iterator<T> {
        private int i = 0;
        @Override
        public boolean hasNext() {
            return i < items.size();
        }
        @Override
        public T next() {
            int n = i;
            i++;
            return items.get(n).item;
        }
    }

    public List<ChooserItem> chooserItemList() {
        return items;
    }

}
