/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.realtimecc2.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author micim
 */
@Entity(name = "personen")
public class Personen implements Serializable 
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
    
    @Column(name = "Vorname")
    private String vorname;
    
    public String getVorname()
    {
	return vorname;
    }
    
    public void setVorname(String vorname)
    {
	this.vorname = vorname;
    }
    
    @Column(name = "Nachname")
    private String nachname;
    
    public String getNachname()
    {
	return nachname;
    }
    
    public void setNachname(String nachname)
    {
	this.nachname = nachname;
    }
    
    @Column(name = "Position")
    private String position;
    
    public String getPosition()
    {
	return position;
    }
    
    public void setPosition(String position)
    {
	this.position = position;
    }
    
    @Column(name = "Standort")
    private String standort;
    
    public String getStandort()
    {
	return standort;
    }
    
    public void setStandort(String standort)
    {
	this.standort = standort;
    }
    @Column(name = "E_Mail")
    private String email;
    
    public String getEmail()
    {
	return email;
    }
    
    public void setEmail(String email)
    {
	this.email = email;
    }
    @Column(name = "Telefon")
    private String telefon;
    
    public String getTelefon()
    {
	return telefon;
    }
    
    public void setTelefon(String telefon)
    {
	this.telefon = benutzer;
    }
    @Column(name = "Benutzer")
    private String benutzer;
    
    public String getBenutzer()
    {
	return benutzer;
    }
    
    public void setBenutzer(String benutzer)
    {
	this.benutzer = benutzer;
    }
    @Column(name = "Passwort")
    private String passwort;
    
    public String getPasswort()
    {
	return passwort;
    }
    
    public void setPasswort(String passwort)
    {
	this.passwort = passwort;
    }
    @Column(name = "Rolle")
    private String rolle;
    
    public String getRolle()
    {
	return rolle;
    }
    
    public void setRolle(String rolle)
    {
	this.rolle = standort;
    }
}
