package capitalhumano.dao.impl;

import capitalhumano.dao.DatosException;
import capitalhumano.dao.FactorDAO;
import capitalhumano.entidades.Factor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class FactorDAOImpl implements FactorDAO {

    @Override
    public Factor buscarPorId(int id) {
        return catalogo().factores.get(id);
    }

    @Override
    public List<Factor> buscarPorCompetencia(int idComp) {
        return listarTodos().stream().filter(f -> f.getCompetencia().getId() == idComp).toList();
    }

    @Override
    public List<Factor> listarTodos() {
        return catalogo().factores.values().stream()
                .filter(Factor::isActivo)
                .sorted(Comparator.comparing((Factor f) -> f.getCompetencia().getNombre()).thenComparing(Factor::getNroOrden))
                .toList();
    }

    private Catalogo catalogo() {
        try (Connection cn = ConexionBD.obtener()) {
            return Catalogo.cargar(cn);
        } catch (SQLException e) {
            throw new DatosException("Error al leer los factores", e);
        }
    }

    @Override
    public void guardar(Factor factor) {
        throw new UnsupportedOperationException("CU017 - Dar de alta factor: fuera del alcance de las Etapas 7 y 8");
    }

    @Override
    public void actualizar(Factor factor) {
        throw new UnsupportedOperationException("CU018 - Modificar factor: fuera del alcance de las Etapas 7 y 8");
    }

    @Override
    public void eliminarLogico(int id) {
        throw new UnsupportedOperationException("CU019 - Dar de baja factor: fuera del alcance de las Etapas 7 y 8");
    }
}
