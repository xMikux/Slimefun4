package io.github.thebusybiscuit.slimefun4.core.attributes;

public enum MachineType {

    CAPACITOR("電容"),
    GENERATOR("發電機"),
    MACHINE("機器");

    private final String suffix;

    MachineType(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return suffix;
    }

}
