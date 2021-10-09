package fr.whiizyyy.insaneac.data;

public enum ClientVersion {
	
    VERSION1_7("1.7"),
    VERSION1_8("1.8"),
    VERSION1_9("1.9"),
    VERSION1_10("1.10"),
    VERSION1_11("1.11"),
    VERSION1_12("1.12"),
    VERSION1_13("1.13"),
    VERSION1_14("1.14"),
    VERSION1_15("1.15"),
    VERSION_UNSUPPORTED("Unknown");
    
    private final String name;

    private ClientVersion(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

