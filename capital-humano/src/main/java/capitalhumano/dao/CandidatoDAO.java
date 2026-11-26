package capitalhumano.dao;

import capitalhumano.entidades.Candidato;

import java.util.List;

public interface CandidatoDAO {
    Candidato buscarPorId(int id);
    /** Devuelve el candidato con sus cuestionarios (solo estado), para tieneCuestionarioActivo(). */
    Candidato buscarPorNroCandidato(String nro);
    Candidato buscarPorDocumento(String tipo, String nro);
    /** Criterio "contiene" en apellido, nombre y nro de candidato (CU025 paso 3). */
    List<Candidato> buscarPorCriterios(String apellido, String nombre, String nro);
}
