package model.abstracted;

public enum Feeling {
    JOY("РАДОСТЬ"),  
    SADNESS("ГРУСТЬ"),
    ANGER("ЗЛОСТЬ"),    
    SURPRISE("УДИВЛЕНИЕ"), 
    FEAR("СТРАХ"),
    CALMNESS("СПОКОЙСТВИЕ"),  
    DELIGHT("ВОСТОРГ"),
    SCARY("СТРАХ");

    private String russianTranslation;

    Feeling(String translation) {
        this.russianTranslation = translation;
    }

    @Override
    public String toString() {
        return this.russianTranslation;
    }
}
