package webapp.Listeners;

import services.Impl.UsersServiceImpl;
import jdbc.SimpleDataSource;
import repositories.Impl.UsersRepositoryImpl;
import repositories.UsersRepository;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@WebListener
public class ServiceListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        //userService
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("/db.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        DataSource dataSource = new SimpleDataSource(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
//        UsersRepository usersRepository = new UsersRepositoryImpl(dataSource);
//        UsersServiceImpl usersService = new UsersServiceImpl(usersRepository);

//        sce.getServletContext().setAttribute("userService", usersService);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
