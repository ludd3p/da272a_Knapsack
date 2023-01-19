package DataModels;

import java.util.HashMap;

/**
 * K = Weight of given item, V = Item
 * Knapsack as HashMap for O(1) lookup by weight 
 */
public class HashSack {

    public HashMap<Integer, Item> getHashMap() {
        return knapSackHashMap;
    }

    // key = volume/weight of item, value = item
    private final HashMap<Integer, Item> knapSackHashMap;

    // represents space left in knapsack
    private int capacity;

    /**
     * @param capacity init to whatever weight u want bag to hold
     */
    public HashSack(int capacity) {
        this.capacity = capacity;
        this.knapSackHashMap = new HashMap<>();
    }

    public boolean checkItemFIt(Item item) {
        return (capacity - item.getVolume()) >= 0;
    }
    public void removeItem(Item item){
        int key = item.getVolume();
        getHashMap().remove(key);
        capacity += key;
    }
    /**
     * @return
     */
    public boolean addItem(Item item) {
        if (checkItemFIt(item)) {
            // binary decision variable
            item.setChosen(true);
            // put to map
            knapSackHashMap.put(item.getVolume(), item);
            // subtract volume/weight  of item put in hashsack from capacity
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

    public HashMap<Integer, Item> getResult() {
        return this.knapSackHashMap;
    }

    @Override
    public String toString() {
        return "HashSack{" +
                "knapSackHashMap=" + knapSackHashMap +
                ", capacity=" + capacity +
                '}';
    }
}
