/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.realtimecc2.dao;

import com.realtimecc2.model.Personen;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author micim
 */
@Repository
public interface PersonRepository extends CrudRepository<Personen, Long>
{
    Personen getByBenutzerAndPasswort(String benutzer, String passwort);
    Personen getByBenutzer(String benutzer);
    List<Personen> findByRolle(String rolle);
    
}
