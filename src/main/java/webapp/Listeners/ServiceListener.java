package webapp.Listeners;

import Service.Impl.UsersServiceImpl;
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
        UsersRepository usersRepository = new UsersRepositoryImpl(dataSource);
        UsersServiceImpl usersService = new UsersServiceImpl(usersRepository);

        sce.getServletContext().setAttribute("userService", usersService);

        //Validators

//        try {
//            properties.load(this.getClass().getResourceAsStream("/app.properties"));
//        } catch (IOException e) {
//            throw new IllegalArgumentException(e);
//        }
//        EmailValidator emailValidator = new EmailValidator(
//                properties.getProperty("emailValidator.regexp")
//        );
//        sce.getServletContext().setAttribute("emailValidator", emailValidator);
//
//        PasswordValidator passwordValidator;
//        try {
//            passwordValidator = new PasswordValidator(
//                    properties.getProperty("passwordValidator.regexp"),
//                    Integer.parseInt(properties.getProperty("passwordValidator.minLength")),
//                    Integer.parseInt(properties.getProperty("passwordValidator.maxLength"))
//            );
//        } catch (NumberFormatException e) {
//            throw new IllegalArgumentException("В app.properties криво проставлены значения (passValid)");
//        }
//        sce.getServletContext().setAttribute("passwordValidator", passwordValidator);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
