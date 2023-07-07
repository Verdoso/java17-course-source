package es.uib.java17.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Duration implements Serializable {
  private static final long serialVersionUID = 1L;

  private String studyType;
  private String typeId;
  private String years;

}
