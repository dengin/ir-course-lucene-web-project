package edu.ozyegin.lucene.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: TTEDEMIRCIOGLU
 * Date: 15.01.2017
 * Time: 19:10
 */
public class Yazar implements Serializable
{
    private String id;
    private String tamAdi;
    private int bulunanMakaleSayisi;
    private float toplamSkoru;
    private List<Makale> makaleListesi;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTamAdi()
    {
        return tamAdi;
    }

    public void setTamAdi(String tamAdi)
    {
        this.tamAdi = tamAdi;
    }

    public int getBulunanMakaleSayisi()
    {
        return bulunanMakaleSayisi;
    }

    public void setBulunanMakaleSayisi(int bulunanMakaleSayisi)
    {
        this.bulunanMakaleSayisi = bulunanMakaleSayisi;
    }

    public float getToplamSkoru()
    {
        return toplamSkoru;
    }

    public void setToplamSkoru(float toplamSkoru)
    {
        this.toplamSkoru = toplamSkoru;
    }

    public List<Makale> getMakaleListesi()
    {
        return makaleListesi;
    }

    public void setMakaleListesi(List<Makale> makaleListesi)
    {
        this.makaleListesi = makaleListesi;
    }

    public static void sortByToplamSkor(List<Yazar> yazarListesi)
    {
        Collections.sort(yazarListesi, new Comparator()
        {
            @Override
            public int compare(Object o1, Object o2)
            {
                Float f1 = ((Yazar) o1).getToplamSkoru();
                Float f2 = ((Yazar) o2).getToplamSkoru();
                return f2.compareTo(f1);
            }
        });
    }
}
