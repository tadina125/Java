package Exercitiu;
/*
Fie datele de intrare (in directorul date):
a)	Intretinere facturi.txt: Iista de facturi (denumire string, repartizare string, valoare double) fisler text de forma:
Gaze naturale,persoane,140
Apă caldă,persoane,500
Căldură,suprafață,300
Gunoi,apartament,100
Câmpul repartizare poate avea următoarele valori: suprafața sau persoane. Denumirea facturii nu poate conține caracterul virgulă.
b)	tabela Apartamente din baza de date SQLite intretinere_apartamente.db cu următoarele câmpuri:
NumarApartament – integer
Nume – string
Suprafața – Integer
NumărPersoane - Integer
Să se scrie o aplicație Java care sa Indeplinească următoarele cerinte:
1)	Să se afișeze în consolă numărul de facturi repartizate pe suprafață si numărul de facturi repartizate pe număr de persoane.
Punctaj: 1 punct.
Criteriu de acordare a punctajulul: afișarea corectă la consolă.
Exemplu:
CERINTA 1: 7 facturi per suprafață/4 facturi per persoană
2)	Să se afișeze la consolă valoarea totală a facturilor pe fiecare tip de repartizare.
Punctaj: 1 punct.
Criteriu de acordare a punctajulul: afișarea corectă la consolă.
Exemplu:
CERINTA 2: Valoarea totală a facturilor de tip de repartizare: P - 940.00, S - 200.00
3)	Să se afișeze la consolă suprafața totală a apartamentelor din bloc.
Punctaj: 2 puncte.
Criteriu de acordare a punctajulul: afișarea corectă la consolă.
Exemplu:
CERINTA 3:  Suprafața totală a apartamentelor: 250
4)	Să se afișeze la consolăm tabelul de întreținere în forma:
Număr apartament, Nume, Suprafață, Persoane, Total de plată
Punctaj: 2 puncte.
Criteriu de acordare a punctajulul: afișarea corectă la consolă.
Exemplu:
CERINTA 4:
Număr apartament, Nume, Suprafață, Persoane, Total de plată
1		CĂTĂLIN ROSENTHAL	100	1	161.21
2		DUMITRU PETROȘANU	50	1	127.88
3		IONUȚ RUSSI		75	2	239.03
4		ANCA GEFRA		50	2	222.42
5		PERSHALL MONICA		100	3	350.30
6		VASILICA DIGITAL	75	2	239.03
5) Să implementeze funcționalitățile de server și cilent TCP/IP și să se execute următorul scenariu:
componenta client trimite serverului un număr de apartament, iar componenta server va întoarce clientului numele proprietarului. Componenta server poate servi oricâte cereri.
Punctaj:
1 punct - afisarea la consolă de către server a numărului de apartament primit de la client
1 punct - implementare server multithreaded (flecare cerere este deservită de un thread independent)
1 punct - afisarea la consolă de către client a răspunsului corespunzător
Criteriu de acordare a punctajulul: afișarea corectă la consolă.
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class MainFacturi
{
    public static List<Apartament> apartamente = new ArrayList<>();
    public static List<Apartament> apartamenteDB = new ArrayList<>();

    public static List<Factura> citireText(String caleFisier)
    {
        List<Factura> rezultat = new ArrayList<>();

        try(var fisier = new BufferedReader(new FileReader(caleFisier)))
        {
            rezultat = fisier.lines()
                    .map(linie->new Factura(
                    linie.split(",")[0],
                            linie.split(",")[1],
                            Double.parseDouble(linie.split(",")[2])
                    )).collect(Collectors.toList());

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return rezultat;
    }
    private static final String URLdb = "jdbc:sqlite:Tools\\intretinere_apartamente.db";
    private static void creareTabela ()
    {
        try(Connection conexiune = DriverManager.getConnection(URLdb))
        {
            Statement comanda = conexiune.createStatement();
            comanda.executeUpdate("CREATE TABLE IF NOT EXISTS Apartamente (nr_apartamente integer, nume text, suprafata integer, numar_persoane integer)");
            comanda.executeUpdate("DELETE FROM Apartamente");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void inserareTabela()
    {
        try(Connection conexiune = DriverManager.getConnection(URLdb))
        {
            PreparedStatement comanda = conexiune.prepareStatement("INSERT INTO Apartamente values(?,?,?,?)");
            for (var apartament : apartamente)
            {
                comanda.setInt(1, apartament.getNr_apartamente());
                comanda.setString(2, apartament.getNume());
                comanda.setInt(3, apartament.getSuprafara());
                comanda.setInt(4, apartament.getNumar_persoane());
                comanda.execute();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void suprafataTotala() throws SQLException
    {
        //List<Apartament> apartamenteDB = new ArrayList<>();
        try(Connection conexiune = DriverManager.getConnection(URLdb))
        {
            Statement comanda = conexiune.createStatement();
            ResultSet rs = comanda.executeQuery("SELECT SUM(suprafata)total FROM Apartamente");
            //rs.next();
            while (rs.next())
            {
                int total = rs.getInt("total");
                System.out.println("Suprafata totala a apartamentelor este de: "+ total +" mp.");
            }




        }
    }

    public static void citireDb() throws SQLException
    {
        //List<Apartament> apartamenteDB = new ArrayList<>();
        try(Connection conexiune = DriverManager.getConnection(URLdb))
        {
            Statement comanda = conexiune.createStatement();
            ResultSet rs = comanda.executeQuery("SELECT * FROM Apartamente");
            while(rs.next())
            {
                int nr_apartamente = rs.getInt("nr_apartamente");
                String nume = rs.getString("nume");
                int suprafata = rs.getInt("suprafata");
                int nr_persoane = rs.getInt("numar_persoane");
                Apartament apartament = new Apartament(nr_persoane, nume, suprafata, nr_apartamente);

                apartamenteDB.add(apartament);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException, SQLException
    {
        List<Factura> facturi = new ArrayList<>();
        List<Factura> removeFacturi = new ArrayList<>();


        facturi = citireText("Date/intretinere_facturi.txt");
        facturi.stream()
                .forEach(factura -> System.out.println(factura.toString()));


        System.out.println("Cerinta 1---------------------");

        Map<String, List<Factura>> repartizareFactura = facturi.stream().collect(Collectors.groupingBy(Factura::getRepartizare));
        System.out.println(repartizareFactura.get("persoane").size() + " facturi per persoane.");
        System.out.println(repartizareFactura.get("suprafață").size() + " facturi per suprafata.");
        System.out.println(repartizareFactura.get("apartament").size() + " facturi per apartamente.");

        System.out.println("Cerinta 2---------------------");

        //VARIANTA CU STREAM-URI
        /*double valP = (facturi.stream()
                .collect(Collectors.groupingBy(Factura::getRepartizare)))
                .values().stream().flatMap(List::stream).filter(Factura::estePersoana).mapToDouble(Factura::getValoare).sum();
        double valS = (facturi.stream()
                .collect(Collectors.groupingBy(Factura::getRepartizare)))
                .values().stream().flatMap(List::stream).filter(Factura::esteSuprafata).mapToDouble(Factura::getValoare).sum();
        double valA = (facturi.stream()
                .collect(Collectors.groupingBy(Factura::getRepartizare)))
                .values().stream().flatMap(List::stream).filter(Factura::esteApartament).mapToDouble(Factura::getValoare).sum();
        System.out.println("Valoarea totala a facturilor de tip repartizare persoana este de " +valP + " lei");
        System.out.println("Valoarea totala a facturilor de tip repartizare suprafață este de " +valS + " lei");
        System.out.println("Valoarea totala a facturilor de tip repartizare apartament este de " +valA + " lei");*/

        for (var entry : repartizareFactura.entrySet()) {
            double valoare = 0.0;
            String repartizare = "";
            for (var factura : entry.getValue()) {
                valoare += factura.getValoare();
                repartizare = factura.getRepartizare();
            }
            System.out.println("Valoarea totala pentru repartizarea de tip " + repartizare + " este de " + valoare + " lei");
        }


        creareTabela();
        apartamente.add(new Apartament(100, "Vasilica Hardisk", 150, 3));
        apartamente.add(new Apartament(30, "Floricica Software", 100, 6));
        inserareTabela();
        citireDb();
        apartamenteDB.stream().forEach(System.out::println);
        System.out.println("Cerinta 3---------------------");
        suprafataTotala();

        System.out.println("Cerinta 4---------------------");
//        double[] valori = new double[facturi.size()];
//        for (var factura1 : facturi)
//        {
//
//            for (int i = 0; i < facturi.size(); i++)
//            {
//                valori[i] = factura1.getValoare();
//
//            }
//
//        }

        double[] valori = new double[facturi.size()];
        int i =0;
        for (var factura1 : facturi)
        {
            valori[i] = factura1.getValoare();
            System.out.println(valori[i]);
            i++;
        }


        System.out.printf("%15s %15s %15s %15s %15s\n", "Numar apartament", "Nume", "Suprafata", "Numar persoane", "Total de plata");
        for (var apartament : apartamenteDB)
        {

            float totalPlata = 0.0f;
            totalPlata = (float) (valori[0] / apartament.getNumar_persoane() + valori[1] / apartament.getNumar_persoane() + valori[2]/ apartament.getSuprafara() + valori[3]/ 1);

            System.out.printf("%5s %25s %15s %15s %15.2f\n", apartament.getNr_apartamente(), apartament.getNume(), apartament.getSuprafara(), apartament.getNumar_persoane(), totalPlata);

        }





    }

}


