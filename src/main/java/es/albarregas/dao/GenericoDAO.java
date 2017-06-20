package es.albarregas.dao;

import es.albarregas.persistencia.HibernateUtil;
import java.io.Serializable;

import java.util.List;
import org.hibernate.HibernateException;

import org.hibernate.Session;

/**
 * DAO Genérico para acceder a la base de datos
 * @author Ricardo
 * @param <T> 
 */
public class GenericoDAO<T> implements IGenericoDAO<T>, Serializable {

    private Session sesion;

    private void iniciaSesion() {
        sesion = HibernateUtil.getSessionFactory().openSession();
        sesion.getTransaction().begin();
    }

    private void cierraSesion() {
        sesion.getTransaction().commit();
        sesion.close();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        sesion.getTransaction().rollback();
        throw he;
    }

    @Override
    public void add(T objeto) {
        try {
            iniciaSesion();
            sesion.save(objeto);
            sesion.flush();

        } catch (HibernateException he) {
            manejaExcepcion(he);
        }finally {
            cierraSesion();
        }
    }

    @Override
    public <T> List<T> get(String entidad) {
        List<T> listadoResultados = null;
        try {
            iniciaSesion();
            listadoResultados = sesion.createQuery(" from " + entidad).list();
        } catch (HibernateException he) {
            this.manejaExcepcion(he);
        } finally {
            this.cierraSesion();
        }
        return listadoResultados;
    }

    @Override
    public <T> T getOne(Serializable pk, Class<T> claseEntidad) {
        T objetoRecuperado = null;

        try {
            iniciaSesion();
            objetoRecuperado = sesion.get(claseEntidad, pk);
        } catch (HibernateException he) {
            this.manejaExcepcion(he);
        } finally {
            this.cierraSesion();
        }

        return objetoRecuperado;
    }

    @Override
    public void update(T objeto) {
        try {
            iniciaSesion();
            sesion.update(objeto);
        } catch (HibernateException he) {
            this.manejaExcepcion(he);
        } finally {
            this.cierraSesion();
        }
    }

    @Override
    public void delete(T objeto) {
        try {
            iniciaSesion();
            sesion.delete(objeto);
        } catch (HibernateException he) {
            this.manejaExcepcion(he);
        } finally {
            this.cierraSesion();
        }
    }

}
