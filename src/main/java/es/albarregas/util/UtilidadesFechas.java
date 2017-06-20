/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.util;

import java.util.Calendar;
import java.util.Date;

/**
 *  Clase con algunas utilidades de fechas que nos pueden ser necesarios
 * @author Ricardo
 */
public class UtilidadesFechas {

    /**
     * Método para pasarle una Fecha, la hora y los minutos y que te devuelva la
     * fecha con las tres cosas, ya que no se puede hacer directamente con la
     * clase Date
     *
     * @param fecha
     * @param hora
     * @param minuto
     * @return
     */
    public static Date getDateConFechaYHora(Date fecha, int hora, int minuto) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, minuto);
        return cal.getTime();
    }

    /**
     * Obtienes el date con la fecha y hora, y si es finde lo pasa al lunes a la misma hora
     * @param fecha
     * @param hora
     * @param minuto
     * @return 
     */
    public static Date getDateConFechaYHoraSinFindes(Date fecha, int hora, int minuto) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        if(cal.get(Calendar.DAY_OF_WEEK) == 7){
            cal.add(Calendar.DATE, 2);
        }else if(cal.get(Calendar.DAY_OF_WEEK) == 1) {
            cal.add(Calendar.DATE, 1);
        }
        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, minuto);
        return cal.getTime();
    }

}
