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

    /** CU006 - Importar candidatos (fuera del alcance de las Etapas 7 y 8). */
    void importar(List<Candidato> candidatos);
    /** CU006 - Importar candidatos (fuera del alcance de las Etapas 7 y 8). */
    void guardar(Candidato c);
    /** CU001 - Autenticar usuario (fuera del alcance de las Etapas 7 y 8). */
    void actualizarClaveAcceso(String nroCandidato, String clave);
}
