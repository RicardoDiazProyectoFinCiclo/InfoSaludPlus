/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.albarregas.managedbean;

import es.albarregas.dao.IGenericoDAO;
import es.albarregas.daofactory.DAOFactory;
import es.albarregas.modelo.Imagen;
import es.albarregas.modelo.Usuario;
import es.albarregas.persistencia.FacesUtils;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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

    @PostConstruct
    public void init() {
        df = DAOFactory.getDAOFactory();
        igd = df.getGenericoDAO();
        usuario = (Usuario) FacesUtils.getSession("usuario");
        if(usuario.getImagen() != null){
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
     * Añadir una imagen
     *
     * @param event
     * @throws Exception
     */
    public void aniadirImagen(FileUploadEvent event) throws Exception {

        try {
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
                usuario.setImagen(imagen);
                igd.add(imagen);
                List<Imagen> imagenes = igd.get("Imagen where idUsuario = " + imagen.getIdUsuario());
                imagen = imagenes.get(0);
                System.out.println("Guardamos " + imagen.getIdUsuario());
            }

            usuario.setImagen(imagen);
            igd.update(usuario);
            FacesUtils.addSession("usuario", usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
