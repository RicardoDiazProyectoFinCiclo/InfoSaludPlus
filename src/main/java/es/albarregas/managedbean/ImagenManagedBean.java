package es.albarregas.managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Centro;
import es.albarregas.modelo.Imagen;
import es.albarregas.modelo.Usuario;
import es.albarregas.util.FacesUtils;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

/**
 *
 * @author Ricardo
 */
@SessionScoped
@ManagedBean(name = "imagenBean")
public class ImagenManagedBean implements Serializable {

    private Usuario usuario;
    private Imagen imagen;
    private DAOFactory df;
    private IGenericoDAO igd;
    private int idCentro;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }

    public int getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

    /**
     * Se carga al entrar en cualquier método y en la vista
     */
    @PostConstruct
    public void init() {

        df = DAOFactory.getDAOFactory();
        igd = df.getGenericoDAO();
        //Esta información puede que sea redundante, pero he tenido que modificar in extremis, por la sesion qe daba problemas
        usuario = (Usuario) FacesUtils.getSession("usuario");
        if (usuario != null && !usuario.getTipo().equals("a") && usuario.getImagen() != null) {
            imagen = usuario.getImagen();
        }

    }

    /* MÉTODOS PARA SUBIR UNA IMAGEN CON RICHFACES */
    /**
     * Mostrar la imagen del usuario
     *
     * @param stream
     * @param object
     * @throws IOException
     * @throws SQLException
     */
    public void pintar(OutputStream stream, Object object) throws IOException, SQLException {

        try {

            usuario = (Usuario) FacesUtils.getSession("usuario");
            if (usuario != null && !usuario.getTipo().equals("a") && usuario.getImagen() != null) {
                imagen = usuario.getImagen();
            }

            if (imagen != null) {
                int tamanioImagen = (int) imagen.getFuenteImagen().length();
                RenderedImage image = ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(imagen.getFuenteImagen().getBytes(1, tamanioImagen))));
                ImageIO.write(image, imagen.getTipoMIME(), stream);

                System.out.println("Entrando en función pintar");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostrar la imagen del centro seleccionado
     *
     * @param stream
     * @param object
     * @throws IOException
     * @throws SQLException
     */
    public void pintarImagenCentro(OutputStream stream, Object object) throws IOException, SQLException {

        try {

            Centro centro = (Centro) FacesUtils.getSession("centroSeleccionado");
            if (centro != null && centro.getImagen() != null) {
                imagen = centro.getImagen();

                int tamanioImagen = (int) imagen.getFuenteImagen().length();
                RenderedImage image = ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(imagen.getFuenteImagen().getBytes(1, tamanioImagen))));
                ImageIO.write(image, imagen.getTipoMIME(), stream);

                System.out.println("Entrando en función pintar");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pintarImagenConId(OutputStream stream, Object object) throws IOException, SQLException {

        try {

//            Map<String,String> params;
//            params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//
//            String idCentro = params.get("paramIdCentro");
//            
//            Centro centro = (Centro)igd.getOne(Integer.parseInt(idCentro), Centro.class);
//            if (centro != null && centro.getImagen() != null) {
//                imagen = centro.getImagen();
//
//                int tamanioImagen = (int) imagen.getFuenteImagen().length();
//                RenderedImage image = ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(imagen.getFuenteImagen().getBytes(1, tamanioImagen))));
//                ImageIO.write(image, imagen.getTipoMIME(), stream);
//
//                System.out.println("Entrando en función pintar");
//            }
            Map<String, String> params;
            params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

            String idImagen = params.get("paramIdImagen");
            if (idImagen != null) {
                imagen = (Imagen) igd.getOne(Integer.parseInt(idImagen), Imagen.class);
                if (imagen != null) {

                    int tamanioImagen = (int) imagen.getFuenteImagen().length();
                    RenderedImage image = ImageIO.read(new BufferedInputStream(new ByteArrayInputStream(imagen.getFuenteImagen().getBytes(1, tamanioImagen))));
                    ImageIO.write(image, imagen.getTipoMIME(), stream);

                    System.out.println("Entrando en función pintar");
                }
            }

            imagen = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Añadir una imagen a un usuario
     *
     * @param event
     * @throws Exception
     */
    public void aniadirImagen(FileUploadEvent event) throws Exception {

        try {

            usuario = (Usuario) FacesUtils.getSession("usuario");
            if (usuario != null && !usuario.getTipo().equals("a") && usuario.getImagen() != null) {
                imagen = usuario.getImagen();
            }

            UploadedFile imagenSubida = event.getUploadedFile();

            Blob imagenRescatada = new javax.sql.rowset.serial.SerialBlob(imagenSubida.getData());

            //Si el usuario no tiene imagen guardamos, si tiene sobreescribimos
            if (imagen != null) {
                imagen.setTipoMIME(imagenSubida.getFileExtension());
                imagen.setFuenteImagen(imagenRescatada);
                igd.update(imagen);
            } else {
                this.setImagen(new Imagen());
                imagen.setTipoMIME(imagenSubida.getFileExtension());
                imagen.setFuenteImagen(imagenRescatada);
                imagen.setIdUsuario(usuario.getId());
//                usuario.setImagen(imagen);
                igd.add(imagen);
                List<Imagen> imagenes = igd.get("Imagen where idUsuario = " + imagen.getIdUsuario());
                imagen = imagenes.get(0);
                System.out.println("Guardamos imagen en usuario" + imagen.getIdUsuario());
            }

            usuario.setImagen(imagen);
            igd.update(usuario);
            FacesUtils.addSession("usuario", usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Añadir imagen a un Centro
     *
     * @param event
     * @throws Exception
     */
    public void aniadirImagenCentro(FileUploadEvent event) throws Exception {

        try {
            UploadedFile imagenSubida = event.getUploadedFile();

            Blob imagenRescatada = new javax.sql.rowset.serial.SerialBlob(imagenSubida.getData());

            Centro centro = (Centro) FacesUtils.getSession("centroSeleccionado");

            if (centro != null) {
                imagen = centro.getImagen();
            } else {
                imagen = null;
            }

            //Si el usuario no tiene imagen guardamos, si tiene sobreescribimos
            if (imagen != null) {
                imagen.setTipoMIME(imagenSubida.getFileExtension());
                imagen.setFuenteImagen(imagenRescatada);
                igd.update(imagen);
            } else {
                this.setImagen(new Imagen());
                imagen.setTipoMIME(imagenSubida.getFileExtension());
                imagen.setFuenteImagen(imagenRescatada);
                imagen.setIdCentro(centro.getId());
                igd.add(imagen);
                List<Imagen> imagenes = igd.get("Imagen where idCentro = " + imagen.getIdCentro());
                imagen = imagenes.get(0);
                System.out.println("Guardamos imagen en centro " + imagen.getIdCentro());
            }

            centro.setImagen(imagen);
            igd.update(centro);
            FacesUtils.addSession("centroSeleccionado", centro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
