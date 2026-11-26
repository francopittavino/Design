package capitalhumano.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Pregunta entre gestor y pantalla. Se usa para:
 *  - la grilla del CU020 (competencia, factor, enunciado),
 *  - el alta del CU021 (ids de competencia/factor/opción + ponderaciones por ítem),
 *  - el wizard del CU026 (id = id de PreguntaCuestionario, opciones = ítems de la opción de respuesta).
 */
public class PreguntaDTO {
    private int id;
    private String nombre;
    private String enunciado;
    private String descripcion;
    private List<OpcionRespuestaDTO> opciones = new ArrayList<>();

    private int idCompetencia;
    private String competencia;
    private int idFactor;
    private String factor;
    private int idOpcionRespuesta;
    /** idItemOpcionDeRespuesta -> ponderación (texto tal cual lo cargó el actor, se valida en el gestor). */
    private Map<Integer, String> ponderaciones = new LinkedHashMap<>();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public List<OpcionRespuestaDTO> getOpciones() { return opciones; }
    public void setOpciones(List<OpcionRespuestaDTO> opciones) { this.opciones = opciones; }
    public int getIdCompetencia() { return idCompetencia; }
    public void setIdCompetencia(int idCompetencia) { this.idCompetencia = idCompetencia; }
    public String getCompetencia() { return competencia; }
    public void setCompetencia(String competencia) { this.competencia = competencia; }
    public int getIdFactor() { return idFactor; }
    public void setIdFactor(int idFactor) { this.idFactor = idFactor; }
    public String getFactor() { return factor; }
    public void setFactor(String factor) { this.factor = factor; }
    public int getIdOpcionRespuesta() { return idOpcionRespuesta; }
    public void setIdOpcionRespuesta(int idOpcionRespuesta) { this.idOpcionRespuesta = idOpcionRespuesta; }
    public Map<Integer, String> getPonderaciones() { return ponderaciones; }
    public void setPonderaciones(Map<Integer, String> ponderaciones) { this.ponderaciones = ponderaciones; }
}
