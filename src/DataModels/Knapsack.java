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

    @Override
    public String toString() {
        return "Knapsack{" +
                "maxVolume=" + maxVolume +
                ", currentVolume=" + currentVolume +
                ", totalValue=" + totalValue +
                '}';
    }
}
