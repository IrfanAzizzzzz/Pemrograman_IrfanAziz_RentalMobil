package rental;

public class Mobil extends Kendaraan {
    private String namaMobil;

    public Mobil(String namaMobil, String platNomor, String status) {
        super(platNomor, status);
        this.namaMobil = namaMobil;
    }

    public String getNamaMobil() {
        return namaMobil;
    }
}