package DataModels;

import java.util.*;

/**
 * K = Weight of given item, V = Item
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

    public void removeItem(Item item){
        int key = item.getVolume();
        getTreeMap().remove(key);
        capacity += key;
    }
    /**
     * @return
     */
    public boolean addItem(Item item) {
        if (checkItemFit(item)) {
            if (knapSackTreeMap.containsKey(item.getVolume())) knapSackTreeMap.get(item.getVolume()).add(item);
             else {
                List<Item> list = new ArrayList<>(List.of(item));
                list.add(item);
                item.setChosen(true);
                knapSackTreeMap.put(item.getVolume(), list);
            }
            // binary decision variable
            // put to map
            // subtract volume/weight  of item put in hashsack from capacity
            setCapacity(capacity - item.getVolume());
            return true;
        } else {
            return false;
        }
    }

    public Item getItemAndRemove(int key){
        System.out.println(key);
        Item item = getTreeMap().get(key).get(0);
        System.out.println("item in tree " + item);
        getTreeMap().remove(key);
        capacity += key;
        return item;
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

    @Override
    public String toString() {
        return "Knapsack{" +
                "TreeMap=" + knapSackTreeMap +
                ", capacity=" + capacity +
                '}';
    }
}
