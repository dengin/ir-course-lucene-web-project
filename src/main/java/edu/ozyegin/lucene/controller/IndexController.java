package edu.ozyegin.lucene.controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

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
    public String indeksDizini;
    public long baslik = 1L;
    public long anahtarlar = 1L;
    public long ozet = 1L;
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
            if (this.makaleDizini == null || this.makaleDizini.trim().equals(""))
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Makale dizini seçilmeli", null));
            }
            if (this.indeksDizini == null || this.indeksDizini.trim().equals(""))
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "İndeks dizini seçilmeli", null));
            }

            islemBasladi = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "İndeksleme işlemi başladı", null));
        }
        catch (Exception exception)
        {
//            RequestContext.getCurrentInstance().addCallbackParam("showDialog", false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "İndeksleme işleminde hata oluştu", exception.getMessage()));
        }
    }

    public String getMakaleDizini()
    {
        return makaleDizini;
    }

    public void setMakaleDizini(String makaleDizini)
    {
        this.makaleDizini = makaleDizini;
    }

    public String getIndeksDizini()
    {
        return indeksDizini;
    }

    public void setIndeksDizini(String indeksDizini)
    {
        this.indeksDizini = indeksDizini;
    }

    public long getBaslik()
    {
        return baslik;
    }

    public void setBaslik(long baslik)
    {
        this.baslik = baslik;
    }

    public long getAnahtarlar()
    {
        return anahtarlar;
    }

    public void setAnahtarlar(long anahtarlar)
    {
        this.anahtarlar = anahtarlar;
    }

    public long getOzet()
    {
        return ozet;
    }

    public void setOzet(long ozet)
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
