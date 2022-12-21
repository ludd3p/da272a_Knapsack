import DataModels.Item;
import DataModels.Knapsack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class KnapsackController {
    private ArrayList<Item> itemList;
    private ArrayList<Knapsack> knapsackList;

    public KnapsackController() {
        generateKnapsacks();
        generateItems(30);
        greedyAlgorithm();

        for (Knapsack k : knapsackList) {
            System.out.println(k.toString());
        }

    }

    public void generateKnapsacks() {
        Random rnd = new Random();
        knapsackList = new ArrayList<>();
        knapsackList.add(new Knapsack(rnd.nextInt(10, 50)));
        knapsackList.add(new Knapsack(rnd.nextInt(10, 100)));
    }

    public void generateItems(int maxItems){
        itemList = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < maxItems; i++) {
            itemList.add(new Item(rnd.nextInt(5) + 1, rnd.nextInt(10) + 1));
        }
    }

    public void greedyAlgorithm(){
        sortItems();
        for (Item i : itemList) {
            for (Knapsack k : knapsackList) {
                if (k.checkItemFit(i)) {
                    k.addItem(i);
                    break;
                }
            }
        }
    }

    public void sortItems(){
        itemList.sort(Comparator.comparing(Item::getWeightedValue).reversed());
    }

}
