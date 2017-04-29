/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.persistencia;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ricardo
 */
public class FacesUtils {

    /*
        Método para añadir atributo a la sesión (nombre, valor)
    */
    public static void addSession(String key, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);
    }

    /*
        Método para eliminar un atributo de la sesión (nombre)
    */
    public static void deleteAttributeSession(String key){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(key);
    }
    
    /*
       Método para eliminar la sesión
    */
    public static void deleteSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(false);
        HttpSession httpSession = (HttpSession) session;
        httpSession.invalidate();
    }
    
    /*
        Método para añadir mensaje a las vistas (idCliente,mensaje)
    */
    public static void addMessage(String idCliente, String tipo, String mensaje){
        
        FacesMessage.Severity severity = null;
        
        //Lo paso a mayusculas y sin espacios para evitar que no funcione por errores al escribir
        switch(tipo.toUpperCase().trim()){
            case "ERROR": severity = FacesMessage.SEVERITY_ERROR; break;
            case "FATAL": severity = FacesMessage.SEVERITY_FATAL; break;
            case "INFO": severity = FacesMessage.SEVERITY_INFO; break;
            case "WARN": severity = FacesMessage.SEVERITY_WARN; break;       
            default: severity = FacesMessage.SEVERITY_ERROR;
        }
        FacesContext.getCurrentInstance().addMessage(idCliente,
                new FacesMessage(severity, mensaje, mensaje));

    }
}
