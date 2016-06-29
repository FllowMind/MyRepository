package hebernate;
// Generated 2016-6-27 19:09:29 by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class Musicsetinfo.
 * @see hebernate.Musicsetinfo
 * @author Hibernate Tools
 */
public class MusicsetinfoHome {

	private static final Log log = LogFactory.getLog(MusicsetinfoHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Musicsetinfo transientInstance) {
		log.debug("persisting Musicsetinfo instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Musicsetinfo instance) {
		log.debug("attaching dirty Musicsetinfo instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Musicsetinfo instance) {
		log.debug("attaching clean Musicsetinfo instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Musicsetinfo persistentInstance) {
		log.debug("deleting Musicsetinfo instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Musicsetinfo merge(Musicsetinfo detachedInstance) {
		log.debug("merging Musicsetinfo instance");
		try {
			Musicsetinfo result = (Musicsetinfo) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Musicsetinfo findById(java.lang.String id) {
		log.debug("getting Musicsetinfo instance with id: " + id);
		try {
			Musicsetinfo instance = (Musicsetinfo) sessionFactory.getCurrentSession().get("hebernate.Musicsetinfo", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Musicsetinfo> findByExample(Musicsetinfo instance) {
		log.debug("finding Musicsetinfo instance by example");
		try {
			List<Musicsetinfo> results = (List<Musicsetinfo>) sessionFactory.getCurrentSession()
					.createCriteria("hebernate.Musicsetinfo").add(create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
