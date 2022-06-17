package Exercitiu;

public class Apartament {
    private int numar_persoane;
    private String nume;
    private int suprafara;
    private int nr_apartamente;

    public Apartament(int numar_persoane, String nume, int suprafara, int nr_apartamente) {
        this.numar_persoane = numar_persoane;
        this.nume = nume;
        this.suprafara = suprafara;
        this.nr_apartamente = nr_apartamente;
    }

    public Apartament() {
        this.numar_persoane = 0;
        this.nume = "Unknown";
        this.suprafara = 0;
        this.nr_apartamente = 0;
    }

    public int getNumar_persoane() {
        return numar_persoane;
    }

    public void setNumar_persoane(int numar_persoane) {
        this.numar_persoane = numar_persoane;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getSuprafara() {
        return suprafara;
    }

    public void setSuprafara(int suprafara) {
        this.suprafara = suprafara;
    }

    public int getNr_apartamente() {
        return nr_apartamente;
    }

    public void setNr_apartamente(int nr_apartamente)
    {
        this.nr_apartamente = nr_apartamente;
    }

    @Override
    public String toString() {
        return "Apartament{" +
                "numar_persoane=" + numar_persoane +
                ", nume='" + nume + '\'' +
                ", suprafara=" + suprafara +
                ", nr_apartamente=" + nr_apartamente +
                '}';
    }
}
