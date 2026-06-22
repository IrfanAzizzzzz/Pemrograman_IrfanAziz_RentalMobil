/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rental;

/**
 *
 * @author irfan aziz
 */
public class Kendaraan {
    protected String platNomor;
    protected String status;

    public Kendaraan(String platNomor, String status) {
        this.platNomor = platNomor;
        this.status = status;
    }

    public String getPlatNomor() {
        return platNomor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
