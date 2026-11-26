package capitalhumano.entidades;

public class CaracteristicaPuesto {
    private int id;
    private double ponderacionMinima;
    private Competencia competencia;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getPonderacionMinima() { return ponderacionMinima; }
    public void setPonderacionMinima(double ponderacionMinima) { this.ponderacionMinima = ponderacionMinima; }
    public Competencia getCompetencia() { return competencia; }
    public void setCompetencia(Competencia competencia) { this.competencia = competencia; }
}
