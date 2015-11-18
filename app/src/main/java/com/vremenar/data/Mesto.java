package com.vremenar.data;

/**
 * Created by Mitja on 17. 11. 2015.
 */
public class Mesto {

    private String naziv;
    private String temparatura;
    private String vlaga;
    private String opis;

    public Mesto(){}

    public Mesto(String naziv, String temparatura, String vlaga, String opis) {
        this.naziv = naziv;
        this.temparatura = temparatura;
        this.vlaga = vlaga;
        this.opis = opis;
    }

    public String getTemparatura() {
        return temparatura;
    }

    public void setTemparatura(String temparatura) {
        this.temparatura = temparatura;
    }

    public String getVlaga() {
        return vlaga;
    }

    public void setVlaga(String vlaga) {
        this.vlaga = vlaga;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }


}
