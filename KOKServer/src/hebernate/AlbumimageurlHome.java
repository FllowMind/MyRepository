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
 * Home object for domain model class Albumimageurl.
 * @see hebernate.Albumimageurl
 * @author Hibernate Tools
 */
public class AlbumimageurlHome {

	private static final Log log = LogFactory.getLog(AlbumimageurlHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Albumimageurl transientInstance) {
		log.debug("persisting Albumimageurl instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Albumimageurl instance) {
		log.debug("attaching dirty Albumimageurl instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Albumimageurl instance) {
		log.debug("attaching clean Albumimageurl instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Albumimageurl persistentInstance) {
		log.debug("deleting Albumimageurl instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Albumimageurl merge(Albumimageurl detachedInstance) {
		log.debug("merging Albumimageurl instance");
		try {
			Albumimageurl result = (Albumimageurl) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Albumimageurl findById(int id) {
		log.debug("getting Albumimageurl instance with id: " + id);
		try {
			Albumimageurl instance = (Albumimageurl) sessionFactory.getCurrentSession().get("hebernate.Albumimageurl",
					id);
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

	public List<Albumimageurl> findByExample(Albumimageurl instance) {
		log.debug("finding Albumimageurl instance by example");
		try {
			List<Albumimageurl> results = (List<Albumimageurl>) sessionFactory.getCurrentSession()
					.createCriteria("hebernate.Albumimageurl").add(create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
