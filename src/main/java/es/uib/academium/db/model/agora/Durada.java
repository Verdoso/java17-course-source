package es.uib.academium.db.model.agora;

import java.io.Serializable;

import lombok.Data;

@Data
public class Durada implements Serializable {
	private static final long serialVersionUID = 1L;

	private String cicle;
	private String titol;
	private String anys;

	public String getCodi() {
		return String.join("-", cicle, titol);
	}
}
