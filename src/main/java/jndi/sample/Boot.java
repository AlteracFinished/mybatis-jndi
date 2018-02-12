package jndi.sample;


import org.apache.catalina.core.ApplicationContextFacade;
import org.apache.catalina.core.StandardContext;
import org.apache.ibatis.datasource.jndi.JndiDataSourceFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.util.Properties;


@WebListener
public class Boot implements ServletContextListener {

    private static Logger logger = LoggerFactory.getLogger(Boot.class);

    //@Resource(name = "jdbc/datasource")
    private static DataSource dataSource;

    //@Resource(name = "jdbc/sqlSessionFactory")
    private static SqlSessionFactory sqlSessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDefaultAutoCommit(true);
        dataSource.setDriver(System.getenv("JDBC.Driver"));
        dataSource.setUrl(System.getenv("JDBC.ConnectionURL"));
        dataSource.setUsername(System.getenv("JDBC.Username"));
        dataSource.setPassword(System.getenv("JDBC.Password"));
        dataSource.setPoolMaximumIdleConnections(30);
        dataSource.setPoolMaximumActiveConnections(100);
        dataSource.setPoolMaximumCheckoutTime(10000);
        Boot.dataSource=dataSource;
        JdbcTransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("dev",transactionFactory,dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMappers("jndi.sample");
        Boot.sqlSessionFactory= new SqlSessionFactoryBuilder().build(configuration);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
