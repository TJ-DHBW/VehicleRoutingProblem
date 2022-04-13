package vrp;

public record Depot(int id, int x, int y) implements ILocation {
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
