package cz.muni.fi.pv168.web;

import org.apache.commons.dbcp2.BasicDataSource;


import cz.muni.fi.pv168.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebListener
public class StartListener implements ServletContextListener {

    final static Logger log = LoggerFactory.getLogger(StartListener.class);

    @Override
    public void contextInitialized(ServletContextEvent ev) {
        log.info("webová aplikace inicializována");
        ServletContext servletContext = ev.getServletContext();
        DataSource ds = null;
        try {
            ds = dataSource();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CarManager carManager = new CarManagerImpl(ds);
        CustomerManager customerManager = new CustomerManagerImpl(ds);
        LeaseManager leaseManager = new LeaseManagerImpl(ds);
        //ApplicationContext springContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        servletContext.setAttribute("customerManager", customerManager);
        servletContext.setAttribute("carManager", carManager);
        servletContext.setAttribute("leaseManager", leaseManager);
        log.info("vytvořeny manažery");
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        log.info("aplikace končí");
    }

    public DataSource dataSource() throws IOException {
        Properties p = new Properties();
        InputStream in;
        in = StartListener.class.getResourceAsStream("/myconf.properties");
        p.load(in);
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(p.getProperty("jdbc.driver"));
        ds.setUrl(p.getProperty("jdbc.url"));
        ds.setUsername(p.getProperty("jdbc.user"));
        ds.setPassword(p.getProperty("jdbc.password"));
        return ds;
    }
}