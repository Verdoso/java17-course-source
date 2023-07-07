package es.uib.java8;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.math.NumberUtils;

import es.uib.java17.model.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StreamsDemo {

  private static final String SEPARATOR = "------";

  public static void main(String[] args) {
    List<Study> studies = new ArrayList<>();
    try (ObjectInputStream theOIS = new ObjectInputStream(StreamsDemo.class.getClassLoader()
        .getResourceAsStream("Studies.ser"))) {
      Study read = null;
      while ((read = (Study) theOIS.readObject()) != null) {
        studies.add(read);
      }
    } catch (EOFException e) {
      log.debug("all studies read");
    } catch (Exception e) {
      log.error("Error reading serialised studies", e);
    }
    log.info("Studies read: {}", studies.size());
    //

    // Simply looping
    log.info(StreamsDemo.SEPARATOR);
    log.info("Simple loop, meh");
    studies.forEach(study -> log.info("{}", study));
    log.info(StreamsDemo.SEPARATOR);

    // Looping over x elements (limit)
    log.info("Just some elements");
    studies.stream()
        .limit(2)
        .forEach(study -> log.info(study.getName()));
    log.info(StreamsDemo.SEPARATOR);

    // Transform (map)
    log.info("Just some elements and using method handles");
    studies.stream()
        .limit(2)
        .map(Study::getName)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Sort (sorted)
    log.info("Sorting, just some elements and using method handles");
    studies.stream()
        .map(Study::getName)
        .sorted()
        .limit(2)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Filter (filter)
    log.info("Filtering, sorting, just some elements and using method handles");
    studies.stream()
        .filter(e -> "O".equals(e.getDuration()
            .getStudyType()))
        .map(Study::getName)
        .sorted()
        .limit(2)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Filters with predicates
    log.info("Filtering out nulls, with a predicate, sort, only a limited number and using method handles");
    final Predicate<? super Study> itsAMaster = e -> e.getDuration() != null && "O".equals(e.getDuration()
        .getStudyType());
    studies.stream()
        .filter(itsAMaster)
        .map(Study::getName)
        .filter(Objects::nonNull)
        .sorted()
        .limit(2)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Count (count)
    log.info("Filter and count: {}", studies.stream()
        .filter(itsAMaster)
        .count());
    log.info(StreamsDemo.SEPARATOR);

    // Check for existence (anyMatch)
    log.info("Check for existence: {}", studies.stream()
        .anyMatch(itsAMaster));
    log.info(StreamsDemo.SEPARATOR);

    // Transform to list (toList)
    log.info("Transform to list");
    List<Study> masters = studies.stream()
        .filter(itsAMaster)
        // To get a guaranteed mutable List
        // .collect(Collectors.toCollection(ArrayList::new))
        // To "usually" get a mutable List
        // .collect(Collectors.toList())
        .toList()
    //
    ;
    masters.stream()
        .map(Study::getName)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Transform to map
    log.info("Transform to a map");
    Map<String, Study> mastersMap = studies.stream()
        .filter(itsAMaster)
        .collect(Collectors.toMap(Study::getId, Function.identity()));
    mastersMap.forEach((key,
        value) -> log.info("key: {}, value: {}", key, value.getName()));
    log.info(StreamsDemo.SEPARATOR);

    // Group by (key, elements indexed by that key)
    log.info("Group by");
    studies.stream()
        .collect(Collectors.groupingBy(Study::getBranch))
        .forEach((key,
            value) -> log.info("Branch: {}, number of studies: {}", key.getName(), value.size()));
    log.info(StreamsDemo.SEPARATOR);

    // Peek (not recommended but for debugging purposes)
    // Flatten -> p.e. map a "list" of "lists" of elements into a "list" of elements
    log.info("Peek and join streams");
    studies.stream()
        .collect(Collectors.groupingBy(Study::getBranch))
        .entrySet()
        .stream()
        .peek(entry -> log.info("------------ Branch -> {}", entry.getKey()
            .getName())) // NOT RECOMMENDED; COMPILER MIGHT REMOVE IT
        .flatMap(entry -> entry.getValue()
            .stream())
        .map(Study::getName)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Building simple streams

    // Checking with isInstance and a cast, for example
    Object willThisBeAString = "Yes, it is!";
    Stream.of(willThisBeAString, 5)
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Integer streams, from a range
    IntStream.range(1, 10)
        .mapToObj(Integer::toString)
        .forEach(log::info);

    // Join two Streams and see if all elements match a condition
    Object maybeNumber = "No number!";
    Stream<Object> primer = Stream.of(maybeNumber, 5);
    Stream<Object> segon = IntStream.range(1, 10)
        .mapToObj(Integer::toString);
    boolean allAreNumbers = Stream.concat(primer, segon)
        .map(Object::toString)
        .allMatch(NumberUtils::isCreatable);
    log.info("Are all numbers?: {}", allAreNumbers ? "Yes" : "No");

    // findAny, findFirst...

    // takeWhile, dropWhile... see LambdasDemo

  }
}
