package management;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@Named
@ApplicationScoped
public class Agent implements ServletContextListener {

    @Inject
    private MeanDistanceBean MeanDistanceBean;
    @Inject
    private CountBean CountBean;

    @PostConstruct
    public void contextInitialized(ServletContextEvent sce) {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName mBean1;
        try {
            mBean1 = new ObjectName("Agent:name=MeanDistanceAgent");
            if (!mbs.isRegistered(mBean1)) {
                mbs.registerMBean(MeanDistanceBean, mBean1);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        ObjectName mBean2;
        try {
            mBean2 = new ObjectName("Agent:name=CountAgent");
            if (!mbs.isRegistered(mBean2)) {
                mbs.registerMBean(CountBean, mBean2);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}