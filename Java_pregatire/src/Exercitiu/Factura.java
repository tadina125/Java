package Exercitiu;

public class Factura
{
    private String denumire;
    private String repartizare;
    private double valoare;

    public Factura(String denumire, String repartizare, double valoare) {
        this.denumire = denumire;
        this.repartizare = repartizare;
        this.valoare = valoare;
    }
    public Factura() {
        this.denumire = "Unknown";
        this.repartizare = "Unknown";
        this.valoare = 0;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getRepartizare() {
        return repartizare;
    }

    public void setRepartizare(String repartizare) {
        this.repartizare = repartizare;
    }

    public double getValoare() {
        return valoare;
    }

    public void setValoare(double valoare) {
        this.valoare = valoare;
    }

    @Override
    public String toString() {
        return "Facturi{" +
                "denumire='" + denumire + '\'' +
                ", repartizare='" + repartizare + '\'' +
                ", valoare=" + valoare +
                '}';
    }

    public boolean estePersoana()

    {
        return  repartizare.equals("persoane");
    }
    public boolean esteSuprafata()
    {
        return repartizare.equals("suprafață");
    }
    public boolean esteApartament()
    {
        return repartizare.equals("apartament");
    }
}
