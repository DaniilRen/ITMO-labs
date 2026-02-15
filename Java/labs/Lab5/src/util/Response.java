package util;

import java.util.List;


public record Response(List<?> body, Status status) {};