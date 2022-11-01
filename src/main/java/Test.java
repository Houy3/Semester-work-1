import Exceptions.DBException;
import Exceptions.NotFoundException;
import Exceptions.NotUniqueException;
import Exceptions.SQLGeneratorException;
import jdbc.SQLAnnotations.NotNull;
import jdbc.SQLGenerator;
import jdbc.SimpleDataSource;
import models.Note;
import models.Task;
import models.Timetable;
import models.User;
import repositories.*;
import repositories.Impl.NotesRepositoryImpl;
import repositories.Impl.TasksRepositoryImpl;
import repositories.Impl.TimetablesRepositoryImpl;
import repositories.Impl.UsersRepositoryImpl;
import services.Impl.NoteServiceImpl;
import services.Impl.TaskServiceImpl;
import services.Impl.TimetablesServiceImpl;
import services.Impl.UsersServiceImpl;
import services.NotesService;
import services.TasksService;
import services.TimetablesService;
import services.UsersService;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

public class Test {
    public static void main(String[] args) throws DBException, NotFoundException, NotUniqueException {

//        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
//        System.out.println("21232F297A57A5A743894A0E4A801FC3");
//        System.out.println(passwordEncryptor.encrypt("admin"));


        Properties properties = new Properties();
        try {
            properties.load(Test.class.getResourceAsStream("/db.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        DataSource dataSource = new SimpleDataSource(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
        AbstractRepository<User> usersRepository = new UsersRepositoryImpl(dataSource);
        UsersService usersService = new UsersServiceImpl(usersRepository);

        AbstractRepository<Timetable> timetablesRepository = new TimetablesRepositoryImpl(dataSource);
        TimetablesService timetablesService = new TimetablesServiceImpl(timetablesRepository);

        AbstractRepository<Note> noteRepository = new NotesRepositoryImpl(dataSource);
        NotesService noteService = new NoteServiceImpl(noteRepository);
//
        AbstractRepository<Task> tasksRepository = new TasksRepositoryImpl(dataSource);
        TasksService tasksService = new TaskServiceImpl(tasksRepository);

//
//        List<User> users = usersService.getAllUsers();
//        for (User _user : users) {
//            System.out.println(_user);
//        }
//        System.out.println("------------------");
//
//        User user = new User();
//        user.setId(2L);
//        user.setEmail("ockap2003@mail.ru");
//        user.setName("Oscar");
//        user.setSurname("Fattakhov");
//        user.setPassword("sdosnskcd");
//        user.setNickname("Houy3");
//        user.setAccessRights(User.AccessRights.ADMIN);
//        usersService.add(user);
//
//        users = usersService.getAllUsers();
//        for (User _user : users) {
//            System.out.println(_user);
//        }
//
//
//        User user = new User();
//        user.setId(1L);
////        usersService.getById(user);
////        System.out.println(user);
////
//        Timetable timetable = new Timetable();
//        timetable.setId(1L);
//        timetable.setName("Работа");
// //       timetablesService.add(timetable);
//   //     timetablesService.addAccessRights(timetable, user, Timetable.AccessRights.OWNER);
////
////        timetablesService.getTimetableById(timetable);
////        timetablesService.changeAccessRights(timetable, user, Timetable.AccessRights.READER);
////
////        //usersService.delete(user);
////        timetablesService.delete(timetable);
////
//        Note note = new Note();
//        note.setName("Надо купить");
//        note.setTimetableId(2L);
//        note.setBody("Чипсы");
//        note.setInArchive(false);
//
//        noteService.add(note);



//        Date calendar = new Date();
//
//        Task task = new Task();
//        task.setNoteId(3L);
//        task.setNotificationsStartTime(calendar);
//        task.setDeadlineTime(calendar);
//
//        tasksService.delete(task);
//        tasksService.add(task);
//        calendar.setHours(12);
//        //tasksService.change(task);
//        tasksService.getById(task);
//        System.out.println(calendar.getHours());

//        for (Class tClass : new Class[]{User.class, Timetable.class, Task.class, Note.class}) {
//            try {
//                System.out.println(SQLGenerator.insert(tClass).getQuery());
//                System.out.println(SQLGenerator.update(tClass).getQuery());
//                System.out.println(SQLGenerator.delete(tClass).getQuery());
//                System.out.println(SQLGenerator.selectById(tClass).getQuery());
//                System.out.println();
//            } catch (SQLGeneratorException e) {
//                throw new RuntimeException(e);
//            }
//        }

//        Timetable timetable = new Timetable();
//        timetable.setName("Учеба");
//
//        timetablesRepository.insert(timetable);


        User user = new User();
        user.setId(2L);

        Timetable timetable = new Timetable();
        timetable.setId(3L);

        Note note = new Note();
        note.setId(5L);
        note.setName("зачет");
        note.setBody("по инфе (Ференец)");
        note.setTimetableId(timetable.getId());
        note.setInArchive(true);

        noteRepository.update(note);


//        usersRepository.insert(user);
//        //usersService.add(user);





    }
}
