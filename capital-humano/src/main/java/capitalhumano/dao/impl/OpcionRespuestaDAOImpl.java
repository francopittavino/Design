package capitalhumano.dao.impl;

import capitalhumano.dao.DatosException;
import capitalhumano.dao.OpcionRespuestaDAO;
import capitalhumano.entidades.OpcionDeRespuesta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OpcionRespuestaDAOImpl implements OpcionRespuestaDAO {

    @Override
    public OpcionDeRespuesta buscarPorId(int id) {
        return catalogo().opciones.get(id);
    }

    @Override
    public List<OpcionDeRespuesta> listarTodas() {
        return catalogo().opciones.values().stream().filter(OpcionDeRespuesta::isActivo).toList();
    }

    @Override
    public List<OpcionDeRespuesta> buscarPorNombre(String nombre) {
        String n = nombre == null ? "" : nombre.trim().toUpperCase();
        return listarTodas().stream().filter(o -> o.getNombre().toUpperCase().contains(n)).toList();
    }

    private Catalogo catalogo() {
        try (Connection cn = ConexionBD.obtener()) {
            return Catalogo.cargar(cn);
        } catch (SQLException e) {
            throw new DatosException("Error al leer las opciones de respuesta", e);
        }
    }

    @Override
    public void guardar(OpcionDeRespuesta opcion) {
        throw new UnsupportedOperationException("CU029 - Dar de alta opción de respuesta: fuera del alcance de las Etapas 7 y 8");
    }

    @Override
    public void actualizar(OpcionDeRespuesta opcion) {
        throw new UnsupportedOperationException("CU030 - Modificar opción de respuesta: fuera del alcance de las Etapas 7 y 8");
    }

    @Override
    public void eliminarLogico(int id) {
        throw new UnsupportedOperationException("CU031 - Dar de baja opción de respuesta: fuera del alcance de las Etapas 7 y 8");
    }
}
