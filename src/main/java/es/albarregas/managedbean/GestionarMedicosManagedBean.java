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
 * Managed Bean para la gestión de los médicos por parte del administrador
 *
 * @author Ricardo
 */
@ViewScoped
@ManagedBean(name = "gestionarMedicosBean")
public class GestionarMedicosManagedBean implements Serializable {

    private Cita cita;
    private Usuario usuario;
    private Paciente paciente;
    private Medico medico;
    private List<Medico> listMedicos;
    private UIDataTable tablaMedicos;
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

    public List<Medico> getListMedicos() {
        return listMedicos;
    }

    public void setListMedicos(List<Medico> listMedicos) {
        this.listMedicos = listMedicos;
    }

    public UIDataTable getTablaMedicos() {
        return tablaMedicos;
    }

    public void setTablaMedicos(UIDataTable tablaMedicos) {
        this.tablaMedicos = tablaMedicos;
    }

    /**
     * Cargamos al entrar en la vista
     */
    @PostConstruct
    public void init() {

        try {
            df = DAOFactory.getDAOFactory();
            igd = df.getGenericoDAO();

            usuario = (Usuario) FacesUtils.getSession("usuario");
            listMedicos = igd.get("Medico");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método en que si la ausencia está puesta la quitamos, si no la tiene la ponemos (Para ausencias médicas)
     * @return 
     */
    public String ponerQuitarAusencias() {
        try {
            Medico medicoUpd = (Medico) tablaMedicos.getRowData();

            if (medicoUpd.getAusencia().equals("n")) {
                medicoUpd.setAusencia("s");
            } else {
                medicoUpd.setAusencia("n");
            }

            igd.update(medicoUpd);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }



}
