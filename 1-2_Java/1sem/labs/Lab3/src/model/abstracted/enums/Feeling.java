package model.abstracted.enums;

public enum Feeling {
    JOY("радость"),  
    SADNESS("грусть"),
    ANGER("злость"),    
    HUNGER("голод"), 
    CALMNESS("спокойствие"),  
    DELIGHT("восторг"),
    SCARY("страх"),
    SLEEPY("сонливость");

    private String text;

    Feeling(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
