package DataModels;

import java.util.ArrayList;

public class Knapsack {
    private ArrayList<Item> items;
    private int maxVolume;
    private int currentVolume;
    private int totalValue;

    public Knapsack(int maxVolume){
        items = new ArrayList<>();
        this.maxVolume = maxVolume;
    }

    public void addValue(int value){
        totalValue += value;
    }

    public void addVolume(int volume){
        currentVolume += volume;
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    public boolean checkItemFit(Item item){
        return item.getVolume() <= (maxVolume - currentVolume);
    }

    public void addItem(Item item) {
        items.add(item);
        totalValue += item.getValue();
        currentVolume += item.getVolume();
    }

    public int getTotalValue(){
        return totalValue;
    }

    public boolean checkFull(){
        return currentVolume != maxVolume;
    }

    public int getRemainingVolume() {
        return maxVolume - currentVolume;
    }

    public Item getHeaviestItem(int weight) {
        Item bestMatch = new Item(0,0);
        for (Item i : items) {
           if (i.getVolume() <= weight){
               if (bestMatch.getVolume() < i.getVolume()) bestMatch = i;
           }
        }
        return bestMatch;
    }

    @Override
    public String toString() {
        return "Knapsack{" +
                "maxVolume=" + maxVolume +
                ", currentVolume=" + currentVolume +
                ", totalValue=" + totalValue +
                '}';
    }
}
