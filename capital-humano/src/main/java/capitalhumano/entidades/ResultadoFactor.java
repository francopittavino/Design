package capitalhumano.entidades;

public class ResultadoFactor {
    private int id;
    private double puntajeObtenido;
    private Factor factor;

    public ResultadoFactor() {}
    public ResultadoFactor(Factor factor, double puntajeObtenido) {
        this.factor = factor;
        this.puntajeObtenido = puntajeObtenido;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getPuntajeObtenido() { return puntajeObtenido; }
    public void setPuntajeObtenido(double puntajeObtenido) { this.puntajeObtenido = puntajeObtenido; }
    public Factor getFactor() { return factor; }
    public void setFactor(Factor factor) { this.factor = factor; }
}
