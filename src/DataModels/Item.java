package DataModels;

public class Item {
    private int volume;
    private int value;
    //adding binary decision variable
    private boolean isChosen;

    public Item (int volume, int value) {
        this.volume = volume;
        this.value = value;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getWeightedValue() {
        return (value/volume);
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    @Override
    public String toString() {
        return "Item{" +
                "volume=" + volume +
                ", value=" + value +
                '}';
    }
}
