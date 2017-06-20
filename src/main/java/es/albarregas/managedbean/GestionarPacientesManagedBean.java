package es.albarregas.managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Cita;
import es.albarregas.modelo.Medico;
import es.albarregas.modelo.Paciente;
import es.albarregas.modelo.Usuario;
import es.albarregas.util.FacesUtils;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.richfaces.component.UIDataTable;

/**
 * Managed Bean para la gestión de los pacientes por parte del administrador
 *
 * @author Ricardo
 */
@ViewScoped
@ManagedBean(name = "gestionarPacientesBean")
public class GestionarPacientesManagedBean implements Serializable {

    private Cita cita;
    private Usuario usuario;
    private Paciente paciente;
    private Medico medico;
    private List<Paciente> listPacientes;
    private UIDataTable tablaPacientes;
    private DAOFactory df;
    private IGenericoDAO igd;

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

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

    public List<Paciente> getListPacientes() {
        return listPacientes;
    }

    public void setListPacientes(List<Paciente> listPacientes) {
        this.listPacientes = listPacientes;
    }

    public UIDataTable getTablaPacientes() {
        return tablaPacientes;
    }

    public void setTablaPacientes(UIDataTable tablaPacientes) {
        this.tablaPacientes = tablaPacientes;
    }
    
    /**
     * Se carga al entrar en la vista
     */
    @PostConstruct
    public void init() {
        df = DAOFactory.getDAOFactory();
        igd = df.getGenericoDAO();

        usuario = (Usuario) FacesUtils.getSession("usuario");
        listPacientes = igd.get("Paciente");
    }

    /**
     * Método para boquear y desbloquear el acceso de los pacientes
     */
    public void bloquearPacientes() {
        try {
            Paciente pacienteSelec = (Paciente) tablaPacientes.getRowData();

            if (pacienteSelec.getBloqueado().equals("n")) {
                pacienteSelec.setBloqueado("s");
            } else {
                pacienteSelec.setBloqueado("n");
            }

            igd.update(pacienteSelec);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
