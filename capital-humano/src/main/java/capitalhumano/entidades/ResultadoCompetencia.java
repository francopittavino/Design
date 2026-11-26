package capitalhumano.entidades;

import java.util.ArrayList;
import java.util.List;

public class ResultadoCompetencia {
    private int id;
    private double puntajeObtenido;
    private Competencia competencia;
    private List<ResultadoFactor> resultadosFactor = new ArrayList<>();

    public ResultadoCompetencia() {}
    public ResultadoCompetencia(Competencia competencia) { this.competencia = competencia; }

    public boolean superaPonderacionMinima(double min) { return puntajeObtenido >= min; }

    public void agregarResultadoFactor(ResultadoFactor rf) { resultadosFactor.add(rf); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getPuntajeObtenido() { return puntajeObtenido; }
    public void setPuntajeObtenido(double puntajeObtenido) { this.puntajeObtenido = puntajeObtenido; }
    public Competencia getCompetencia() { return competencia; }
    public void setCompetencia(Competencia competencia) { this.competencia = competencia; }
    public List<ResultadoFactor> getResultadosFactor() { return resultadosFactor; }
}
