package es.uib.java17;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import es.uib.academium.db.model.agora.EstudiBasic;
import es.uib.java8.LambdasDemo;
import es.uib.java8.StreamsDemo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeaturesDemo {

  // Text blocks: https://www.baeldung.com/java-text-blocks
  public static final String CAMPUSES = """
      [

          {
              "id": "E",
              "order": 2,
              "name": {
                  "en": "Ibiza",
                  "ca": "Eivissa",
                  "es": "Ibiza"
              }
          },
          {
              "id": "M",
              "order": 3,
              "name": {
                  "en": "Minorca",
                  "ca": "Menorca",
                  "es": "Menorca"
              }
          },
          {
              "id": "P",
              "order": 1,
              "name": {
                  "en": "Majorca",
                  "ca": "Mallorca",
                  "es": "Mallorca"
              }
          },
          {
              "id": "V",
              "order": 4,
              "name": {
                  "en": "Online",
                  "ca": "En línia",
                  "es": "En línea"
              }
          }

      ]
      """;

  public static void main(String[] args) {
    // TEXT BLOCKS
    log.info(CAMPUSES);

    //////////////////////////
    //////////////////////////
    // NEW STREAM METHODS
    //////////////////////////
    //////////////////////////
    // To create a Stream from a value you are not sure if it is null or not (ex.
    // user provided)
    log.info("of Nullable (Saludos): {}", Stream.ofNullable("Saludos")
        .count());

    log.info("of Nullable (null): {}", Stream.ofNullable(null)
        .count());

    // Like a for, but with streams so, filter, map, flatMap, collect...
    log.info("Iterate, now with condition");
    Stream.iterate(50, i -> i < 200, i -> i + 20)
        .map(Object::toString)
        .forEach(log::info);

    log.info("take while > 13: {}",
        // Take while & negate
        IntStream.of(2, 6, 9, 54, 8, 14, 3, 54, 6, 85, 3, 9, 17, 85, 3, 9, 542, 23, 5, 72)
            .takeWhile(LambdasDemo.MAJOR_QUE_13.negate())
            .mapToObj(Integer::toString)
            .collect(Collectors.joining(", ")));

    log.info("drop while > 13: {}",
        // Drop while & negate
        IntStream.of(2, 6, 9, 54, 8, 14, 3, 54, 6, 85, 3, 9, 17, 85, 3, 9, 542, 23, 5, 72)
            .dropWhile(LambdasDemo.MAJOR_QUE_13.negate())
            .mapToObj(Integer::toString)
            .collect(Collectors.joining(", ")));

    //////////////////////////
    //////////////////////////
    // COLLECTIONS FACTORY METHODS
    //////////////////////////
    //////////////////////////
    List<String> listOfStrings = List.of("Una", "cadena", "a", "trozos");
    Set<String> setOfStrings = Set.of("Una", "cadena", "a", "trozos");
    List<String> anotherListOfStrings = List.copyOf(setOfStrings);

    // More than 10, use Map.ofEntries
    Map<String, Integer> mapsAlso = Map.of("Uno", 1, "dos", 2, "tres", 3);

    // Careful with immutability and new restrictions!!
    int[] arrayOfInts = { 1, 2, 3, 4, 5 };
    final List<int[]> listOfArraysOfInts = List.of(arrayOfInts);
    log.info("{} -> size: {}", listOfArraysOfInts, listOfArraysOfInts.size());
    Integer[] arrayOfIntegers = { 1, 2, 3, 4, 5 };
    final List<Integer> listOfIntegers = List.of(arrayOfIntegers);
    log.info("{} -> size: {}", listOfIntegers, listOfIntegers.size());
    //
    try {
      List.of("Una", "cadena", null, "a", "trozos");
    } catch (NullPointerException e) {
      log.error("Cannot add a null to a list using List.of as opposed to Arrays.asList");
    }
    try {
      Set.of("Una", "cadena", "a", "trozos", "Una");
    } catch (IllegalArgumentException e) {
      log.error("Cannot repeat elements using Set.of");
    }
    try {
      listOfStrings.add("Cannot add, unmodifiable!!");
    } catch (UnsupportedOperationException e) {
      log.error("Cannot modify List created with factory methods!");
    }
    try {
      anotherListOfStrings.add("Cannot add, unmodifiable!!");
    } catch (UnsupportedOperationException e) {
      log.error("Cannot modify Set created with factory methods!");
    }
    try {
      mapsAlso.put("Cuatro", 4);
    } catch (UnsupportedOperationException e) {
      log.error("Cannot modify Map created with factory methods!");
    }

    //////////////////////////
    // OPTIONAL
    //////////////////////////
    Optional<String> containString = Optional.of("Sure thing");
    try {
      containString = Optional.of(null);
    } catch (NullPointerException e) {
      log.error("Cannot create an optional with of from a null value");
    }
    // Use to create from a value you are not sure it is not null (ex. user
    // provided)
    Optional<String> mightContainString = Optional.ofNullable("Sure thing");
    mightContainString = Optional.ofNullable(null);

    // New method, isEmpty
    if (mightContainString.isEmpty() == !mightContainString.isPresent()) {
      log.info("New isEmpty method to make code clearer!");
    }

    // NEVER EVER DO THIS, NEVER (never set Optional variables to null)
    Optional<String> noPleaseNever = null;
    // USE
    Optional<String> yetToInitialise = Optional.empty();

    String variable = "xxx";// comes from somewhere
    // NEVER EVER DO THIS, NEVER (create useless Optional just to if/else. Same with
    // Stream.ofNullable)
    Optional.ofNullable(variable)
        .ifPresentOrElse(value -> {
          log.info("We have value {}", value);
          // Do something with value
        }, () -> {
          log.info("We have no value");
          // Treat missing value case
        });
    // USE
    if (variable != null) {
      log.info("We have value {}", variable);
    } else {
      log.info("We have no value");
    }

    // New method, toStream, if we need a Stream of T
    Stream<String> streamFromOptional = mightContainString.stream();

    // DON'T DO THIS (isPresent + get)
    if (mightContainString.isPresent()) {
      log.info("Yes, we have value {}", mightContainString.get());
    } else {
      log.info("Ouch, we have no value");
    }

    // USE
    // Conditional execution from a provided Optional
    mightContainString.ifPresentOrElse(value -> {
      log.info("Yes, we have value {}", value);
      // Do something with value
    }, () -> {
      log.info("Ouch, we have no value");
      // Treat missing value case
    });

    // OR USE
    // Even with filter, transformation and default value
    log.info("Value provided: {}", mightContainString.filter(value -> value.startsWith("S"))
        .map(String::toUpperCase)
        // .orElse("Always " + "evaluated");
        // .orElseThrow() // Throws java.util.NoSuchElementException
        // .orElseThrow(() -> new IllegalArgumentException("We really wanted a value
        // here")))
        .orElseGet(() -> "Lazily " + "evaluated"));

    // If we want an Optional with default value
    Optional<String> itDoesContainAString = mightContainString.or(() -> Optional.of("Default string"));
    log.info("Optional with value or default: {}", itDoesContainAString);

    //////////////////////////
    // INSTANCE OF
    //////////////////////////

    Object isThisAString = "It is a String";
    if (isThisAString instanceof String stringValue) {
      log.info("Check + cast, all in one: {}", stringValue);
    }
    // Does not match null
    isThisAString = null;
    if (isThisAString instanceof String stringValue) {
      log.info("Check + cast, all in one: {}", stringValue);
    } else {
      log.info("isThisAString is null or a different class");
    }
    // Can be used in later conditions (and no need to check for null!)
    isThisAString = "Do more things";
    if (isThisAString instanceof String stringValue && stringValue.startsWith("Do")) {
      log.info("Check + cast + condition, all in one if: {}", stringValue);
    }

    //////////////////////////
    // VAR
    //////////////////////////
    Map<String, List<Map<String, DateTimeFormatter>>> formats = new HashMap<>();
    var listOfFormats = formats.values();
    listOfFormats.forEach(format -> {
      var firstEntry = format.get(0);
      // ....
    });

    //////////////////////////
    // SWITCH EXPRESSIONS
    //////////////////////////
    enum OPCIONES {
      UNA, OTRA, LA_OTRAOTRA
    }
    OPCIONES opcion = OPCIONES.UNA;
    String resultado = switch (opcion) {
      case LA_OTRAOTRA -> "La que no es la otra ni la una";
      case OTRA -> {
        log.info("Escogieron la otra!");
        yield "La que no es la una por poco";
      }
      case UNA -> "La primera";
    };
    log.info("Escogieron {}", resultado);

    //////////////////////////
    // RECORDS
    //////////////////////////
    record EstudySummary(String id, String description) {
      EstudySummary {
        Objects.requireNonNull(id);
        Objects.requireNonNull(description);
        // No need to assign parameters!!!
      }

      EstudySummary(EstudiBasic estudiBasic) {
        this(estudiBasic.getCodi(), estudiBasic.getNomCatala());
      }
    }
    log.info("-".repeat(20));
    log.info("Records");
    log.info("-".repeat(20));
    List<EstudiBasic> estudisBasics = initEstudis();
    estudisBasics.stream()
        .map(EstudySummary::new)
        .map(EstudySummary::toString)
        .forEach(log::info);
    log.info("-".repeat(20));
    log.info("-".repeat(20));

    //////////////////////////
    // NEW STRING METHODS
    //////////////////////////
    log.info("New methods -> indent '{}' vs '{}'", "Test", "Test".indent(5));
    log.info("New methods -> transform {}", "Test".transform(String::toUpperCase)); // Like a map, to be able to pass a function as parameter
    log.info("New methods -> repeat {}", "Test".repeat(3));
    log.info("New methods -> Test.isBlank: {} \"   \".isBlank: {} \"   \".isEmpty: {}", "Test".isBlank(), "  ".isBlank(), "  ".isEmpty());
    log.info("New methods -> '{}' vs strip:'{}' vs stripLeading:'{}' vs stripTrailing:'{}'", "        Test     ", "        Test     ".strip(),
        "        Test     ".stripLeading(), "        Test     ".stripTrailing());
    log.info("New methods -> lines {}", "This\nis\na\nlist\nof\nlines".lines()
        .toList());
    log.info("New methods -> formatted {}", "Test: %s - %d".formatted("an argument", 20));
    log.info("New methods -> String.format {}", String.format("Test: %s - %d", "an argument", 20));

    //////////////////////////////////////
    // PLENTY OF OTHER SMALL IMPROVEMENTS
    //////////////////////////////////////
    // Since Java 9
    log.info("------");
    log.info("Date ranges as streams of LocalDate");
    Set<DayOfWeek> usefulDays = new HashSet<>(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = startDate.plusMonths(1);
    log.info("From {} to {}", startDate, endDate);
    startDate.datesUntil(endDate)
        .filter(stepdate -> usefulDays.contains(stepdate.getDayOfWeek()))
        .forEach(stepdate -> {
          log.info("stepdate: {}: {}", stepdate, stepdate.getDayOfWeek());
        });
  }

  ////////////
  // END MAIN
  ////////////

  ////////////////////////////////////////////////
  // INTERFACE STATIC, DEFAULT AND PRIVATE METHODS
  ////////////////////////////////////////////////

  public interface DemoInterface {
    static final String WHATEVER = "whatever";

    String normalMethod();

    default String defaultImplementation() {
      return this.privateMethod();
    }

    private String privateMethod() {
      return DemoInterface.staticMethod();
    }

    static String staticMethod() {
      return DemoInterface.WHATEVER;
    }
  }

  //////////////////////////
  // FUNCTIONAL INTERFACES
  //////////////////////////
  @FunctionalInterface
  public interface FunctionThatThrowsIOException {
    String apply(String s) throws IOException;
  }

  private static Function<String, String> wrap(FunctionThatThrowsIOException f) {
    return t -> {
      try {
        return f.apply(t);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }

  // Functional interface just declares a method, so just do that!
  private static final FunctionThatThrowsIOException FUNCTION = s -> {
    if (s.startsWith("XXX")) {
      return s.toUpperCase();
    } else {
      throw new IOException("Bad!");
    }
  };

  private static final Function<String, String> CONSUME_FUNCTION = wrap(FUNCTION);

  private List<String> strings = Stream.of("una", "otra")
      .map(CONSUME_FUNCTION)
      .toList();

  //////////////////////////
  // SEALED CLASSES
  //////////////////////////

  static sealed interface APIResponse permits APICorrectResponse, APIErrorResponse {
  }

  static final class APIErrorResponse implements APIResponse {
  }

  static final class APICorrectResponse implements APIResponse {
  }

// Soon (https://openjdk.org/jeps/406) to be used as
//  var x = switch (respuesta) {
//    case null ->
//    case APICorrectResponse respuestaCorrecta  ->
//    case APIErrorResponse respuestaErronea  ->
//  }

  private static List<EstudiBasic> initEstudis() {
    List<EstudiBasic> estudis = new ArrayList<>();
    try (ObjectInputStream theOIS = new ObjectInputStream(StreamsDemo.class.getClassLoader()
        .getResourceAsStream("Estudis.ser"))) {
      EstudiBasic read = null;
      while ((read = (EstudiBasic) theOIS.readObject()) != null) {
        estudis.add(read);
      }
    } catch (EOFException e) {
      log.debug("Ya los hemos leido todos");
    } catch (Exception e) {
      log.error("Error leyendo estudios serializados", e);
    }
    log.info("Estudios leidos: {}", estudis.size());
    return estudis;
  }
}
