/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Informe;
import es.albarregas.modelo.Medico;
import es.albarregas.modelo.Paciente;
import es.albarregas.modelo.Usuario;
import es.albarregas.util.FacesUtils;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
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
    private Boolean informeLectura;

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

    public Boolean getInformeLectura() {
        return informeLectura;
    }

    public void setInformeLectura(Boolean informeLectura) {
        this.informeLectura = informeLectura;
    }
    
    

    @PostConstruct
    public void init() {
        df = DAOFactory.getDAOFactory();
        igd = df.getGenericoDAO();
        informeLectura = false;
        
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
                listInformes = igd.get("Informe i WHERE i.medico.id = " + usuario.getId() +" ORDER BY fecha desc");
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
        informeLectura = false;
        paciente = (Paciente) panelInforme.getRowData();
        informe.setPaciente(paciente);
        informe.setMedico(medico);
        System.out.println("Id paciente: " + paciente.getId());
        return "";
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
        informeLectura = true;
        informe = (Informe) panelInforme.getRowData();
        return "";
    }
    
    public void imprimirInforme(){
        try {
            URL url = this.getClass().getResource("/es/albarregas/reportes/Informe.jasper");
            System.out.println("Ruta: " + url.toString());
            String paramNombrePaciente = informe.getPaciente().getApellidos() + "," + informe.getPaciente().getNombre();
            String paramNombreMedico = informe.getMedico().getApellidos() + "," + informe.getMedico().getNombre();
            String paramTitulo = informe.getTitulo();
            String paramObservacion = informe.getObservacion();
            String paramTratamiento = informe.getTratamiento();
            String paramCentro = informe.getMedico().getCentro().getNombre();
            String paramFecha = informe.fechaFormateada();
            String paramHora = informe.horaFormateada();
            String paramTelefonoPaciente = informe.getPaciente().getDireccion().getTelefono();
            String paramDireccionPaciente = informe.getMedico().getCentro().getDireccion().getDireccion() + ", ("
                    + informe.getMedico().getCentro().getDireccion().getPueblo().getCodigoPostal() + ") "
                    + informe.getMedico().getDireccion().getPueblo().getNombre() + ", "
                    + informe.getMedico().getDireccion().getPueblo().getProvincia().getNombre();

            String paramTelefono = informe.getMedico().getCentro().getDireccion().getTelefono();
            String paramEspecialidadMedico = informe.getMedico().getServicio().getNombre();
            String paramDescripcion = informe.getDescripcion();
            
            JasperReport jr = (JasperReport) JRLoader.loadObject(url);
            Map parametros = new HashMap<String, Object>();
            parametros.put("nombrePaciente", paramNombrePaciente);
            parametros.put("telefonoPaciente", paramTelefonoPaciente);
            parametros.put("direccionPaciente", paramDireccionPaciente);
            
            parametros.put("nombreMedico", paramNombreMedico);
            parametros.put("especialidadMedico", paramEspecialidadMedico);
            parametros.put("centroMedico", paramCentro);
            
            parametros.put("titulo",paramTitulo);
            parametros.put("tratamiento", paramTratamiento);
            parametros.put("observacion", paramObservacion);
            parametros.put("descripcion", paramDescripcion);
            
            parametros.put("fechaInforme", paramFecha);
            parametros.put("hora", paramHora);

            JasperPrint jp = JasperFillManager.fillReport(jr, parametros, new JREmptyDataSource());
            JasperViewer jv = new JasperViewer(jp);
            jv.show();
        } catch (Exception ex) {
            Logger.getLogger(GestionarMedicosManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
