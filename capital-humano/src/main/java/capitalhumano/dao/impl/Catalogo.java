package capitalhumano.dao.impl;

import capitalhumano.entidades.Competencia;
import capitalhumano.entidades.Factor;
import capitalhumano.entidades.ItemOpcionDeRespuesta;
import capitalhumano.entidades.OpcionDeRespuesta;
import capitalhumano.entidades.PonderacionItem;
import capitalhumano.entidades.Pregunta;
import capitalhumano.entidades.TipoCompetencia;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Carga el modelo de evaluación (competencias -> factores -> preguntas -> opciones/ítems/ponderaciones)
 * armando el grafo de objetos con una única instancia por fila. Lo usan las implementaciones de los
 * DAO que necesitan devolver entidades navegables (Puesto, Cuestionario, Evaluacion).
 * No es un DAO del diseño: es un detalle interno de la capa de datos.
 */
final class Catalogo {
    final Map<Integer, Competencia> competencias = new LinkedHashMap<>();
    final Map<Integer, Factor> factores = new LinkedHashMap<>();
    final Map<Integer, Pregunta> preguntas = new LinkedHashMap<>();
    final Map<Integer, OpcionDeRespuesta> opciones = new LinkedHashMap<>();
    final Map<Integer, ItemOpcionDeRespuesta> items = new LinkedHashMap<>();

    static Catalogo cargar(Connection cn) throws SQLException {
        Catalogo c = new Catalogo();
        try (Statement st = cn.createStatement()) {
            try (ResultSet rs = st.executeQuery(
                    "SELECT id_opcion, nombre, descripcion, activo FROM opcion_de_respuesta ORDER BY nombre")) {
                while (rs.next()) {
                    OpcionDeRespuesta o = new OpcionDeRespuesta();
                    o.setId(rs.getInt(1));
                    o.setNombre(rs.getString(2));
                    o.setDescripcion(rs.getString(3));
                    o.setActivo(rs.getBoolean(4));
                    c.opciones.put(o.getId(), o);
                }
            }
            try (ResultSet rs = st.executeQuery(
                    "SELECT id_item, id_opcion, orden, descripcion, activo FROM item_opcion_de_respuesta ORDER BY id_opcion, orden")) {
                while (rs.next()) {
                    ItemOpcionDeRespuesta i = new ItemOpcionDeRespuesta();
                    i.setId(rs.getInt(1));
                    i.setOrden(rs.getInt(3));
                    i.setDescripcion(rs.getString(4));
                    i.setActivo(rs.getBoolean(5));
                    c.items.put(i.getId(), i);
                    c.opciones.get(rs.getInt(2)).getItems().add(i);
                }
            }
            try (ResultSet rs = st.executeQuery(
                    "SELECT id_competencia, codigo, nombre, descripcion, tipo, activo FROM competencia ORDER BY nombre")) {
                while (rs.next()) {
                    Competencia co = new Competencia();
                    co.setId(rs.getInt(1));
                    co.setCodigo(rs.getString(2));
                    co.setNombre(rs.getString(3));
                    co.setDescripcion(rs.getString(4));
                    co.setTipo(TipoCompetencia.valueOf(rs.getString(5)));
                    co.setActivo(rs.getBoolean(6));
                    c.competencias.put(co.getId(), co);
                }
            }
            try (ResultSet rs = st.executeQuery(
                    "SELECT id_factor, id_competencia, codigo, nombre, descripcion, nro_orden, activo, fecha_baja "
                            + "FROM factor ORDER BY id_competencia, nro_orden")) {
                while (rs.next()) {
                    Factor f = new Factor();
                    f.setId(rs.getInt(1));
                    f.setCodigo(rs.getString(3));
                    f.setNombre(rs.getString(4));
                    f.setDescripcion(rs.getString(5));
                    f.setNroOrden(rs.getInt(6));
                    f.setActivo(rs.getBoolean(7));
                    if (rs.getDate(8) != null) f.setFechaBaja(rs.getDate(8).toLocalDate());
                    Competencia co = c.competencias.get(rs.getInt(2));
                    f.setCompetencia(co);
                    c.factores.put(f.getId(), f);
                    if (f.isActivo()) co.getFactores().add(f);
                }
            }
            try (ResultSet rs = st.executeQuery(
                    "SELECT id_pregunta, id_factor, id_opcion, nombre, enunciado, descripcion, activo, fecha_baja "
                            + "FROM pregunta ORDER BY id_pregunta")) {
                while (rs.next()) {
                    Pregunta p = new Pregunta();
                    p.setId(rs.getInt(1));
                    p.setNombre(rs.getString(4));
                    p.setEnunciado(rs.getString(5));
                    p.setDescripcion(rs.getString(6));
                    p.setActivo(rs.getBoolean(7));
                    if (rs.getDate(8) != null) p.setFechaBaja(rs.getDate(8).toLocalDate());
                    Factor f = c.factores.get(rs.getInt(2));
                    p.setFactor(f);
                    p.setOpcionDeRespuesta(c.opciones.get(rs.getInt(3)));
                    c.preguntas.put(p.getId(), p);
                    f.getPreguntas().add(p);
                }
            }
            try (ResultSet rs = st.executeQuery(
                    "SELECT id_ponderacion, id_pregunta, id_item, valor FROM ponderacion_item")) {
                while (rs.next()) {
                    PonderacionItem pi = new PonderacionItem(rs.getDouble(4), c.items.get(rs.getInt(3)));
                    pi.setId(rs.getInt(1));
                    c.preguntas.get(rs.getInt(2)).agregarPonderacion(pi);
                }
            }
        }
        return c;
    }
}
