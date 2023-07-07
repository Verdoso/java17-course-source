package es.uib.java8;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import es.uib.academium.db.model.agora.Branca;
import es.uib.academium.db.model.agora.EstudiBasic;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LambdasDemo {

  // Function, one parameter, one result
  public static final Function<EstudiBasic, String> GET_NOM_BRANCA = estudiBasic -> {
    if (estudiBasic != null && estudiBasic.getBranca() != null) {
      return estudiBasic.getBranca()
          .getNomCatala();
    } else {
      return null;
    }
  };

  // As pointers to method handlers
  public static final Function<EstudiBasic, String> GET_NOM = EstudiBasic::getNomCatala;

  // We can store them as regular types!
  static final List<Function<EstudiBasic, String>> GETTERS = Arrays.asList(GET_NOM_BRANCA, GET_NOM);

  // Consumer, one parameter
  // Oneliners, no need for { } or return
  public static final Consumer<EstudiBasic> PRINT_ESTUDIBASIC = estudiBasic -> log.info("Consuming nom del estudi: {}", estudiBasic.getNomCatala());
  // Shortcut class
  public static final IntConsumer PRINT_VALUE = value -> log.info("Integer value: {}", value);

  private static final Random random = new Random();
  private static final int LIMIT = 5;
  // Supplier, no parameter, one result using context
  public static final Supplier<Integer> DAME_ALEATORIO = () -> random.nextInt(LIMIT) + 1;
  // Shortcut class
  public static final IntSupplier DAME_ALEATORIO_INT = () -> random.nextInt(LIMIT) + 1;

  // Predicate, one parameter, boolean as result (used for filters, for
  // example)
  public static final Predicate<Integer> MAJOR_QUE_20 = value -> value > 20;
  // Shortcut class
  public static final IntPredicate MAJOR_QUE_13 = value -> value > 13;

  public static void main(String[] args) {
    EstudiBasic sampleEstudiBasic = new EstudiBasic();
    sampleEstudiBasic.setNomCatala("Nom en catala");
    Branca sampleBranca = new Branca();
    sampleBranca.setNomCatala("Nom de la branca");
    sampleEstudiBasic.setBranca(sampleBranca);

    // Calling functions
    log.info("GET_NOM_BRANCA.apply(sampleEstudiBasic): {}", GET_NOM_BRANCA.apply(sampleEstudiBasic));
    log.info("GET_NOM.apply(sampleEstudiBasic): {}", GET_NOM.apply(sampleEstudiBasic));

    for (Function<EstudiBasic, String> getter : GETTERS) {
      log.info("Calling one getter: {} -> {}", getter.toString(), getter.apply(sampleEstudiBasic));
    }

    // Calling consumer
    PRINT_ESTUDIBASIC.accept(sampleEstudiBasic);

    // Calling supplier
    log.info("DAME_ALEATORIO: {}", DAME_ALEATORIO.get());
    log.info("DAME_ALEATORIO: {}", DAME_ALEATORIO_INT.getAsInt());

    // Calling predicate
    log.info("MAJOR_QUE_20: 20>20 {}", MAJOR_QUE_20.test(20));
    log.info("MAJOR_QUE_20: 30>20 {}", MAJOR_QUE_20.test(30));

    log.info("> 20: {}",
        // Using the predicate to filter values
        Stream.of(2, 6, 9, 54, 8, 14, 3, 54, 6, 85, 3, 9, 17, 85, 3, 9, 542, 23, 5, 72)
            .filter(MAJOR_QUE_20)
            .distinct()
            .sorted()
            .map(Object::toString) // Using Integer::toString is ambiguous!!!
            .collect(Collectors.joining(", ")));

    log.info("> 13: {}",
        // Using the predicate to filter values
        IntStream.of(2, 6, 9, 54, 8, 14, 3, 54, 6, 85, 3, 9, 17, 85, 3, 9, 542, 23, 5, 72)
            .filter(MAJOR_QUE_13)
            .distinct()
            .sorted()
            .mapToObj(Integer::toString)
            .collect(Collectors.joining(", ")));
  }
}
