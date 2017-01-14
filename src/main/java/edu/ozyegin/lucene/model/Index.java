package edu.ozyegin.lucene.model;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * User: TTEDEMIRCIOGLU
 * Date: 03.12.2016
 * Time: 20:09
 */
public class Index
{
    public static void main(String[] args)
    {
        String indexDirectory = "D:\\Kisisel\\Ozyegin\\Dersler\\Donem1\\InformationRetrieval\\Proje\\Makaleler\\Openaire\\yeni\\index";
        String docsDirectory = "D:\\Kisisel\\Ozyegin\\Dersler\\Donem1\\InformationRetrieval\\Proje\\Makaleler\\Openaire\\yeni\\makale";
        index(new String[]{"-index", indexDirectory, "-docs", docsDirectory});
    }

    private static void index(String[] args)
    {
        String usage = "java org.apache.lucene.demo.IndexFiles [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\nThis indexes the documents in DOCS_PATH, creating a Lucene indexin INDEX_PATH that can be searched with SearchFiles";
        String indexPath = "index";
        String docsPath = null;
        boolean create = true;

        for(int docDir = 0; docDir < args.length; ++docDir) {
            if("-index".equals(args[docDir])) {
                indexPath = args[docDir + 1];
                ++docDir;
            } else if("-docs".equals(args[docDir])) {
                docsPath = args[docDir + 1];
                ++docDir;
            } else if("-update".equals(args[docDir])) {
                create = false;
            }
        }

        if(docsPath == null) {
            System.err.println("Usage: " + usage);
            System.exit(1);
        }

        Path var13 = Paths.get(docsPath, new String[0]);
        if(!Files.isReadable(var13)) {
            System.out.println("Document directory \'" + var13.toAbsolutePath() + "\' does not exist or is not readable, please check the path");
            System.exit(1);
        }

        Date start = new Date();

        try {
            MyIndexer myIndexer = new MyIndexer(indexPath, docsPath);
            myIndexer.indexData();

//            System.out.println("Indexing to directory \'" + indexPath + "\'...");
//            FSDirectory e = FSDirectory.open(Paths.get(indexPath, new String[0]));
//            TurkishAnalyzer analyzer = new TurkishAnalyzer();
//            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
//            if(create) {
//                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
//            } else {
//                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
//            }
//            IndexWriter writer = new IndexWriter(e, iwc);
//            indexDoc(writer, var13, Files.getLastModifiedTime(var13, new LinkOption[0]).toMillis());
//            writer.close();
            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");
        } catch (IOException var12) {
            System.out.println(" caught a " + var12.getClass() + "\n with message: " + var12.getMessage());
        }
    }

    private static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
//        InputStream stream = Files.newInputStream(file, new OpenOption[0]);
        Throwable var5 = null;
        try
        {

            File dataDir = new File("D:\\Kisisel\\Ozyegin\\Dersler\\Donem1\\InformationRetrieval\\Proje\\Makaleler\\Openaire\\yeni\\makale\\");
            if (!dataDir.exists())
            {
                throw new RuntimeException("D:\\Kisisel\\Ozyegin\\Dersler\\Donem1\\InformationRetrieval\\Proje\\Makaleler\\Openaire\\yeni\\makale\\" + " does not exist");
            }
            File[] files = dataDir.listFiles();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            for (File f : files)
            {
                org.w3c.dom.Document document = dBuilder.parse(f);
                document.getDocumentElement().normalize();
                NodeList nList = document.getElementsByTagName("makale");

                int i = 0;
                /*Step 1. Prepare the data for indexing. Extract the data. */
                for (int temp = 0; temp < nList.getLength(); temp++)
                {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE)
                    {

                        Element eElement = (Element) nNode;
                        StringField pathField = new StringField("path", f.toPath().toString(), Field.Store.YES);
                        String baslik = eElement.getElementsByTagName("baslik").item(0).getTextContent();
                        String yil = eElement.getElementsByTagName("yil").item(0).getTextContent();
                        String yazarlar = eElement.getElementsByTagName("yazarlar").item(0).getTextContent();
                        String anahtarlar = eElement.getElementsByTagName("anahtarlar").item(0).getTextContent();
                        String doi = eElement.getElementsByTagName("doi").item(0).getTextContent();
                        String ozet = eElement.getElementsByTagName("ozet").item(0).getTextContent();

                        Document doc = new Document();
//                        StringField pathField = new StringField("path", "1", Store.YES);
                        doc.add(pathField);
                        doc.add(new LongPoint("modified", new long[]{lastModified}));
                        doc.add(new TextField("baslik", baslik, Field.Store.YES));
                        doc.add(new TextField("yil", yil, Field.Store.YES));
                        doc.add(new TextField("yazarlar", yazarlar, Field.Store.YES));
                        doc.add(new TextField("anahtarlar", anahtarlar, Field.Store.YES));
                        doc.add(new TextField("doi", doi, Field.Store.YES));
                        doc.add(new TextField("ozet", ozet, Field.Store.YES));
                        doc.add(new TextField("contents", ozet, Field.Store.YES));
                        if(writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
//                System.out.println("adding " + file);
                            writer.addDocument(doc);
                        } else {
//                System.out.println("updating " + file);
//                writer.updateDocument(new Term("path", file.toString()), doc);
                        }
                    }
                }



            }






        } catch (Throwable var15) {
            var5 = var15;
//            throw var15;
        } finally {
//            if(stream != null) {
//                if(var5 != null) {
//                    try {
//                        stream.close();
//                    } catch (Throwable var14) {
//                        var5.addSuppressed(var14);
//                    }
//                } else {
//                    stream.close();
//                }
//            }

        }

    }
}
