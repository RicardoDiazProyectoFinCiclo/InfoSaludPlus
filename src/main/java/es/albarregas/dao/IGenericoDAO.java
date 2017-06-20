package es.albarregas.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Interface del DAO Genérico para acceder a base de datos
 * @author Ricardo
 * @param <T> 
 */
public interface IGenericoDAO<T> {

    /**
     * Añadir registro a base de datos
     * @param objeto 
     */
    public void add(T objeto);

    /**
     * Obtener un array de registros de base de datos
     * @param <T>
     * @param entidad
     * @return 
     */
    public <T> List<T> get(String entidad);

    /**
     * Obtener un solo registro de base de datos
     * @param <T>
     * @param pk
     * @param claseEntidad
     * @return 
     */
    public <T> T getOne(Serializable pk, Class<T> claseEntidad);

    /**
     * Actualizar registro de base de datos
     * @param objeto 
     */
    public void update(T objeto);

    /**
     * Eliminar registro de base de datos
     * @param objeto 
     */
    public void delete(T objeto);

}