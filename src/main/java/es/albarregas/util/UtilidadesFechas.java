/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.util;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ricardo
 */
public class UtilidadesFechas {
    
     /**
     * Método para pasarle una Fecha, la hora y los minutos y que te devuelva la fecha con las tres cosas,
     * ya que no se puede hacer directamente con la clase Date
     * @param fecha
     * @param hora
     * @param minuto
     * @return 
     */
    public static Date getDateConFechaYHora(Date fecha, int hora, int minuto){
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, minuto);
        return cal.getTime();   
    }
    
}
