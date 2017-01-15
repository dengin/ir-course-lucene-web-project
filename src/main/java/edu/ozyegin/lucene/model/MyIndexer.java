package edu.ozyegin.lucene.model;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tr.TurkishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * User: TTEDEMIRCIOGLU
 * Date: 21.12.2016
 * Time: 19:19
 */
public class MyIndexer
{
    private IndexWriter indexWriter;
    /*Location of directory where index files are stored */
    private String indexDirectory;
    /*Location of data directory */
    private String dataDirectory;

    private Float baslikBoostValue = 2.2F;
    private Float anahtarlarBoostValue;
    private Float ozetBoostValue;
    private String analyser = "Standart";

    public MyIndexer(String indexDirectory, String dataDirectory, float baslikBoostValue, float anahtarlarBoostValue, float ozetBoostValue, String analyser) throws Exception
    {
        this.indexDirectory = indexDirectory;
        this.dataDirectory = dataDirectory;
        this.baslikBoostValue = baslikBoostValue;
        this.anahtarlarBoostValue = anahtarlarBoostValue;
        this.ozetBoostValue = ozetBoostValue;
        this.analyser = analyser;
        createIndexWriter();
    }

    /**
     * This method creates an instance of IndexWriter which is used
     * to add Documents and write indexes on the disc.
     */
    void createIndexWriter() throws Exception
    {
        if (indexWriter == null)
        {
            prepareIndexDirectoryPath();

            //Create instance of Directory where index files will be stored
            FSDirectory fsDirectory = FSDirectory.open(Paths.get(indexDirectory, new String[0]));
                /* Create instance of analyzer, which will be used to tokenize
                the input data */
            Analyzer analyzer;
            if (this.analyser != null && this.analyser.equals("Turkce"))
            {
                analyzer = new TurkishAnalyzer();
            }
            else
            {
                analyzer = new StandardAnalyzer();
            }

            IndexWriterConfig config = new IndexWriterConfig(analyzer);

            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            indexWriter = new IndexWriter(fsDirectory, config);
        }
    }

    private void prepareIndexDirectoryPath() throws Exception
    {
        File indexDirectoryParam = new File(indexDirectory);
        if (!indexDirectoryParam.exists())
        {
            throw new Exception("İndeks ana dizini bulunamadı");
        }
        indexDirectory += "/indeks_";
        if (this.analyser != null)
        {
            if (this.analyser.equals("Turkce"))
            {
                indexDirectory += "tur_";
            }
            else
            {
                indexDirectory += "std_";
            }
        }
        if (this.baslikBoostValue != null)
        {
            indexDirectory += "baslik" + this.baslikBoostValue.toString() + "_";
        }
        if (this.anahtarlarBoostValue != null)
        {
            indexDirectory += "anahtar" + this.anahtarlarBoostValue.toString() + "_";
        }
        if (this.ozetBoostValue != null)
        {
            indexDirectory += "ozet" + this.ozetBoostValue.toString() + "_";
        }
        if (indexDirectory.endsWith("_"))
        {
            indexDirectory = indexDirectory.substring(0, indexDirectory.length() - 1);
        }
         indexDirectoryParam = new File(indexDirectory);
        if (indexDirectoryParam.exists())
        {
            throw new Exception("İndeks daha önceden kaydedilmiş");
        }
    }

    /**
     * This method reads data directory and loads all properties files.
     * It extracts  various fields and writes them to the index using IndexWriter.
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void indexData() throws IOException, ParserConfigurationException, SAXException
    {
        Path dataPath = Paths.get(dataDirectory, new String[0]);
        long lastModifiedTime = Files.getLastModifiedTime(dataPath, new LinkOption[0]).toMillis();

        File[] files = getFilesToBeIndexed();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        for (File file : files)
        {
            org.w3c.dom.Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("makale");

                /*Step 1. Prepare the data for indexing. Extract the data. */
            for (int temp = 0; temp < nList.getLength(); temp++)
            {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;
                    StringField pathField = new StringField("path", file.toPath().toString(), Field.Store.YES);
                    String baslik = eElement.getElementsByTagName("baslik").item(0).getTextContent();
                    String yil = eElement.getElementsByTagName("yil").item(0).getTextContent();
                    String yazarlar = eElement.getElementsByTagName("yazarlar").item(0).getTextContent();
                    String anahtarlar = eElement.getElementsByTagName("anahtarlar").item(0).getTextContent();
                    String doi = eElement.getElementsByTagName("doi").item(0).getTextContent();
                    String ozet = eElement.getElementsByTagName("ozet").item(0).getTextContent();

                        /*Step 2. Wrap the data in the Fields and add them to a Document */

                    TextField baslikField = new TextField("baslik", baslik, Field.Store.YES);
                    TextField yilField = new TextField("yil", yil, Field.Store.YES);
                    TextField yazarlarField = new TextField("yazarlar", yazarlar, Field.Store.YES);
                    TextField anahtarlarField = new TextField("anahtarlar", anahtarlar, Field.Store.YES);
                    TextField doiField = new TextField("doi", doi, Field.Store.YES);
                    TextField ozetField = new TextField("ozet", ozet, Field.Store.YES);

                    setBoostValues(baslikField, yilField, anahtarlarField, doiField, ozetField);

                    // Add these fields to a Lucene Document
                    Document document = new Document();
                    document.add(pathField);
                    document.add(new LongPoint("modified", new long[]{lastModifiedTime}));
                    document.add(baslikField);
                    document.add(yilField);
                    document.add(yazarlarField);
                    document.add(anahtarlarField);
                    document.add(doiField);
                    document.add(ozetField);

                    //Step 3: Add this document to Lucene Index.
                    indexWriter.addDocument(document);
                }
            }
        }
		/*
         * Commits all changes to the index and closes all associated files.
		 */
        indexWriter.close();
    }

    private void setBoostValues(TextField baslikField, TextField yilField, TextField anahtarlarField, TextField doiField, TextField ozetField)
    {
        // Display search results that contain query in their baslik field first by setting boost factor
        if (baslikBoostValue != null)
        {
            baslikField.setBoost(baslikBoostValue);
        }
        if (anahtarlarBoostValue != null)
        {
            anahtarlarField.setBoost(anahtarlarBoostValue);
        }
        if (ozetBoostValue != null)
        {
            ozetField.setBoost(ozetBoostValue);
        }
    }

    private File[] getFilesToBeIndexed()
    {
        File dataDir = new File(dataDirectory);
        if (!dataDir.exists())
        {
            throw new RuntimeException(dataDirectory + " bulunamadı");
        }
        return dataDir.listFiles();
    }

    public Float getBaslikBoostValue()
    {
        return baslikBoostValue;
    }

    public void setBaslikBoostValue(Float baslikBoostValue)
    {
        this.baslikBoostValue = baslikBoostValue;
    }

    public Float getAnahtarlarBoostValue()
    {
        return anahtarlarBoostValue;
    }

    public void setAnahtarlarBoostValue(Float anahtarlarBoostValue)
    {
        this.anahtarlarBoostValue = anahtarlarBoostValue;
    }

    public Float getOzetBoostValue()
    {
        return ozetBoostValue;
    }

    public void setOzetBoostValue(Float ozetBoostValue)
    {
        this.ozetBoostValue = ozetBoostValue;
    }
}
