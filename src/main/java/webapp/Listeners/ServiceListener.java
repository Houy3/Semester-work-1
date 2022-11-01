package webapp.Listeners;

import jdbc.SimpleDataSource;
import models.encryptors.Encryptor;
import models.encryptors.PasswordEncryptor;
import models.validators.EmailValidator;
import models.validators.NicknameValidator;
import models.validators.PasswordValidator;
import models.validators.Validator;
import repositories.Repository;
import repositories.RepositoryImpl;
import services.Service;
import services.ServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

@WebListener
public class ServiceListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        //userService
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        DataSource dataSource = new SimpleDataSource(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
        Repository repository = new RepositoryImpl(dataSource);
        Service usersService = new ServiceImpl(repository);

        sce.getServletContext().setAttribute("service", usersService);


        final Validator<String> emailValidator = new EmailValidator(properties.getProperty("email.regexp"));
        sce.getServletContext().setAttribute("emailValidator", emailValidator);

        try {
            final Validator<String> passwordValidator = new PasswordValidator(
                    properties.getProperty("password.regexp"),
                    Integer.parseInt(properties.getProperty("password.minLength")),
                    Integer.parseInt(properties.getProperty("password.maxLength"))
            );
            sce.getServletContext().setAttribute("passwordValidator", passwordValidator);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Неверный формат длины пароля");
        }

        final Encryptor<String> passwordEncryptor = new PasswordEncryptor();
        sce.getServletContext().setAttribute("passwordEncryptor", passwordEncryptor);

        final Validator<String> nicknameValidator = new NicknameValidator(properties.getProperty("nickname.regexp"));
        sce.getServletContext().setAttribute("nicknameValidator", nicknameValidator);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
