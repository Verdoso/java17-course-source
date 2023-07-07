package es.uib.java17.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Branch implements Serializable {
  private static final long serialVersionUID = 1L;

  private String id;
  private String name;
}
