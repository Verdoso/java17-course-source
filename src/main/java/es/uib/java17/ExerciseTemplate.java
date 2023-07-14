package es.uib.java17;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import es.uib.java17.model.Study;
import es.uib.java8.StreamsDemo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExerciseTemplate {

  /*
   * Usando los estudios que aparecen como ejemplo en el proyecto:
   *
   * 1) Filtrar los estudios por aquellos que tienen mecId, convertirlos en record
   * con la informacion (mecId, nombre, nombre de Rama, tipo de estudio
   * (Duration.studyType) Agruparlos por tipo de estudio e imprimir el mapa
   * subsiguiente, ordenando los estudios por nombre alfabéticamente.
   *
   * 2) Crear un conjunto de codigos de estudio (usando los métodos de factoría de
   * las colecciones) y comprobar para cada uno de ellos si existen en los
   * estudios del fichero o no, pintando en cada caso un mensaje diferente,
   * incluyendo el codigo encontrado, o no. (no usar for/if tradicionales).
   *
   * 3) Crear un bucle que pinte todos los lunes del mes de Agosto de 2023
   */
  public static void main(String[] args) {
    log.info("Aquí la implementación");
  }

  private static List<Study> initStudies() {
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
    return studies;
  }
}
