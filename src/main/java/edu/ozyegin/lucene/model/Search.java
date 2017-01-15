package edu.ozyegin.lucene.model;

/**
 * User: TTEDEMIRCIOGLU
 * Date: 03.12.2016
 * Time: 20:18
 */
public class Search
{
    public static void main(String[] args)
    {
        try
        {
            MySearcher.main(new String[]{"-index", "D:\\Kisisel\\Ozyegin\\Dersler\\Donem1\\InformationRetrieval\\Proje\\Makaleler\\Openaire\\yeni\\index\\indeks_tur_baslik1.0_anahtar1.0_ozet1.0"
                    , "-field", "baslik"
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
