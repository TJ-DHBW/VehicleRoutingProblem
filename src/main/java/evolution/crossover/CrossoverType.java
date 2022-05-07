package evolution.crossover;

public enum CrossoverType {
    HRX, UPX;

    public String getFullName() {
        return switch (this) {
            case HRX -> "Heuristic Crossover";
            case UPX -> "Uniform Partially Mapped Crossover";
            default -> "Unknown CrossoverType";
        };
    }
}
