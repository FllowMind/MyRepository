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
 * Home object for domain model class Artistimageurl.
 * @see hebernate.Artistimageurl
 * @author Hibernate Tools
 */
public class ArtistimageurlHome {

	private static final Log log = LogFactory.getLog(ArtistimageurlHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Artistimageurl transientInstance) {
		log.debug("persisting Artistimageurl instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Artistimageurl instance) {
		log.debug("attaching dirty Artistimageurl instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Artistimageurl instance) {
		log.debug("attaching clean Artistimageurl instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Artistimageurl persistentInstance) {
		log.debug("deleting Artistimageurl instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Artistimageurl merge(Artistimageurl detachedInstance) {
		log.debug("merging Artistimageurl instance");
		try {
			Artistimageurl result = (Artistimageurl) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Artistimageurl findById(java.lang.String id) {
		log.debug("getting Artistimageurl instance with id: " + id);
		try {
			Artistimageurl instance = (Artistimageurl) sessionFactory.getCurrentSession()
					.get("hebernate.Artistimageurl", id);
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

	public List<Artistimageurl> findByExample(Artistimageurl instance) {
		log.debug("finding Artistimageurl instance by example");
		try {
			List<Artistimageurl> results = (List<Artistimageurl>) sessionFactory.getCurrentSession()
					.createCriteria("hebernate.Artistimageurl").add(create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
