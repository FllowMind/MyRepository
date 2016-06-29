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
 * Home object for domain model class Musicinfo.
 * @see hebernate.Musicinfo
 * @author Hibernate Tools
 */
public class MusicinfoHome {

	private static final Log log = LogFactory.getLog(MusicinfoHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Musicinfo transientInstance) {
		log.debug("persisting Musicinfo instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Musicinfo instance) {
		log.debug("attaching dirty Musicinfo instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Musicinfo instance) {
		log.debug("attaching clean Musicinfo instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Musicinfo persistentInstance) {
		log.debug("deleting Musicinfo instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Musicinfo merge(Musicinfo detachedInstance) {
		log.debug("merging Musicinfo instance");
		try {
			Musicinfo result = (Musicinfo) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Musicinfo findById(hebernate.MusicinfoId id) {
		log.debug("getting Musicinfo instance with id: " + id);
		try {
			Musicinfo instance = (Musicinfo) sessionFactory.getCurrentSession().get("hebernate.Musicinfo", id);
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

	public List<Musicinfo> findByExample(Musicinfo instance) {
		log.debug("finding Musicinfo instance by example");
		try {
			List<Musicinfo> results = (List<Musicinfo>) sessionFactory.getCurrentSession()
					.createCriteria("hebernate.Musicinfo").add(create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
