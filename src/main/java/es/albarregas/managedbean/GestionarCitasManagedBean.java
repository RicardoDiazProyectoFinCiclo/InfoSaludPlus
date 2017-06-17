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
 * Managed Bean para la gestión de las citas por parte del administrador
 *
 * @author Ricardo
 */
@ViewScoped
@ManagedBean(name = "gestionarCitasBean")
public class GestionarCitasManagedBean implements Serializable {

    private Cita cita;
    private Usuario usuario;
    private Paciente paciente;
    private Medico medico;
    private List<Cita> listCitas;
    private UIDataTable tablaCitas;
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

    public List<Cita> getListCitas() {
        return listCitas;
    }

    public void setListCitas(List<Cita> listCitas) {
        this.listCitas = listCitas;
    }

    public UIDataTable getTablaCitas() {
        return tablaCitas;
    }

    public void setTablaCitas(UIDataTable tablaCitas) {
        this.tablaCitas = tablaCitas;
    }

    @PostConstruct
    public void init() {
        df = DAOFactory.getDAOFactory();
        igd = df.getGenericoDAO();

        usuario = (Usuario) FacesUtils.getSession("usuario");
        listCitas = igd.get("Cita Where DATE(fecha) > CURDATE()");
    }

}
