package util;

import java.util.ArrayList;


public record Response(ArrayList<?> body, Status status) {};