package evolution.mutation;

public enum MutationType {
    SCM, CIM;

    public String getFullName(){
        return switch (this){
            case SCM -> "Scramble Mutation";
            case CIM -> "Centre Inverse Mutation";
            default -> "Unknown MutationType";
        };
    }
}
