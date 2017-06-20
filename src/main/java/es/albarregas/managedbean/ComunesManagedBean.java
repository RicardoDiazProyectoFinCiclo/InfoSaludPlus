package es.albarregas.managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Centro;
import es.albarregas.modelo.Medico;
import es.albarregas.modelo.Servicio;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *  Managed Bean para cosas que se van a mostrar en páginas públicas o comunes
 * @author Ricardo
 */
@ViewScoped
@ManagedBean(name = "comunesBean")
public class ComunesManagedBean implements Serializable{
    
    private DAOFactory df;
    private IGenericoDAO igd;
    private List<Centro> listCentros; 
    private List<Servicio> listServicios;
    private List<Medico> listMedicos;

    public DAOFactory getDf() {
        return df;
    }

    public void setDf(DAOFactory df) {
        this.df = df;
    }

    public IGenericoDAO getIgd() {
        return igd;
    }

    public void setIgd(IGenericoDAO igd) {
        this.igd = igd;
    }

    public List<Centro> getListCentros() {
        return listCentros;
    }

    public void setListCentros(List<Centro> listCentros) {
        this.listCentros = listCentros;
    }

    public List<Servicio> getListServicios() {
        return listServicios;
    }

    public void setListServicios(List<Servicio> listServicios) {
        this.listServicios = listServicios;
    }

    public List<Medico> getListMedicos() {
        return listMedicos;
    }

    public void setListMedicos(List<Medico> listMedicos) {
        this.listMedicos = listMedicos;
    }
    
    
  
    
    /**
     * Postconstructor, se carga cada vez que entramos en la página
     */
    @PostConstruct
    public void init(){
        df = DAOFactory.getDAOFactory();
        igd = df.getGenericoDAO();
        
        this.setListCentros(igd.get("Centro Order By nombre"));
        this.setListServicios(igd.get("Servicio Order By nombre "));
        this.setListMedicos(igd.get("Medico Order By nombre"));
    }
    
    
    
    
}
