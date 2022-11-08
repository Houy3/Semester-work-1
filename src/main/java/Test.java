import exceptions.*;
import jdbc.SimpleDataSource;
import models.*;
import repositories.*;
import repositories.Impl.*;
import services.*;
import services.Impl.*;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

import static webapp.Constants.SPLIT_CHAR;

public class Test {
    public static void main(String[] args) throws DBException, NotFoundException, NotUniqueException, NullException, ServiceException {

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
//        UsersRepository usersRepository = new UsersRepositoryImpl(dataSource);
//        UsersService usersService = new UsersServiceImpl(usersRepository);
//
        TimetablesRepository timetablesRepository = new TimetablesRepositoryImpl(dataSource);
        TimetablesService timetablesService = new TimetablesServiceImpl(timetablesRepository);

//        NotesRepository notesRepository = new NotesRepositoryImpl(dataSource);
//        NotesService notesService = new NotesServiceImpl(notesRepository);
//
//        TasksRepository tasksRepository = new TasksRepositoryImpl(dataSource);
//        TasksService tasksService = new TasksServiceImpl(tasksRepository);
//
//        PeriodsRepository periodsRepository = new PeriodsRepositoryImpl(dataSource);
//        PeriodService periodService = new PeriodServiceImpl(periodsRepository);
//
//        EventsRepository eventsRepository = new EventsRepositoryImpl(dataSource);
//        EventsService eventsService = new EventsServiceImpl(eventsRepository, periodService);


//        String fieldName ="email";// new String[]{"id", "noteId", "eventId", "userId"};
//       // for (String fieldName : fieldsName) {
//            for (Class tClass : new Class[]{User.class, Timetable.class, Task.class, Note.class, Event.class, Period.class, User_Timetable.class}) {
//                try {
//                    System.out.println();
//                    System.out.println(SQLGenerator.insert(tClass));
//                    System.out.println(SQLGenerator.update(tClass, tClass.getDeclaredField(fieldName)));
//                    System.out.println(SQLGenerator.delete(tClass, tClass.getDeclaredField(fieldName)));
//                    System.out.println(SQLGenerator.selectByUniqueField(tClass, tClass.getDeclaredField(fieldName)));
//                    System.out.println(SQLGenerator.selectUniqueCheck(tClass));
//                } catch (SQLGeneratorException | NoSuchFieldException e) {
//                    //System.out.println(e.getMessage());
//                }
//            }
//        //}
//        System.out.println((new StringBuilder("")));


//        User user = new User();
//        user.setEmail("ockap2003@mail.ru");
////        user.setPasswordHash("12345678");
////        user.setNickname("Houy3");
////        user.setAccessRights(User.AccessRights.REGULAR);
////        user.setId(1L);
//        service.getByUniqueField(user, "email");
//        System.out.println(user);
//
//        Timetable timetable = new Timetable();
//        timetable.setName("Учеба");
//        service.add(timetable);
//
//        UserTimetable user_timetable = new UserTimetable();
//        user_timetable.setUserId(2L);
//        user_timetable.setTimetableId(2L);
//      //  user_timetable.setAccessRights(Timetable.AccessRights.OWNER);
//        service.delete(user_timetable, "usfgerId");
//        System.out.println(user_timetable);

//
//        Note note = new Note();
//        note.setName("Пара по терверу");
//        note.setBody("а что тут придумать");
//        note.setTimetableId(timetable.getId());
//        note.setInArchive(false);
//        service.add(note);
//
//        Task task = new Task();
//        task.setNoteId(note.getId());
//        task.setNotificationStartTime(new Date(System.currentTimeMillis()));
//        task.setDeadlineTime(new Date(System.currentTimeMillis() + 1000*60*30));
//        service.add(task);
//
//        Event event = new Event();
//        event.setNoteId(note.getId());
//        event.setPlace("1409 кабинет");
//        service.add(event);
//
//        Period period = new Period();
//        period.setEventId(1L);
//        period.setStartTime(new Date(System.currentTimeMillis()));
//        period.setEndTime(new Date(System.currentTimeMillis() + 1000*60*30));
//        period.setGroupId(1L);
//        service.add(period);

//        Map<Timetable, Timetable.AccessRights> timetables = timetablesService.getTimetablesForUserId(2L);
//
//        for (Timetable timetable : timetables.keySet()) {
//            System.out.println(timetable + " " + timetables.get(timetable));
//        }
//
//        List<Note> notes = notesService.getNotesByTimetableId(1L);
//        for (Note note : notes) {
//            System.out.println(note);
//        }

//        Timestamp timestamp = new Timestamp((new Date()).getTime());
//        System.out.println(new Date());

//        List<Task> tasks = tasksService.getTasksByTimetableIdAndDate(1L, new Date());
//        for (Task task : tasks) {
//            System.out.println(task);
//        }

//        List<Event> events = eventsService.getEventsByTimetableIdAndDate(1L, new Date());
//        for (Event event : events) {
//            System.out.println(event);
//            for (Period period : event.getPeriods()){
//                System.out.println("\t" + period);
//            }
//        }


//        Period period = new Period();
//        period.setEventId(1L);
//        period.setStartTime(Calendar.getInstance().getTime());
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.add(Calendar.HOUR,2);
//        period.setEndTime(calendar1.getTime());
//        period.setRepeatability(Period.Repeatability.DAY);
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.set(2022, 10, 8);
//        period.setRepeatabilityEndTime(calendar2.getTime());
//        periodService.add(period);


        String v = "12";
        String[] vv = v.split("\\.");
        System.out.println(vv[0]);
        Arrays.stream(v.split(SPLIT_CHAR)).map(Long::parseLong).toList();//.forEach(System.out::println);
//        Map<Timetable, Timetable.AccessRights> tt = timetablesService.getTimetablesForUserId(2L);
//
//        for (Timetable timetable : tt.keySet()) {
//            System.out.println(timetable + " " + tt.get(timetable));
//        }
    }
}
