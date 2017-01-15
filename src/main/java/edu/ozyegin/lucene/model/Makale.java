package edu.ozyegin.lucene.model;

import java.io.Serializable;

/**
 * User: TTEDEMIRCIOGLU
 * Date: 15.01.2017
 * Time: 05:46
 */
public class Makale implements Serializable
{
    private String adres;
    private String baslik;
    private String yil;
    private String anahtarlar;
    private String yazarId;
    private String yazarTamAdi;
    private String doi;
    private String ozet;

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

    public String getYazarId()
    {
        return yazarId;
    }

    public void setYazarId(String yazarId)
    {
        this.yazarId = yazarId;
    }

    public String getYazarTamAdi()
    {
        return yazarTamAdi;
    }

    public void setYazarTamAdi(String yazarTamAdi)
    {
        this.yazarTamAdi = yazarTamAdi;
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

    public String getAdres()
    {
        return adres;
    }

    public void setAdres(String adres)
    {
        this.adres = adres;
    }

    public String getYazarBilgisi()
    {
        String bilgi = "";
        if (yazarTamAdi != null && !yazarTamAdi.trim().equals(""))
        {
            bilgi += yazarTamAdi;
        }
        if (yazarId != null && !yazarId.trim().equals(""))
        {
            if (bilgi.length() > 0)
            {
                bilgi += "\n";
            }
            bilgi += yazarId;
        }
        return bilgi;
    }
}
