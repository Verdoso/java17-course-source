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

import es.uib.java17.model.Branch;
import es.uib.java17.model.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LambdasDemo {

  // Function, one parameter, one result
  public static final Function<Study, String> GET_BRANCH_NAME = study -> {
    if (study != null && study.getBranch() != null) {
      return study.getBranch()
          .getName();
    } else {
      return null;
    }
  };

  // As pointers to method handlers
  public static final Function<Study, String> GET_NAME = Study::getName;

  // We can store them as regular types!
  static final List<Function<Study, String>> GETTERS = Arrays.asList(GET_BRANCH_NAME, GET_NAME);

  // Consumer, one parameter
  // Oneliners, no need for { } or return
  public static final Consumer<Study> PRINT_STUDY = study -> log.info("Consuming study name: {}", study.getName());
  // Shortcut class
  public static final IntConsumer PRINT_VALUE = value -> log.info("Integer value: {}", value);

  private static final Random random = new Random();
  private static final int LIMIT = 5;
  // Supplier, no parameter, one result using context
  public static final Supplier<Integer> SOMETHING_RANDOM = () -> random.nextInt(LIMIT) + 1;
  // Shortcut class
  public static final IntSupplier SOME_RANDOM_INT = () -> random.nextInt(LIMIT) + 1;

  // Predicate, one parameter, boolean as result (used for filters, for
  // example)
  public static final Predicate<Integer> GREATER_THAN_20 = value -> value > 20;
  // Shortcut class
  public static final IntPredicate GREATER_THAN_13 = value -> value > 13;

  public static void main(String[] args) {
    Study sampleStudy = new Study();
    sampleStudy.setName("Study name");
    Branch sampleBranch = new Branch();
    sampleBranch.setName("Branch name");
    sampleStudy.setBranch(sampleBranch);

    // Calling functions
    log.info("GET_BRANCH_NAME.apply(sampleStudy): {}", GET_BRANCH_NAME.apply(sampleStudy));
    log.info("GET_NAME.apply(sampleStudy): {}", GET_NAME.apply(sampleStudy));

    for (Function<Study, String> getter : GETTERS) {
      log.info("Calling one getter: {} -> {}", getter.toString(), getter.apply(sampleStudy));
    }

    // Calling consumer
    PRINT_STUDY.accept(sampleStudy);

    // Calling supplier
    log.info("SOMETHING_RANDOM: {}", SOMETHING_RANDOM.get());
    log.info("SOMETHING_RANDOM: {}", SOME_RANDOM_INT.getAsInt());

    // Calling predicate
    log.info("GREATER_THAN_20: 20>20 {}", GREATER_THAN_20.test(20));
    log.info("GREATER_THAN_20: 30>20 {}", GREATER_THAN_20.test(30));

    log.info("> 20: {}",
        // Using the predicate to filter values, from plain Stream<Integer>
        Stream.of(2, 6, 9, 54, 8, 14, 3, 54, 6, 85, 3, 9, 17, 85, 3, 9, 542, 23, 5, 72)
            .filter(GREATER_THAN_20)
            .distinct()
            .sorted()
            .map(Object::toString) // Using Integer::toString is ambiguous!!!
            .collect(Collectors.joining(", ")));

    log.info("> 13: {}",
        // Using the predicate to filter values, from an IntStream
        IntStream.of(2, 6, 9, 54, 8, 14, 3, 54, 6, 85, 3, 9, 17, 85, 3, 9, 542, 23, 5, 72)
            .filter(GREATER_THAN_13)
            .distinct()
            .sorted()
            .mapToObj(Integer::toString)
            .collect(Collectors.joining(", ")));
  }
}
