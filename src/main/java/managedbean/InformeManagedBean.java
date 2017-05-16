/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import es.albarregas.dao.GenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Informe;
import es.albarregas.modelo.Medico;
import es.albarregas.modelo.Paciente;
import es.albarregas.modelo.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Ricardo
 */

@ManagedBean (name = "informeBean")
public class InformeManagedBean implements Serializable{
    
    private Usuario usuario;
    private Paciente paciente;
    private Medico medico;  
    private Informe informe;
    private List<Informe> listInformes;
    DAOFactory df = null;
    GenericoDAO igd = null;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Informe getInforme() {
        return informe;
    }

    public void setInforme(Informe informe) {
        this.informe = informe;
    }

    public List<Informe> getListInformes() {
        return listInformes;
    }

    public void setListInformes(List<Informe> listInformes) {
        this.listInformes = listInformes;
    }
    
    
    
    
    public String generar(){
        df = DAOFactory.getDAOFactory();
        igd = new GenericoDAO();
        listInformes = igd.get("Informe");
        
        System.out.println("Entrando en informes medicos paciente");
        return "informesMedicosPaciente";
        
    }
    
}
