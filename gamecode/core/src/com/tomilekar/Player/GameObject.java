package com.tomilekar.Player;


enum GameObject {

    PlAYER("Player",  (short)2),
    BUILDING("Building",(short)4),
    STANDARD("Standard",(short) 8);

    private final String label;
    private final short maskBit;
    private final short categoryBit;
    private final short groupIndex;

    GameObject(final String label, short maskBit) {
        this(label, maskBit, (short)0, (short)0);
    }

    GameObject(String label, final short maskBit, final short categoryBit,final  short groupIndex) {
        this.label = label;
        this.maskBit = maskBit;
        this.categoryBit = categoryBit;
        this.groupIndex = groupIndex;
    }

    public String getLabel() {
        return label;
    }

    public short getMaskBit() {
        return maskBit;
    }

    public short getCategoryBit() {
        return categoryBit;
    }

    public short getGroupIndex() {
        return groupIndex;
    }
}
