package es.uib.java17.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Study implements Serializable {
  private static final long serialVersionUID = 1L;

  private String id;
  private String name;
  private String mecId;
  private String webId;
  private Branch branch;
  private Duration duration;
  private String initYear;
}
