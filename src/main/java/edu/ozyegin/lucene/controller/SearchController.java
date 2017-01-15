package edu.ozyegin.lucene.controller;

import edu.ozyegin.lucene.model.Makale;
import edu.ozyegin.lucene.model.MySearcher;
import edu.ozyegin.lucene.model.Yazar;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: TTEDEMIRCIOGLU
 * Date: 15.01.2017
 * Time: 05:29
 */
@ManagedBean(name = "searchController")
@ViewScoped
public class SearchController implements Serializable
{
    private static final int DEFAULT_BUFFER_SIZE = 10240;
    private String indeksAnaDizini;
    private List<String> indeksDizinleri;
    private String secilenIndeksDizini;
    private String baslik;
    private String anahtarlar;
    private String ozet;
    private List<Makale> makaleListesi;
    private List<Yazar> yazarListesi;
    private String analyser;

    public void ara(String alan)
    {
        try
        {
            aramaIsleminiBaslat(alan);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Arama işlemi tamamlandı", null));
        }
        catch (Exception exception)
        {
//            RequestContext.getCurrentInstance().addCallbackParam("showDialog", false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ("Arama işleminde hata oluştu: " + exception.getMessage()), exception.getMessage()));
        }
    }

    private void aramaIsleminiBaslat(String alan) throws Exception
    {
        if (this.indeksAnaDizini == null || this.indeksAnaDizini.trim().equals(""))
        {
            throw new Exception("İndeks ana dizini seçilmeli");
        }
        if (this.secilenIndeksDizini == null || this.secilenIndeksDizini.trim().equals(""))
        {
            throw new Exception("İndeks dizini seçilmeli");
        }
        if (this.analyser == null || this.analyser.trim().equals(""))
        {
            throw new Exception("Analyser seçilmeli");
        }
        if (alan == null || alan.trim().equals(""))
        {
            throw new Exception("Alan seçimi yapılmalı");
        }
        if (!alan.equals("baslik") && !alan.equals("anahtarlar") && !alan.equals("ozet"))
        {
            throw new Exception("Alan değeri 'baslik', 'anahtarlar' ya da 'ozet' olmalıdır");
        }
        String sorgu = null;
        if (alan.equals("baslik"))
        {
            if (baslik == null || baslik.trim().equals(""))
            {
                throw new Exception("Başlık alanı dolu olmalıdır");
            }
            sorgu = baslik;
        }
        if (alan.equals("anahtarlar"))
        {
            if (anahtarlar == null || anahtarlar.trim().equals(""))
            {
                throw new Exception("Anahtar kelimeler alanı dolu olmalıdır");
            }
            sorgu = anahtarlar;
        }
        if (alan.equals("ozet"))
        {
            if (ozet == null || ozet.trim().equals(""))
            {
                throw new Exception("Özet alanı dolu olmalıdır");
            }
            sorgu = ozet;
        }

        MySearcher mySearcher = new MySearcher((this.indeksAnaDizini + "/" + secilenIndeksDizini), alan, sorgu, analyser);
        this.makaleListesi = mySearcher.search();
    }

    public void dizinleriGetir()
    {
        try
        {
            if (this.indeksAnaDizini == null || this.indeksAnaDizini.trim().equals(""))
            {
                throw new Exception("İndeks ana dizini boş olamaz");
            }
            File dir = new File(indeksAnaDizini);
            if (!dir.isDirectory())
            {
                throw new Exception(indeksAnaDizini + " dizin değildir");
            }
            String[] indeksDirectories = dir.list();
            if (indeksDirectories == null || indeksDirectories.length == 0)
            {
                throw new Exception(indeksAnaDizini + " boş");
            }
            this.indeksDizinleri = new ArrayList();
            for (int i = 0; i < indeksDirectories.length; i++)
            {
                this.indeksDizinleri.add(indeksDirectories[i]);
            }
        }
        catch (Exception exception)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ("Dizin getirme işleminde hata oluştu: " + exception.getMessage()), exception.getMessage()));
        }
    }

    public void makaleyiAc(String adres)
    {
        try
        {
//            InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(adres);
//            StreamedContent file = new DefaultStreamedContent(stream, "application/txt", "makale");

            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            File file = new File(adres);
            if (!file.exists())
            {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            response.reset();
            response.setBufferSize(DEFAULT_BUFFER_SIZE);
            response.setContentType("application/txt");
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "attachment;filename=\"" + file.getName() + "\"");
            BufferedInputStream input = null;
            BufferedOutputStream output = null;
            try
            {
                input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
                output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                int length;
                while ((length = input.read(buffer)) > 0)
                {
                    output.write(buffer, 0, length);
                }
            }
            finally
            {
                input.close();
                output.close();
            }
            facesContext.responseComplete();
        }
        catch (Exception exception)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ("Dosya indirme işleminde hata oluştu: " + exception.getMessage()), exception.getMessage()));
        }
    }

    public void yazarlariSirala()
    {
        try
        {
            yazarListesi = new ArrayList();
            for (Makale makale : makaleListesi)
            {
                if (yazarListesi.size() == 0)
                {
                    yeniYazarEkle(makale);
                }
                else
                {
                    boolean yazarBulundu = false;
                    for (Yazar yazar : yazarListesi)
                    {
                        if ((yazar.getId() != null
                                && !yazar.getId().trim().equals("")
                                && makale.getYazarId() != null
                                && !makale.getYazarId().trim().equals("")
                                && yazar.getId().equals(makale.getYazarId()))
                                ||
                                (yazar.getTamAdi() != null
                                        && !yazar.getTamAdi().trim().equals("")
                                        && makale.getYazarTamAdi() != null
                                        && !makale.getYazarTamAdi().trim().equals("")
                                        && yazar.getTamAdi().equals(makale.getYazarTamAdi())))
                        {
                            yazarBulundu = true;
                            yazar.setToplamSkoru(yazar.getToplamSkoru() + makale.getAramaSkoru());
                            yazar.setBulunanMakaleSayisi(yazar.getMakaleListesi().size() + 1);
                            yazar.getMakaleListesi().add(makale);
                            break;
                        }
                    }
                    if (!yazarBulundu)
                    {
                        yeniYazarEkle(makale);
                    }
                }
            }
            Yazar.sortByToplamSkor(yazarListesi);
        }
        catch (Exception exception)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ("Yazar değerlendirme işleminde hata oluştu: " + exception.getMessage()), exception.getMessage()));
        }
    }

    private void yeniYazarEkle(Makale makale)
    {
        Yazar yazar = new Yazar();
        yazar.setId(makale.getYazarId());
        yazar.setTamAdi(makale.getYazarTamAdi());
        yazar.setBulunanMakaleSayisi(1);
        yazar.setToplamSkoru(makale.getAramaSkoru());
        yazar.setMakaleListesi(new ArrayList<Makale>());
        yazar.getMakaleListesi().add(makale);
        yazarListesi.add(yazar);
    }

    public String getIndeksAnaDizini()
    {
        return indeksAnaDizini;
    }

    public void setIndeksAnaDizini(String indeksAnaDizini)
    {
        this.indeksAnaDizini = indeksAnaDizini;
    }

    public List<String> getIndeksDizinleri()
    {
        return indeksDizinleri;
    }

    public void setIndeksDizinleri(List<String> indeksDizinleri)
    {
        this.indeksDizinleri = indeksDizinleri;
    }

    public String getSecilenIndeksDizini()
    {
        return secilenIndeksDizini;
    }

    public void setSecilenIndeksDizini(String secilenIndeksDizini)
    {
        this.secilenIndeksDizini = secilenIndeksDizini;
    }

    public List<Makale> getMakaleListesi()
    {
        return makaleListesi;
    }

    public void setMakaleListesi(List<Makale> makaleListesi)
    {
        this.makaleListesi = makaleListesi;
    }

    public String getOzet()
    {
        return ozet;
    }

    public void setOzet(String ozet)
    {
        this.ozet = ozet;
    }

    public String getAnahtarlar()
    {
        return anahtarlar;
    }

    public void setAnahtarlar(String anahtarlar)
    {
        this.anahtarlar = anahtarlar;
    }

    public String getBaslik()
    {
        return baslik;
    }

    public void setBaslik(String baslik)
    {
        this.baslik = baslik;
    }

    public String getAnalyser()
    {
        return analyser;
    }

    public void setAnalyser(String analyser)
    {
        this.analyser = analyser;
    }

    public List<Yazar> getYazarListesi()
    {
        return yazarListesi;
    }

    public void setYazarListesi(List<Yazar> yazarListesi)
    {
        this.yazarListesi = yazarListesi;
    }
}
