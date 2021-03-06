
package es.albarregas.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *  Clase con métodos para acceder o escribir en la sesión y añadir mensajes de error o informrmación
 * @author Ricardo
 */
public class FacesUtils {

    
    /**
     * Método para añadir atributo a la sesión (nombre, valor)
     * @param key
     * @param value 
     */
    public static void addSession(String key, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);
    }

    /**
     * Método para eliminar un atributo de la sesión (nombre)
     * @param key 
     */
    public static void deleteAttributeSession(String key){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(key);
    }
    
    /**
     * Método para eliminar la sesión completa
     */
    public static void deleteSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(false);
        HttpSession httpSession = (HttpSession) session;
        if(session != null){
            httpSession.invalidate();
        }
        
    }
    
    /**
     * Método para obtener la sesión
     * @param key
     * @return 
     */
    public static Object getSession(String key){
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
    }
    
    /**
     * Método para añadir mensaje a las vistas (idCliente,mensaje)
     * @param idCliente
     * @param tipo
     * @param mensaje 
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
