package model.objects;

import model.abstracted.Hour;


public class Environment {
    public static String explains_with(String message) {
        return "объяснялось тем, что " + message;
    }

    public static String could_be(String message) {
        return "можно было " + message;
    }

    public static String located_in(Object place) {
        return "находясь в " + place.toString();
    }

    public static String time_went(Hour hour) {
        return "прошло " + hour.toString();
    }
}
