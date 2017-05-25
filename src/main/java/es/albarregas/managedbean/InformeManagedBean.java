/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.managedbean;

import es.albarregas.dao.GenericoDAO;
import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Informe;
import es.albarregas.modelo.Medico;
import es.albarregas.modelo.Paciente;
import es.albarregas.modelo.Usuario;
import es.albarregas.persistencia.FacesUtils;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.richfaces.component.UIDataTable;

/**
 * Managed Bean para tratar la subida y bajada de imágenes de forma aislada para reutilizar código y evitar problemas
 * @author Ricardo
 */
@ViewScoped
@ManagedBean(name = "informeBean")
public class InformeManagedBean implements Cloneable, Serializable {

    private Usuario usuario;
    private Paciente paciente;
    private Medico medico;
    private Informe informe;
    private List<Informe> listInformes;
    private List<Paciente> listPacientes;
    private UIDataTable panelInforme;

    DAOFactory df;
    IGenericoDAO igd;

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

    public List<Paciente> getListPacientes() {
        return listPacientes;
    }

    public void setListPacientes(List<Paciente> listPacientes) {
        this.listPacientes = listPacientes;
    }

    public UIDataTable getPanelInforme() {
        return panelInforme;
    }

    public void setPanelInforme(UIDataTable panelInforme) {
        this.panelInforme = panelInforme;
    }
    
    

    @PostConstruct
    public void init() {
        df = DAOFactory.getDAOFactory();
        igd = df.getGenericoDAO();
        
        if (FacesUtils.getSession("usuario") != null) {
            usuario = (Usuario) FacesUtils.getSession("usuario");
        }

        informe = new Informe();
        medico = new Medico();
        informe.setFecha(new Date());

        //Con el switch reutilizamos mucho código, sino tendríamos que hacer dos ManagedBeans muy parecidos para cada tipo de usuario
        switch (usuario.getTipo()) {
            case "p":
                listInformes = igd.get("Informe i WHERE i.paciente.id = " + usuario.getId());
                break;
            case "m":
                listInformes = igd.get("Informe i WHERE i.medico.id = " + usuario.getId());
                listPacientes = igd.get("Paciente p WHERE p.medico.id = " + usuario.getId());
                medico = (Medico) igd.getOne(usuario.getId(), Medico.class);
                break;
            default:
                break;
        }

    }

    /**
     * Para sacar la fecha de hoy formateada
     * @return 
     */
    public String fechaFormateada() {
        String nombreCentro = this.usuario.getCentro().getNombre();
        String fechaFormateada = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy").format(informe.getFecha());
        fechaFormateada = nombreCentro + ", " + fechaFormateada;
        return fechaFormateada;
    }

    /**
     * Rescatamos el paciente del futuro informe a añadir
     * @return 
     */
    public String iniciarInforme() {
        paciente = (Paciente) panelInforme.getRowData();
        System.out.println("Id paciente: " + paciente.getId());
        return null;
    }

    /**
     * Añadimos el informe a la base de datos
     * @return
     * @throws Exception 
     */
    public String guardarInforme() throws Exception {
        System.out.println("Guardando informe");
        informe.setMedico(medico);
        informe.setPaciente(paciente);
        informe.setFecha(new Date());
        igd.add(informe);
        return "";

    }

    /**
     * Rescatamos el informe seleccionado
     * @return
     * @throws Exception 
     */
    public String verInforme() throws Exception {
        informe = (Informe) panelInforme.getRowData();
        return "";
    }

}
