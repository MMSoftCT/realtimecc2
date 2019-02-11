/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.realtimecc2.dao;

import com.realtimecc2.model.Zeiten;
import java.util.Date;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author micim
 */
@Repository
public interface ZeitRepository extends CrudRepository<Zeiten, Long>
{
    Zeiten getByPersonidAndDatum(Long personid,Date datum);
    List<Zeiten> findByPersonid(Long personid);
    List<Zeiten> findByPersonidAndDatumBetween(long personid,Date start, Date end);
}
