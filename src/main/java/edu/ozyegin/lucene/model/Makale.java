package edu.ozyegin.lucene.model;

import java.io.Serializable;

/**
 * User: TTEDEMIRCIOGLU
 * Date: 15.01.2017
 * Time: 05:46
 */
public class Makale implements Serializable
{
    private String makaleIsmi;
    private String baslik;
    private String yil;
    private String anahtarlar;
    private String yazarlar;
    private String doi;
    private String ozet;

    public String getMakaleIsmi()
    {
        return makaleIsmi;
    }

    public void setMakaleIsmi(String makaleIsmi)
    {
        this.makaleIsmi = makaleIsmi;
    }

    public String getBaslik()
    {
        return baslik;
    }

    public void setBaslik(String baslik)
    {
        this.baslik = baslik;
    }

    public String getYil()
    {
        return yil;
    }

    public void setYil(String yil)
    {
        this.yil = yil;
    }

    public String getAnahtarlar()
    {
        return anahtarlar;
    }

    public void setAnahtarlar(String anahtarlar)
    {
        this.anahtarlar = anahtarlar;
    }

    public String getYazarlar()
    {
        return yazarlar;
    }

    public void setYazarlar(String yazarlar)
    {
        this.yazarlar = yazarlar;
    }

    public String getDoi()
    {
        return doi;
    }

    public void setDoi(String doi)
    {
        this.doi = doi;
    }

    public String getOzet()
    {
        return ozet;
    }

    public void setOzet(String ozet)
    {
        this.ozet = ozet;
    }
}
