package model.abstracted.enums;

public enum Feeling {
    JOY("радость", "радостный"),  
    SADNESS("грусть", "грустный"),
    ANGER("злость", "злой"),    
    HUNGER("голод", "голодный"), 
    CALMNESS("спокойствие", "спокойный"),  
    DELIGHT("восторг", "восторженный"),
    SCARY("страх", "страшный"),
    SLEEPY("сонливость", "сонливый");

    private String noun;
    private String adjective;

    Feeling(String translation, String adjective) {
        this.noun = translation;
        this.adjective = adjective;
    }

    public String toAdjective() {
        return this.adjective;
    }

    public String toNoun() {
        return this.noun;
    }
}
