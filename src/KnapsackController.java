import DataModels.TreeSack;
import DataModels.Item;

import java.util.*;

public class KnapsackController {
    private ArrayList<Item> itemList;
    private ArrayList<TreeSack> treeSackList;
    public KnapsackController() {
        //Generate set of items
        generateItems(100000);
        //Sort the items
        sortItems();

        //Generate trees
        //generateTrees();      // For real use

        badTreeData();          // Generate trees with data that we know can be improved for testing purposes
        printTreeSackData();    // before algo is run
        System.out.println("--------^ BEFORE ALGORITHM ^-------\n\n");

        greedyAlgorithm();      // run initial greedy
        printTreeSackData();    // after greedy is run, before neighbourhood

        System.out.println("---------^ AFTER GREEDY ALGO ^ -----------\n\n");
        neighborhoodSearchz();  // search for better solutions in neighborhood
        printTreeSackData();    // after neighbourhood
        System.out.println("---------^ AFTER NEIGHBOURHOOD ALGO ^ -----------\n\n");

        greedyAlgorithm();      // Run greedy again to try add new item after neighborhood search
        printTreeSackData();    // Print the final result
    }

    public void printTreeSackData() {
        for (TreeSack sack: treeSackList){
            System.out.println(treeSackList.indexOf(sack));
            System.out.println(sack.toString());
            System.out.println("Remaining capacity: " + sack.getCapacity() + " value: " + sack.getTotalValue());
        }
        System.out.println("Total value of all knapsacks: " + valueOfAllKnapsacks());
    }
    public void badTreeData() {
        treeSackList = new ArrayList<>();
        treeSackList.add(new TreeSack(20));
        treeSackList.add(new TreeSack(30));
        treeSackList.add(new TreeSack(40));
        treeSackList.add(new TreeSack(40));
        treeSackList.get(0).addItem(new Item(5,5));
        treeSackList.get(0).addItem(new Item(5,5));
        treeSackList.get(0).addItem(new Item(2,2));
        treeSackList.get(0).addItem(new Item(2,2));
        treeSackList.get(1).addItem(new Item(11,10));
        treeSackList.get(1).addItem(new Item(1,2));
        treeSackList.get(1).addItem(new Item(2,2));
        treeSackList.get(1).addItem(new Item(5,5));
        treeSackList.get(1).addItem(new Item(2,4));
        treeSackList.get(1).addItem(new Item(7,7));
        treeSackList.get(2).addItem(new Item(1,1));
        treeSackList.get(2).addItem(new Item(6,1));
        treeSackList.get(2).addItem(new Item(3,1));
        treeSackList.get(3).addItem(new Item(20, 1));

    }

    /**
     * Generates knapsacks with a random amount of available volume
     */
    public void generateTrees() {
        Random rnd = new Random();
        treeSackList = new ArrayList<>();
        treeSackList.add(new TreeSack(rnd.nextInt(20, 50)));
        treeSackList.add(new TreeSack(rnd.nextInt(40, 100)));
        treeSackList.add(new TreeSack(rnd.nextInt(40, 100)));
    }

    /**
     * Generates items with random attributes volume / weight.
     * @param maxItems The amount of items to be generated.
     */
    public void generateItems(int maxItems){
        itemList = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < maxItems; i++) {
            itemList.add(new Item(rnd.nextInt(2, 80) + 1, rnd.nextInt(100) + 1));
        }
    }

    /**
     * Sorts the list of items in declining order of weighted value
     */
    public void sortItems(){
        itemList.sort(Comparator.comparing(Item::getWeightedValue).reversed());
    }

    /**
     * Our greedy algorithm.
     * Wants to fit the most valuable items into the knapsacks.
     * Prioritizes them in order so will always try to fit items in order of the treeSackList.
     */
    public void greedyAlgorithm(){
        System.out.println("Running greedy algorithm");
        Item item;
        for (int i = 0; i < itemList.size(); i++) {
            item = itemList.get(i);
            for (TreeSack t : treeSackList) {
                if (t.checkItemFit(item)) {
                    t.addItem(item);
                    itemList.remove(item);
                    i--;
                    break;
                }
            }
        }
    }
    public void greedyTree(){
        Item item;
        sortItems();
        for (int i = 0; i < itemList.size(); i++) {
            item = itemList.get(i);
            for (TreeSack sack : treeSackList) {
                if (sack.checkItemFit(item)) {
                    sack.addItem(item);
                    itemList.remove(item);
                    i--;
                    break;
                }
            }
        }
    }

    /**
     * Looks for ways to fill knapsacks to the brim by either moving and item from another knapsack that is not full,
     * or finding a pair of items that can be swapped between the knapsacks to fill it,
     * or as last resort move a smaller item from another bag to make more room in that one which can potentially then fit a brand-new item.
     */
    public void neighborhoodSearchz() {
        for (TreeSack t : treeSackList){
            if (t.getCapacity() > 0) checkForPerfectMove(t);
            if (t.getCapacity() > 0) checkForPerfectSwap(t);
            if (t.getCapacity() > 0) checkForSmallerItem(t);
        }
    }

    /**
     * Looks for an item to put in the knapsack that is to be optimized by checking if the
     * next knapsack in the list in the list contains an item which has a weight equal to the remaining capacity of
     * the knapsack that is to be optimized. This is achieved by running .contains(key) where key = remaining capacity
     * of optimizing sack.
     * @param optimizingSack sack to optimize
     */
    public void checkForPerfectMove(TreeSack optimizingSack){
        int capacity;
        int nextTreeSackIndex = ((treeSackList.indexOf(optimizingSack) + 1) % treeSackList.size());
        while (treeSackList.indexOf(optimizingSack) != nextTreeSackIndex ) {
            capacity = optimizingSack.getCapacity();
            TreeSack nextSack = treeSackList.get(nextTreeSackIndex);

            // Don't remove items from an already optimized sack
            if (nextSack.getCapacity() == 0) {
                System.out.println("Next sack is full, dont remove items. Continue.");
                nextTreeSackIndex = ((nextTreeSackIndex + 1) % treeSackList.size());
                continue;
            }

            // Checks if the next sack contains an item that can be moved to fill first knapsack without swapping items.
            if (nextSack.getTreeMap().containsKey(capacity)) {
                System.out.println("Perfect move available!");
                moveItem(optimizingSack, nextSack, nextSack.getTreeMap().get(capacity).get(0));
            }
            nextTreeSackIndex = ((nextTreeSackIndex+1) % treeSackList.size());
        }
    }

    public void checkForPerfectSwap(TreeSack optimizingSack) {
        int capacity = optimizingSack.getCapacity();
        if (capacity == 0) return;
        int nextTreeSackIndex = ((treeSackList.indexOf(optimizingSack) + 1) % treeSackList.size());
        while (treeSackList.indexOf(optimizingSack) != nextTreeSackIndex ) {
            TreeSack nextSack = treeSackList.get(nextTreeSackIndex);

            // Don't remove items from an already optimized sack
            if (nextSack.getCapacity() == 0) {
                System.out.println("Next sack is full, dont remove items. Continue.");
                nextTreeSackIndex = ((nextTreeSackIndex + 1) % treeSackList.size());
                continue;
            }
            // Checks if next knapsack contains an item that is larger than the remaining space in first knapsack to see if potential swap is possible
            if (nextSack.getTreeMap().ceilingKey(capacity) != null){
                System.out.println("Checking for perfect swap");
                searchMatchedSwap(optimizingSack, nextSack, capacity);
            }
            nextTreeSackIndex = ((nextTreeSackIndex+1) % treeSackList.size());
        }
    }

    /**
     * Looks for an item which is equal to or less than the remaining capacity of optimizing sack by running the
     * .floor(key) where key = remaining capacity of optimizing sack
     * @param optimizingSack sack to optimize
     */
    public void checkForSmallerItem(TreeSack optimizingSack){
        int capacity = optimizingSack.getCapacity();
        if (capacity == 0) return;
        int nextTreeSackIndex = ((treeSackList.indexOf(optimizingSack) + 1) % treeSackList.size());
        while (treeSackList.indexOf(optimizingSack) != nextTreeSackIndex ) {
            TreeSack nextSack = treeSackList.get(nextTreeSackIndex);

            // Don't remove items from an already optimized sack
            if (nextSack.getCapacity() == 0) {
                System.out.println("Next sack is full, dont remove items. Continue.");
                nextTreeSackIndex = ((nextTreeSackIndex + 1) % treeSackList.size());
                continue;
            }
            // Checks if next knapsack contains an item that is larger than the remaining space in first knapsack to see if potential swap is possible
            System.out.println("Checking for smaller item to move");
            if (nextSack.getTreeMap().floorKey(capacity) != null){
                int key = nextSack.getTreeMap().floorKey(capacity);
                moveItem(optimizingSack, nextSack, nextSack.getTreeMap().get(key).get(0));
            }
            nextTreeSackIndex = ((nextTreeSackIndex+1) % treeSackList.size());
        }
    }


    /**
     * Time complexity: worst case o(n) where n = nodes in tree
     * Checks for possible swaps that will fill the first knapsack perfectly.
     * Works by looking for items that matches the volume of remaining capacity plus the volume of an item to be swapped away.
     * @param optimizingSack Knapsack to be optimized
     * @param nextSack Knapsack that we're swapping with
     * @param capacity Baseline remaining capacity
     */
    public void searchMatchedSwap(TreeSack optimizingSack, TreeSack nextSack, int capacity) {
        // Iterates through the keys (unique weights) of items in first knapsack
        for (List<Item> items : optimizingSack.getTreeMap().values()) {
            // Checks next knapsack to find an item that matches remaining capacity + the volume of item to be swapped
            if (nextSack.getTreeMap().containsKey(capacity + items.get(0).getVolume())) {
                System.out.println("Perfect swap available!");
                swapItems(optimizingSack, nextSack, items.get(0), nextSack.getTreeMap().get((capacity + items.get(0).getVolume())).get(0));
                break;
            }
        }
    }


    /**
     * Method to move items between knapsacks.
     * @param to Receiving knapsack
     * @param from Donating knapsack
     * @param itemToMove The item that will be moved
     */
    public void moveItem(TreeSack to, TreeSack from, Item itemToMove){
        System.out.println("Moving item: " + itemToMove);
        from.removeItem(itemToMove);
        to.addItem(itemToMove);
    }

    /**
     * Method to swap two items between knapsacks
     * @param optimizing The knapsack being optimized
     * @param next The charity knapsack
     * @param itemFromOptimizing Item traded away
     * @param itemFromNext Item received in trade
     */
    public void swapItems(TreeSack optimizing, TreeSack next, Item itemFromOptimizing, Item itemFromNext) {
        System.out.println("Swapping item of vol: " + itemFromOptimizing + " and remaining capacity of: " + optimizing.getCapacity() + " for item of vol: " + itemFromNext);
        // First takes out the items of respective knapsack
        optimizing.removeItem(itemFromOptimizing);
        next.removeItem(itemFromNext);
        // Adds items to the other knapsack
        optimizing.addItem(itemFromNext);
        next.addItem(itemFromOptimizing);
        System.out.println("Optimizing sack new capacity: " + optimizing.getCapacity());
    }



    // LEGACY, not used.
    public void neighbourHoodSearch(){
        // iterate over list of knapsacks
        for (int i = 0; i < treeSackList.size(); i++) {
            TreeSack optimizeSack = treeSackList.get(i);
            System.out.println("optimizing treesack: "+ (i+1) + "/ " + treeSackList.size()  + " ["+ optimizeSack.toString() + "]");
            int remainingCapacity = optimizeSack.getCapacity();

            // optimized sack is already optimized
            if(remainingCapacity == 0){
                System.out.println("Knapsack already full");
                continue;
            }

            // iterate over i+1
            for (int j = i+1; j < treeSackList.size(); j++) {
                TreeSack sack = treeSackList.get(j);

                // don't remove items from an already optimized sack
                if(sack.getCapacity() == 0){ continue; }

                // if the sack (i+1) contains an item which corresponds to space left in knapsack to optimize
                if(findPerfectMatch(sack,remainingCapacity)){
                    // get item from sack where key = capacity left in sack to optimize
                    Item item = sack.getTreeMap().get(remainingCapacity).get(0);
                    // remove the item from the sack
                    moveItem(optimizeSack, sack, item);
                    System.out.println("perfect match");
                }

                else if (!findPerfectMatch(sack, remainingCapacity)){

                    for (int k = j + 1; k < treeSackList.size(); k++) {
                        TreeSack nextSack = treeSackList.get(k);

                        if(findPerfectMatch(nextSack, remainingCapacity)){
                            Item item = nextSack.getTreeMap().get(remainingCapacity).get(0);
                            moveItem(optimizeSack, nextSack, item);
                            System.out.println("perfect match in deeper knapsack");
                        }
                    }
                }

                // else search for highest weight in proximity to remainingCapacity
                else if(sack.getTreeMap().floorKey(remainingCapacity) != null) {
                    // Returns the greatest key less than or equal to the given key, or null if there is no such key in Θ(log n) time.
                    int key = sack.getTreeMap().floorKey(remainingCapacity);
                    // fetch item found by key in Θ(log n) time
                    Item item = sack.getTreeMap().get(key).get(0);
                    moveItem(optimizeSack, sack, item);
                    System.out.println("floor match: " + item );

                } else System.out.println("No key found");
            }
            System.out.println("\nRESULT OF OPTIMIZED KNAPSACK:" + optimizeSack.toString()  + "\n");
        }
    }
    public boolean findPerfectMatch(TreeSack treeSack, Integer perfectFit){
        return treeSack.getTreeMap().containsKey(perfectFit);
    }


    private int valueOfAllKnapsacks() {
        int totalValue = 0;
        for(TreeSack sack : treeSackList) totalValue += sack.getTotalValue();
        return totalValue;
    }




}
