package application;

/**
 * Represents the type of application this is.
 * */
public enum ApplicationType {
    STANDALONE ("Standalone"), SERVER ("Server"), CLIENT ("Client");
    // The string representation of this type
    private String _name;
    // Creates a new application type with the specified name
    private ApplicationType(String name) {
        _name = name;
    }
    
    /**
     * Returns the string representation of this ApplicationType
     * @return This ApplicationType's text representation
     * */
    @Override
    public String toString() {
        return _name;
    }
}
