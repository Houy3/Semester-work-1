package webapp.Listeners;

import jdbc.SimpleDataSource;
import models.encryptors.Encryptor;
import models.encryptors.PasswordEncryptor;
import models.validators.EmailValidator;
import models.validators.NicknameValidator;
import models.validators.PasswordValidator;
import models.validators.Validator;
import repositories.*;
import repositories.Impl.*;
import services.*;
import services.Impl.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

import static webapp.Constants.*;

@WebListener
public class ServiceListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        Properties properties = new Properties();
        try {
            properties.load(ServiceListener.class.getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        DataSource dataSource = new SimpleDataSource(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
        UsersRepository usersRepository = new UsersRepositoryImpl(dataSource);
        UsersService usersService = new UsersServiceImpl(usersRepository);
        sce.getServletContext().setAttribute(USERS_SERVICE, usersService);

        TimetablesRepository timetablesRepository = new TimetablesRepositoryImpl(dataSource);
        TimetablesService timetablesService = new TimetablesServiceImpl(timetablesRepository);
        sce.getServletContext().setAttribute(TIMETABLES_SERVICE, timetablesService);

        NotesRepository notesRepository = new NotesRepositoryImpl(dataSource);
        NotesService notesService = new NotesServiceImpl(notesRepository);
        sce.getServletContext().setAttribute(NOTES_SERVICE, notesService);

        TasksRepository tasksRepository = new TasksRepositoryImpl(dataSource);
        TasksService tasksService = new TasksServiceImpl(tasksRepository);
        sce.getServletContext().setAttribute(TASKS_SERVICE, tasksService);

        PeriodsRepository periodsRepository = new PeriodsRepositoryImpl(dataSource);
        PeriodService periodService = new PeriodServiceImpl(periodsRepository);
        sce.getServletContext().setAttribute(PERIODS_SERVICE, periodService);

        EventsRepository eventsRepository = new EventsRepositoryImpl(dataSource);
        EventsService eventsService = new EventsServiceImpl(eventsRepository, periodService);
        sce.getServletContext().setAttribute(EVENTS_SERVICE, eventsService);


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
