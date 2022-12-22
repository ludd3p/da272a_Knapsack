import DataModels.Item;
import DataModels.Knapsack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class KnapsackController {
    private ArrayList<Item> itemList;
    private ArrayList<Knapsack> knapsackList;

    public KnapsackController() {
        generateItems(100);
        //generateKnapsacks();
        //greedyAlgorithm();
        badKnapsackData();


        for (Knapsack k : knapsackList) {
            System.out.println(k.toString());
        }

        neighborhood();

        for (Knapsack k : knapsackList) {
            System.out.println(k.toString());
        }
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

    public void neighborhood(){
        int combinedKnapsackValue = 0;
        boolean spaceExists = false;
        ArrayList <Knapsack> localSearchResult = new ArrayList<>();

        for (Knapsack k : knapsackList) {
            combinedKnapsackValue += k.getTotalValue();
            if (!spaceExists) spaceExists = k.checkFull();
        }

        System.out.println("Remaining items " + itemList.size());
        if (spaceExists && itemList.size() > 0) {
            System.out.println("Free space in at least one of the knapsacks, trying to optimize space used");
                localSearchResult = optimizeKnapsacks();
                if (combinedKnapsackValue < valueOfAllKnapsacks(localSearchResult)) {
                    System.err.println("Better solution found using local search.");
                }
        }
        System.out.println("Final result of neighborhood search:" + "\n");
        System.out.println("GREEDY VALUE = " + combinedKnapsackValue +", LOCAL OPTIMA VALUE = " + valueOfAllKnapsacks(localSearchResult) + "\n");
    }

    private static int valueOfAllKnapsacks(ArrayList<Knapsack> knapsacks) {
        int totalValue = 0;
        for(Knapsack sack : knapsacks) totalValue += sack.getTotalValue();
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

                    if (nextKnapsackHeaviestItem.getVolume() != 0) { // Checks if volume is <0 because every item has a volume of >=1 except if getHeaviestItem doesn't have a matching item.
                        nextKnapsack.removeItem(nextKnapsackHeaviestItem); // Removes the item that is moved from donor list.
                        k.addItem(nextKnapsackHeaviestItem); // Adds the item to the recipient list.
                        System.out.println("Item: " + nextKnapsackHeaviestItem + " successfully moved to list: " + knapsackList.indexOf(k) + " from list: " + nextKnapsackIndex);

                    }
                    nextKnapsackIndex = ((nextKnapsackIndex+1) % knapsackList.size()); // Sets next index to check
                }

                Item item;
                for (int i = 0; i < itemList.size(); i++) { // Iterates through the remaining items to see if there is one that fits.
                    item = itemList.get(i);
                    if (k.checkItemFit(item)) {
                        k.addItem(item);
                        itemList.remove(item);
                        i--;
                    }
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
