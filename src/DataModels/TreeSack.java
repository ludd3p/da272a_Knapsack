package DataModels;
import java.util.*;

/**
 * K = Weight of given item, V = List<Item>
 * Knapsack as HashMap for O(1) lookup by weight 
 */
public class TreeSack {

    // key = volume/weight of item, value = item
    private final TreeMap<Integer, List<Item>> knapSackTreeMap;

    public TreeMap<Integer, List<Item>> getTreeMap() {
        return knapSackTreeMap;
    }

    // represents space left in knapsack
    private int capacity;


    /**
     * @param capacity init to whatever weight u want bag to hold
     */
    public TreeSack(int capacity) {
        this.capacity = capacity;
        this.knapSackTreeMap = new TreeMap<>(Comparator.naturalOrder());
    }

    public boolean checkItemFit(Item item) {
        return (capacity - item.getVolume()) >= 0;
    }

    /**
     * @param item to remove from knapsack
     * get the list in which the item is stored and remove the item from the array.
     *             If the array is now empty, remove it from the treemap,
     *             otherwise put the array back in the treemap and update the capacity
     */
    public void removeItem(Item item){
        int key = item.getVolume();
        ArrayList<Item> arr = new ArrayList<>();
        arr = (ArrayList<Item>) knapSackTreeMap.get(key);
        arr.remove(item);
        getTreeMap().remove(key);

        // don't put empty list back in treemap
        if (arr.size() >= 1)getTreeMap().put(key, arr);
        capacity += key;
    }
    /**
     * @return true if item added, false if item did not fit
     */
    public boolean addItem(Item item) {
        // if item fits in knapsack then add it to list and subtract volume from capacity
        if (checkItemFit(item)) {
            // if an item I with a volume equal to that of item to bed added already exists in list,
            // fetch the list from the treemap and add the new item to said list.
            if (knapSackTreeMap.containsKey(item.getVolume())) {
                ArrayList<Item> arr = new ArrayList<>();
                arr = (ArrayList<Item>) knapSackTreeMap.get(item.getVolume());
                arr.add(item);
                knapSackTreeMap.put(item.getVolume(), arr);
            }
            // else we create a new a list and add the item to said list
             else {
                List<Item> list = new ArrayList<>(List.of(item));
                item.setChosen(true);
                knapSackTreeMap.put(item.getVolume(), list);
            }

            // subtract volume of item put in list from remaining capacity of knapsack
            setCapacity(capacity - item.getVolume());
            return true;
        } else {
            return false;
        }
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public TreeMap<Integer, List<Item>> getResult() {
        return this.knapSackTreeMap;
    }

    /**
     *
     * @return sum of total values for a knapsack.
     */
    public int getTotalValue() {
        int i = 0;
        // iterate over all the lists which contains all the items,
        for (List<Item> items : knapSackTreeMap.values()) {
            for (Item item : items) {
                i += item.getValue();
            }
        }
        return i;
    }

    @Override
    public String toString() {
        return "Knapsack{" +
                "TreeMap " + knapSackTreeMap +
                ", capacity=" + capacity +
                '}';
    }
}
