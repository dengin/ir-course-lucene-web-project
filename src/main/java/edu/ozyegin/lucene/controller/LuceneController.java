package edu.ozyegin.lucene.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * User: TTEDEMIRCIOGLU
 * Date: 12.01.2017
 * Time: 23:52
 */
@ManagedBean
@SessionScoped
public class LuceneController implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String parameter;
    private String result;

    public String getParameter()
    {
        return parameter;
    }

    public void setParameter(String parameter)
    {
        this.parameter = parameter;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String search()
    {
        return "aaaaa";
    }
}
