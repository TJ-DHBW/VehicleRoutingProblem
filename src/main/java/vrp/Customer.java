package vrp;

import evolution.IGene;

public record Customer(int id, int x, int y, int demand, int readyTime, int dueDate, int serviceTime) implements ILocation, IGene {
    @Override
    public int getId() {
        return this.id();
    }

    @Override
    public int getX() {
        return this.x();
    }

    @Override
    public int getY() {
        return this.y();
    }
}
