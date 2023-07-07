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

import es.uib.academium.db.model.agora.EstudiBasic;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StreamsDemo {

  private static final String SEPARATOR = "------";

  public static void main(String[] args) {
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
    //

    // Recorrido simple
    log.info(StreamsDemo.SEPARATOR);
    log.info("Recorrido simple");
    estudis.forEach(estudi -> log.info("{}", estudi));
    log.info(StreamsDemo.SEPARATOR);

    // Recorrer x elementos (limit)
    log.info("Limitado");
    estudis.stream()
        .limit(2)
        .forEach(estudi -> log.info(estudi.getNomCatala()));
    log.info(StreamsDemo.SEPARATOR);

    // Transformar (map)
    log.info("Limitado y usando method handles");
    estudis.stream()
        .limit(2)
        .map(EstudiBasic::getNomCatala)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Ordenar (sorted)
    log.info("Ordenado, limitado y usando method handles");
    estudis.stream()
        .map(EstudiBasic::getNomCatala)
        .sorted()
        .limit(2)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Filtrar (filter)
    log.info("Filtrado, ordenado, limitado y usando method handles");
    estudis.stream()
        .filter(e -> "O".equals(e.getDurada()
            .getCicle()))
        .map(EstudiBasic::getNomCatala)
        .sorted()
        .limit(2)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Filtros con predicados
    log.info("Filtrado quitando nulos, con predicado, ordenado, limitado y usando method handles");
    final Predicate<? super EstudiBasic> esUnMaster = e -> "O".equals(e.getDurada()
        .getCicle());
    estudis.stream()
        .filter(esUnMaster)
        .map(EstudiBasic::getNomCatala)
        .filter(Objects::nonNull)
        .sorted()
        .limit(2)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Contar (count)
    log.info("Filtrar y contar: {}", estudis.stream()
        .filter(esUnMaster)
        .count());
    log.info(StreamsDemo.SEPARATOR);

    // Comprobar existencia (anyMatch)
    log.info("Filtrar y comprobar si hay: {}", estudis.stream()
        .anyMatch(esUnMaster));
    log.info(StreamsDemo.SEPARATOR);

    // Convertir en lista (toList
    log.info("Crear una lista con los resultados");
    List<EstudiBasic> masters = estudis.stream()
        .filter(esUnMaster)
        // To get a guaranteed mutable List
        // .collect(Collectors.toCollection(ArrayList::new))
        // To "usually" get a mutable List
        // .collect(Collectors.toList())
        .toList()
    //
    ;
    masters.stream()
        .map(EstudiBasic::getNomCatala)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Convertir en mapa
    log.info("Crear un mapa con los resultados");
    Map<String, EstudiBasic> mastersMap = estudis.stream()
        .filter(esUnMaster)
        .collect(Collectors.toMap(EstudiBasic::getCodi, Function.identity()));
    mastersMap.forEach((key,
        value) -> log.info("key: {}, value: {}", key, value.getNomCatala()));
    log.info(StreamsDemo.SEPARATOR);

    // Agrupar (clave, lista de elementos segun clave)
    log.info("Agrupar");
    estudis.stream()
        .collect(Collectors.groupingBy(EstudiBasic::getBranca))
        .forEach((key,
            value) -> log.info("Branca: {}, numero d'estudis: {}", key.getNomCatala(), value.size()));
    log.info(StreamsDemo.SEPARATOR);

    // Peek (no recomendable excepto para debug)
    // Juntar -> p.e. Convertir lista de listas de elementos en lista de elementos
    log.info("Echar un vistazo y juntar streams");
    estudis.stream()
        .collect(Collectors.groupingBy(EstudiBasic::getBranca))
        .entrySet()
        .stream()
        .peek(entry -> log.info("------------ Branca -> {}", entry.getKey()
            .getNomCatala())) // NOT RECOMMENDED; COMPILER MIGHT REMOVE IT
        .flatMap(entry -> entry.getValue()
            .stream())
        .map(EstudiBasic::getNomCatala)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Construir Streams simples
    // Checking with isInstance and a cast, for example
    Object willThisBeAString = "Yes, it is!";
    Stream.of(willThisBeAString, 5)
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .forEach(log::info);
    log.info(StreamsDemo.SEPARATOR);

    // Construir streams de enteros
    IntStream.range(1, 10)
        .mapToObj(Integer::toString)
        .forEach(log::info);

    // Juntar dos Streams y comprobar si todos cumplen condicion
    Object maybeNumber = "No number!";
    Stream<Object> primer = Stream.of(maybeNumber, 5);
    Stream<Object> segon = IntStream.range(1, 10)
        .mapToObj(Integer::toString);
    boolean totNumeros = Stream.concat(primer, segon)
        .map(Object::toString)
        .allMatch(NumberUtils::isCreatable);
    log.info("Tot son números?: {}", totNumeros ? "Sí" : "No");

    // findAny, findFirst...

    // takeWhile, dropWhile... see LambdasDemo

  }
}
