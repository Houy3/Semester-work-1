import exceptions.DBException;
import exceptions.NotFoundException;
import exceptions.NotNullException;
import exceptions.NotUniqueException;
import jdbc.SimpleDataSource;
import models.*;
import repositories.Repository;
import repositories.RepositoryImpl;
import services.Service;
import services.ServiceImpl;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class Test {
    public static void main(String[] args) throws DBException, NotFoundException, NotUniqueException, NotNullException {

//        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
//        System.out.println("21232F297A57A5A743894A0E4A801FC3");
//        System.out.println(passwordEncryptor.encrypt("admin"));


        Properties properties = new Properties();
        try {
            properties.load(Test.class.getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        DataSource dataSource = new SimpleDataSource(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
        Repository repository = new RepositoryImpl(dataSource);
        Service service = new ServiceImpl(repository);



//        for (Class tClass : new Class[]{User.class, Timetable.class, Task.class, Note.class, Event.class, Period.class, User_Timetable.class}) {
//            try {
//                System.out.println(SQLGenerator.insert(tClass));
//                System.out.println(SQLGenerator.update(tClass));
//                System.out.println(SQLGenerator.delete(tClass));
//                System.out.println(SQLGenerator.selectById(tClass));
//                System.out.println(SQLGenerator.selectUniqueCheck(tClass));
//                System.out.println();
//            } catch (SQLGeneratorException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        System.out.println((new StringBuilder("")));


        User user = new User();
        user.setEmail("ockap2003@mail.ru");
        user.setPasswordHash("12345678");
        user.setNickname("Houy3");
        user.setAccessRights(User.AccessRights.REGULAR);
        service.add(user);

        Timetable timetable = new Timetable();
        timetable.setName("Учеба");
        service.add(timetable);

        User_Timetable user_timetable = new User_Timetable();
        user_timetable.setUserId(user.getId());
        user_timetable.setTimetableId(timetable.getId());
        user_timetable.setAccessRights(Timetable.AccessRights.OWNER);
        service.add(user_timetable);

        Note note = new Note();
        note.setName("Пара по терверу");
        note.setBody("а что тут придумать");
        note.setTimetableId(timetable.getId());
        note.setInArchive(false);
        service.add(note);

        Task task = new Task();
        task.setNoteId(note.getId());
        task.setNotificationStartTime(new Date(System.currentTimeMillis()));
        task.setDeadlineTime(new Date(System.currentTimeMillis() + 1000*60*30));
        service.add(task);

        Event event = new Event();
        event.setNoteId(note.getId());
        event.setPlace("1409 кабинет");
        service.add(event);

        Period period = new Period();
        period.setEventId(1L);
        period.setStartTime(new Date(System.currentTimeMillis()));
        period.setEndTime(new Date(System.currentTimeMillis() + 1000*60*30));
        period.setGroupId(1L);
        service.add(period);




    }

}
