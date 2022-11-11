package webapp.Listeners;

import repositories.Inter.*;
import models.encryptors.Encryptor;
import models.encryptors.PasswordEncryptor;
import models.validators.EmailValidator;
import models.validators.NicknameValidator;
import models.validators.PasswordValidator;
import models.validators.Validator;
import repositories.Impl.*;
import repositories.SimpleDataSource;
import services.Impl.*;
import services.Inter.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

import static webapp.Utils.Constants.*;

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
        sce.getServletContext().setAttribute(USERS_SERVICE.name(), usersService);

        TimetablesRepository timetablesRepository = new TimetablesRepositoryImpl(dataSource);
        TimetablesService timetablesService = new TimetablesServiceImpl(timetablesRepository);
        sce.getServletContext().setAttribute(TIMETABLES_SERVICE.name(), timetablesService);

        NotesRepository notesRepository = new NotesRepositoryImpl(dataSource);
        NotesService notesService = new NotesServiceImpl(notesRepository);
        sce.getServletContext().setAttribute(NOTES_SERVICE.name(), notesService);

        TasksRepository tasksRepository = new TasksRepositoryImpl(dataSource);
        TasksService tasksService = new TasksServiceImpl(tasksRepository);
        sce.getServletContext().setAttribute(TASKS_SERVICE.name(), tasksService);

        PeriodsRepository periodsRepository = new PeriodsRepositoryImpl(dataSource);
        PeriodService periodService = new PeriodServiceImpl(periodsRepository);
        sce.getServletContext().setAttribute(PERIODS_SERVICE.name(), periodService);

        EventsRepository eventsRepository = new EventsRepositoryImpl(dataSource);
        EventsService eventsService = new EventsServiceImpl(eventsRepository, periodService);
        sce.getServletContext().setAttribute(EVENTS_SERVICE.name(), eventsService);


        final Validator<String> emailValidator = new EmailValidator(properties.getProperty("email.regexp"));
        sce.getServletContext().setAttribute(EMAIL_VALIDATOR.name(), emailValidator);

        try {
            final Validator<String> passwordValidator = new PasswordValidator(
                    properties.getProperty("password.regexp"),
                    Integer.parseInt(properties.getProperty("password.minLength")),
                    Integer.parseInt(properties.getProperty("password.maxLength"))
            );
            sce.getServletContext().setAttribute(PASSWORD_VALIDATOR.name(), passwordValidator);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Wrong format of options.");
        }

        final Encryptor<String> passwordEncryptor = new PasswordEncryptor();
        sce.getServletContext().setAttribute(PASSWORD_ENCRYPTOR.name(), passwordEncryptor);

        final Validator<String> nicknameValidator = new NicknameValidator(properties.getProperty("nickname.regexp"));
        sce.getServletContext().setAttribute(NICKNAME_VALIDATOR.name(), nicknameValidator);


        sce.getServletContext().setAttribute(DATE_FORMAT.name(), "dd.MM.yyyy HH:mm:ss");
        sce.getServletContext().setAttribute(HTML_DATE_FORMAT.name(), "yyyy-MM-dd");
        sce.getServletContext().setAttribute(HTML_TIME_FORMAT.name(), "HH:mm");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
