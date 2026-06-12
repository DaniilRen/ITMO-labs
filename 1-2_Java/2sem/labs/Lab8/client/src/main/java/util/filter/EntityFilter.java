package util.filter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.models.Entity;
import common.models.Route;

public final class EntityFilter {
  public enum Operator {
  EQ("="),
  NE("!="),
  GT(">"),
  LT("<"),
  GTE(">="),
  LTE("<="),
  CONTAINS("contains");

    private final String symbol;

    Operator(String symbol) {
      this.symbol = symbol;
    }

    public String symbol() {
      return symbol;
    }

    public static Operator fromSymbol(String symbol) {
      for (Operator op : values()) {
        if (op.symbol.equals(symbol)) {
          return op;
        }
      }
      return EQ;
    }
  }

  public record Condition(String field, Operator operator, String value) {}

  public static List<Entity> apply(List<Entity> source, List<Condition> conditions) {
    if (conditions == null || conditions.isEmpty()) {
      return source;
    }
    Predicate<Entity> predicate =
        conditions.stream()
            .map(EntityFilter::toPredicate)
            .reduce(Predicate::and)
            .orElse(entity -> true);

  return source.stream().filter(predicate).collect(Collectors.toList());
  }

  public static List<Entity> sort(List<Entity> source, String field, boolean ascending) {
    Stream<Entity> stream = source.stream();
    return stream.sorted(EntityFilter.comparator(field, ascending)).collect(Collectors.toList());
  }

  private static Predicate<Entity> toPredicate(Condition condition) {
    return entity -> {
      if (!(entity instanceof Route route)) {
        return false;
      }
      Comparable<?> left = extractComparable(route, condition.field());
      if (left == null) {
        return false;
      }
      return compare(left, condition.operator(), condition.value());
    };
  }

  private static Comparable<?> extractComparable(Route route, String field) {
    return switch (field) {
      case "id" -> route.getId();
      case "name" -> route.getName();
      case "x" -> route.getCoordinates() != null ? route.getCoordinates().getX() : null;
      case "y" -> route.getCoordinates() != null ? route.getCoordinates().getY() : null;
      case "distance" -> route.getDistance();
      case "author" -> route.getAuthor();
      case "creationDate" -> route.getCreationDate();
      case "from" ->
          route.getLocationFrom() != null ? route.getLocationFrom().getName() : null;
      case "to" -> route.getLocationTo() != null ? route.getLocationTo().getName() : null;
      default -> null;
    };
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static boolean compare(Comparable left, Operator operator, String rawValue) {
    try {
      Object right = parseValue(left, rawValue);
      int cmp = left.compareTo(right);
      return switch (operator) {
        case EQ -> cmp == 0;
        case NE -> cmp != 0;
        case GT -> cmp > 0;
        case LT -> cmp < 0;
        case GTE -> cmp >= 0;
        case LTE -> cmp <= 0;
        case CONTAINS -> left.toString().toLowerCase().contains(rawValue.toLowerCase());
      };
    } catch (Exception e) {
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  private static Object parseValue(Comparable<?> sample, String rawValue) {
    if (sample instanceof Integer) {
      return Integer.parseInt(rawValue);
    }
    if (sample instanceof Long) {
      return Long.parseLong(rawValue);
    }
    if (sample instanceof Float) {
      return Float.parseFloat(rawValue);
    }
    if (sample instanceof Double) {
      return Double.parseDouble(rawValue);
    }
    if (sample instanceof LocalDateTime) {
      return LocalDateTime.parse(rawValue);
    }
    return rawValue;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static java.util.Comparator<Entity> comparator(String field, boolean ascending) {
    java.util.Comparator<Entity> cmp =
        (a, b) -> {
          if (!(a instanceof Route ra) || !(b instanceof Route rb)) {
            return 0;
          }
          Comparable va = extractComparable(ra, field);
          Comparable vb = extractComparable(rb, field);
          if (va == null && vb == null) return 0;
          if (va == null) return -1;
          if (vb == null) return 1;
          return va.compareTo(vb);
        };
    return ascending ? cmp : cmp.reversed();
  }
}
