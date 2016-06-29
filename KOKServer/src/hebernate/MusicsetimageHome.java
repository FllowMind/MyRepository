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
 * Home object for domain model class Musicsetimage.
 * @see hebernate.Musicsetimage
 * @author Hibernate Tools
 */
public class MusicsetimageHome {

	private static final Log log = LogFactory.getLog(MusicsetimageHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Musicsetimage transientInstance) {
		log.debug("persisting Musicsetimage instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Musicsetimage instance) {
		log.debug("attaching dirty Musicsetimage instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Musicsetimage instance) {
		log.debug("attaching clean Musicsetimage instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Musicsetimage persistentInstance) {
		log.debug("deleting Musicsetimage instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Musicsetimage merge(Musicsetimage detachedInstance) {
		log.debug("merging Musicsetimage instance");
		try {
			Musicsetimage result = (Musicsetimage) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Musicsetimage findById(java.lang.String id) {
		log.debug("getting Musicsetimage instance with id: " + id);
		try {
			Musicsetimage instance = (Musicsetimage) sessionFactory.getCurrentSession().get("hebernate.Musicsetimage",
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

	public List<Musicsetimage> findByExample(Musicsetimage instance) {
		log.debug("finding Musicsetimage instance by example");
		try {
			List<Musicsetimage> results = (List<Musicsetimage>) sessionFactory.getCurrentSession()
					.createCriteria("hebernate.Musicsetimage").add(create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
