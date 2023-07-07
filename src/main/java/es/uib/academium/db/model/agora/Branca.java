package es.uib.academium.db.model.agora;

import java.io.Serializable;

import lombok.Data;

@Data
public class Branca implements Serializable {
	private static final long serialVersionUID = 1L;

	private String codi;
	private String nomCatala;
	private String nomCastella;
	private String nomAngles;
}
