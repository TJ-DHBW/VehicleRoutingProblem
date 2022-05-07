package evolution.selection;

public enum SelectionType {
    RWS, TOS;

    public String getFullName() {
        return switch (this) {
            case RWS -> "Roulette Wheel Selection";
            case TOS -> "Tournament Selection";
            default -> "Unknown SelectionType";
        };
    }
}
