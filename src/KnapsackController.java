import DataModels.TreeSack;
import DataModels.Item;
import DataModels.Knapsack;

import java.util.*;

public class KnapsackController {
    private ArrayList<Item> itemList;
    private ArrayList<Knapsack> knapsackList;
    private ArrayList<TreeSack> treeSackList;
    public KnapsackController() {
        generateItems(100);
        badTreeData();

        //greedyTree();
        neighbourHoodSearch2();

        //generateKnapsacks();
        //greedyAlgorithm();
        badKnapsackData();

        for (TreeSack sack: treeSackList){
            System.out.println(sack.toString());
        }
        /*
        for (Knapsack k : knapsackList) {
            System.out.println(k.toString());
        }

       // neighborhood();

        for (Knapsack k : knapsackList) {
            System.out.println(k.toString());
        }

         */
    }
    public void badTreeData() {
        treeSackList = new ArrayList<>();
        treeSackList.add(new TreeSack(20));
        treeSackList.add(new TreeSack(30));
        treeSackList.add(new TreeSack(40));
        treeSackList.get(0).addItem(new Item(5,5));
        treeSackList.get(0).addItem(new Item(5,5));
        treeSackList.get(0).addItem(new Item(2,2));
        treeSackList.get(0).addItem(new Item(2,2));
        treeSackList.get(1).addItem(new Item(11,10));
        treeSackList.get(1).addItem(new Item(2,2));
        treeSackList.get(1).addItem(new Item(2,2));
        treeSackList.get(1).addItem(new Item(5,5));
        treeSackList.get(1).addItem(new Item(2,4));
        treeSackList.get(1).addItem(new Item(7,7));
        treeSackList.get(2).addItem(new Item(1,1));
        treeSackList.get(2).addItem(new Item(6,1));

    }
    /**
     * Generates Knapsacks with data that we know can be improved
     */
    public void badKnapsackData() {
        knapsackList = new ArrayList<>();
        knapsackList.add(new Knapsack(20));
        knapsackList.add(new Knapsack(30));
        knapsackList.add(new Knapsack(40));
        knapsackList.get(0).addItem(new Item(5,5));
        knapsackList.get(0).addItem(new Item(5,5));
        knapsackList.get(0).addItem(new Item(2,2));
        knapsackList.get(0).addItem(new Item(2,2));
        knapsackList.get(1).addItem(new Item(10,10));
        knapsackList.get(1).addItem(new Item(2,2));
        knapsackList.get(1).addItem(new Item(2,2));
        knapsackList.get(1).addItem(new Item(5,5));
        knapsackList.get(1).addItem(new Item(4,4));
        knapsackList.get(1).addItem(new Item(7,7));
        knapsackList.get(2).addItem(new Item(1,1));
    }

    /**
     * Generates knapsacks with a random amount of available volume
     */
    public void generateKnapsacks() {
        Random rnd = new Random();
        knapsackList = new ArrayList<>();
        knapsackList.add(new Knapsack(rnd.nextInt(20, 50)));
        knapsackList.add(new Knapsack(rnd.nextInt(40, 100)));
        knapsackList.add(new Knapsack(rnd.nextInt(40, 100)));
    }

    /**
     * Generates items with random attributes volume / weight.
     * @param maxItems The amount of items to be generated.
     */
    public void generateItems(int maxItems){
        itemList = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < maxItems; i++) {
            itemList.add(new Item(rnd.nextInt(8, 80) + 1, rnd.nextInt(100) + 1));
        }
    }

    /**
     * Our greedy algorithm.
     * Wants to fit the most valuable items into the knapsacks.
     * Calls a method to sort the list of items based on a weighted value of value/weight
     */
    public void greedyAlgorithm(){
        Item item;
        sortItems();
        for (int i = 0; i < itemList.size(); i++) {
            item = itemList.get(i);
            for (Knapsack k : knapsackList) {
                if (k.checkItemFit(item)) {
                    k.addItem(item);
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

    public void neighborhood(){
        int greedyKnapsackValue = 0;
        boolean spaceExists = false;

        for (Knapsack k : knapsackList) {
            greedyKnapsackValue += k.getTotalValue();
            if (!spaceExists) spaceExists = k.checkFull();
        }

        if (spaceExists && itemList.size() > 0) {
            System.out.println("Free space in at least one of the knapsacks, trying to optimize space used");
                knapsackList = optimizeKnapsacks();
                if (greedyKnapsackValue < valueOfAllKnapsacks()) {
                    System.out.println("Better solution found using neighborhood search.");
                    System.out.println("Combined value after greedy algo = " + greedyKnapsackValue + ". Combined value after neighborhood search = " + valueOfAllKnapsacks());
                }
        }
    }
    // expected to run in  n^2
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
                    simpleSwap(sack, optimizeSack, item);
                    System.out.println("perfect match");
                }

                else if (!findPerfectMatch(sack, remainingCapacity)){

                    for (int k = j + 1; k < treeSackList.size(); k++) {
                        TreeSack nextSack = treeSackList.get(k);

                        if(findPerfectMatch(nextSack, remainingCapacity)){
                            Item item = nextSack.getTreeMap().get(remainingCapacity).get(0);
                            simpleSwap(nextSack, optimizeSack, item);
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
                    simpleSwap(sack, optimizeSack, item);
                    System.out.println("floor match: " + item );

                } else System.out.println("No key found");
            }
            System.out.println("\nRESULT OF OPTIMIZED KNAPSACK:" + optimizeSack.toString()  + "\n");
        }
    }
    public boolean findPerfectMatch(TreeSack treeSack, Integer perfectFit){
        return treeSack.getTreeMap().containsKey(perfectFit);
    }
    public void simpleSwap(TreeSack from, TreeSack to, Item itemToSwap){
        from.removeItem(itemToSwap);
        to.addItem(itemToSwap);
    }


    public void neighbourHoodSearch2(){
        // iterate over list of knapsacks
        for (TreeSack optimizingSack : treeSackList) {
            int capacity = optimizingSack.getCapacity();

            // optimized sack is already optimized
            if(capacity == 0) continue;

            // iterate over neighboring knapsacks
            int nextHashSackIndex = ((treeSackList.indexOf(optimizingSack) + 1) % treeSackList.size());
            while (treeSackList.indexOf(optimizingSack) != nextHashSackIndex ) {
                TreeSack nextSack = treeSackList.get(nextHashSackIndex);

                // don't remove items from an already optimized sack
                if(nextSack.getCapacity() == 0){
                    nextHashSackIndex = ((nextHashSackIndex+1) % treeSackList.size());
                    continue;
                }

                // if the sack contains an item which corresponds to space left in knapsack to optimize
                if(nextSack.getTreeMap().containsKey(capacity)){
                    // get item from sack where key = capacity left in sack to optimize
                    Item item = nextSack.getTreeMap().get(capacity).get(0);
                    // remove the item from the sack
                    nextSack.removeItem(item);
                    // add the item to the optimizedSack
                    optimizingSack.addItem(item);
                }
                else if (nextSack.getTreeMap().ceilingKey(capacity) != null){
                    for (List<Item> items : optimizingSack.getTreeMap().values()) {
                        for (Item item : items) {
                            if (nextSack.getTreeMap().containsKey(capacity + item.getVolume())) {
                                // get item from sack where key = capacity left in sack to optimize
                                Item item2 = nextSack.getTreeMap().get((capacity + item.getVolume())).get(0);
                                // remove the item from the sack
                                nextSack.removeItem(item);
                                // add the item to the optimizedSack
                                optimizingSack.addItem(item2);
                        }

                        }
                    }
                }
                nextHashSackIndex = ((nextHashSackIndex+1) % treeSackList.size());
            }
        }
    }


    private int valueOfAllKnapsacks() {
        int totalValue = 0;
        for(Knapsack sack : knapsackList) totalValue += sack.getTotalValue();
        return totalValue;
    }

    public ArrayList <Knapsack> optimizeKnapsacks() {
        System.out.println("Method: optimize knapsacks");

        for (Knapsack k : knapsackList) {
            if (k.getRemainingVolume() > 0) {
                int nextKnapsackIndex = ((knapsackList.indexOf(k) + 1) % knapsackList.size()); // Calculates which index the next Knapsack has, modulus to check if restart at 0
                while (knapsackList.indexOf(k) != nextKnapsackIndex ) { //Iterates through the list of Knapsacks, usable if >2 Knapsacks exist. Will break when nextKnapsackIndex == index of Knapsack that is being checked.

                    Knapsack nextKnapsack = knapsackList.get(nextKnapsackIndex); // Fetches next Knapsack in line
                    Item nextKnapsackHeaviestItem = nextKnapsack.getHeaviestItem(k.getRemainingVolume()); // Checks if another Knapsack contains an item that is <= to the remaining space in the current Knapsack

                    if (!nextKnapsack.checkFull() && nextKnapsackHeaviestItem.getVolume() != 0) { // Checks if volume is !0 because every item has a volume of >=1 except if getHeaviestItem doesn't have a matching item.
                        nextKnapsack.removeItem(nextKnapsackHeaviestItem); // Removes the item that is moved from donor list.
                        k.addItem(nextKnapsackHeaviestItem); // Adds the item to the recipient list.
                        System.out.println("Item: " + nextKnapsackHeaviestItem + " successfully moved to list: " + knapsackList.indexOf(k) + " from list: " + nextKnapsackIndex);
                        Item item;
                        for (int i = 0; i < itemList.size(); i++) { // Iterates through the remaining items to see if there is one that fits.
                            item = itemList.get(i);
                            if (nextKnapsack.checkItemFit(item)) {
                                nextKnapsack.addItem(item);
                                itemList.remove(item);
                                i--;
                                System.out.println("Item added: " + item + " to list: " + knapsackList.indexOf(nextKnapsack));
                            }
                        }
                    }

                    nextKnapsackIndex = ((nextKnapsackIndex+1) % knapsackList.size()); // Sets next index to check
                }



            }
        }
        return knapsackList;
    }

    /**
     * Sorts the list of items in declining order of weighted value
     */
    public void sortItems(){
        itemList.sort(Comparator.comparing(Item::getWeightedValue).reversed());
    }

}
