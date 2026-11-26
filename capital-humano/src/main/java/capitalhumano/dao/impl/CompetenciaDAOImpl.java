package capitalhumano.dao.impl;

import capitalhumano.dao.CompetenciaDAO;
import capitalhumano.dao.DatosException;
import capitalhumano.entidades.Competencia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CompetenciaDAOImpl implements CompetenciaDAO {

    @Override
    public Competencia buscarPorId(int id) {
        return catalogo().competencias.get(id);
    }

    @Override
    public List<Competencia> listarTodas() {
        return catalogo().competencias.values().stream().filter(Competencia::isActivo).toList();
    }

    @Override
    public List<Competencia> buscarPorNombre(String nombre) {
        String n = nombre == null ? "" : nombre.trim().toUpperCase();
        return listarTodas().stream().filter(c -> c.getNombre().toUpperCase().contains(n)).toList();
    }

    private Catalogo catalogo() {
        try (Connection cn = ConexionBD.obtener()) {
            return Catalogo.cargar(cn);
        } catch (SQLException e) {
            throw new DatosException("Error al leer las competencias", e);
        }
    }

    @Override
    public void guardar(Competencia comp) {
        throw new UnsupportedOperationException("CU013 - Dar de alta competencia: fuera del alcance de las Etapas 7 y 8");
    }

    @Override
    public void actualizar(Competencia comp) {
        throw new UnsupportedOperationException("CU014 - Modificar competencia: fuera del alcance de las Etapas 7 y 8");
    }

    @Override
    public void eliminarLogico(int id) {
        throw new UnsupportedOperationException("CU015 - Dar de baja competencia: fuera del alcance de las Etapas 7 y 8");
    }
}
