package edu.ozyegin.lucene.controller;

import edu.ozyegin.lucene.model.MyIndexer;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * User: TTEDEMIRCIOGLU
 * Date: 15.01.2017
 * Time: 02:05
 */
@ManagedBean(name = "indexController")
@ViewScoped
public class IndexController implements Serializable
{
    public String makaleDizini;
    public String indeksAnaDizini;
    public float baslik = 1L;
    public float anahtarlar = 1L;
    public float ozet = 1L;
    public boolean islemBasladi;
    public String analyser;

    @PostConstruct
    public void init()
    {
    }

    public void indeksle()
    {
        try
        {
            indeksIsleminiBaslat();
            islemBasladi = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "İndeksleme işlemi tamamlandı", null));
        }
        catch (Exception exception)
        {
//            RequestContext.getCurrentInstance().addCallbackParam("showDialog", false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ("İndeksleme işleminde hata oluştu: " + exception.getMessage()), exception.getMessage()));
        }
    }

    private void indeksIsleminiBaslat() throws Exception
    {
        if (this.makaleDizini == null || this.makaleDizini.trim().equals(""))
        {
            throw new Exception("Makale dizini seçilmeli");
        }
        if (this.indeksAnaDizini == null || this.indeksAnaDizini.trim().equals(""))
        {
            throw new Exception("İndeks ana dizini seçilmeli");
        }
        Path makaleKlasoru = Paths.get(this.getMakaleDizini(), new String[0]);
        if (!Files.isReadable(makaleKlasoru))
        {
            throw new Exception("Makale dizini (" + makaleKlasoru.toAbsolutePath() + ") yok ya da okunabilir değil, lütfen kontrol ediniz");
        }
        MyIndexer myIndexer = new MyIndexer(this.indeksAnaDizini, this.makaleDizini, this.baslik, this.anahtarlar, this.ozet, this.analyser);
        myIndexer.indexData();
    }

    public String getMakaleDizini()
    {
        return makaleDizini;
    }

    public void setMakaleDizini(String makaleDizini)
    {
        this.makaleDizini = makaleDizini;
    }

    public String getIndeksAnaDizini()
    {
        return indeksAnaDizini;
    }

    public void setIndeksAnaDizini(String indeksAnaDizini)
    {
        this.indeksAnaDizini = indeksAnaDizini;
    }

    public float getBaslik()
    {
        return baslik;
    }

    public void setBaslik(float baslik)
    {
        this.baslik = baslik;
    }

    public float getAnahtarlar()
    {
        return anahtarlar;
    }

    public void setAnahtarlar(float anahtarlar)
    {
        this.anahtarlar = anahtarlar;
    }

    public float getOzet()
    {
        return ozet;
    }

    public void setOzet(float ozet)
    {
        this.ozet = ozet;
    }

    public boolean getIslemBasladi()
    {
        return islemBasladi;
    }

    public void setIslemBasladi(boolean islemBasladi)
    {
        this.islemBasladi = islemBasladi;
    }

    public String getAnalyser()
    {
        return analyser;
    }

    public void setAnalyser(String analyser)
    {
        this.analyser = analyser;
    }
}
