/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.realtimecc2.model;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author micim
 */
@Entity(name = "zeiten")
public class Zeiten implements Serializable 
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    
    public long getId()
    {
	return id;
    }
    
    public void setId(long id)
    {
	this.id = id;
    }
    
    @Column(name = "Personid")
    private long personid;
    
    public long getPersonid()
    {
	return personid;
    }
    
    public void setPersonid(long personId)
    {
	this.personid = personId;
    }
    
    @Column(name = "Datum")
    private Date datum;
    
    public Date getDatum()
    {
	return datum;
    }
    
    public void setDatum(Date datum)
    {
	this.datum = datum;
    }
    
    @Column(name = "Kommen")
    private Time kommen;
    
    public Time getKommen()
    {
	return kommen;
    }
    
    public void setKommen(Time kommen)
    {
	this.kommen = kommen;
    }
    
    @Column(name = "Gehen")
    private Time gehen;
    
    public Time getGehen()
    {
	return gehen;
    }
    
    public void setGehen(Time gehen)
    {
	this.gehen = gehen;
    }
}
