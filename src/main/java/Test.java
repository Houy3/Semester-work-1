import exceptions.*;
import models.encryptors.PasswordEncryptor;
import repositories.Inter.*;
import repositories.Impl.*;
import repositories.SimpleDataSource;
import services.Impl.*;
import services.Inter.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Test {
    public static void main(String[] args) throws DBException, NotFoundException, NotUniqueException, NullException, ServiceException, ParseException {

        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
//        System.out.println("21232F297A57A5A743894A0E4A801FC3");
        System.out.println(passwordEncryptor.encrypt("admin"));

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

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
        UsersRepository usersRepository = new UsersRepositoryImpl(dataSource);
        UsersService usersService = new UsersServiceImpl(usersRepository);

        TimetablesRepository timetablesRepository = new TimetablesRepositoryImpl(dataSource);
        TimetablesService timetablesService = new TimetablesServiceImpl(timetablesRepository);

        NotesRepository notesRepository = new NotesRepositoryImpl(dataSource);
        NotesService notesService = new NotesServiceImpl(notesRepository);

        TasksRepository tasksRepository = new TasksRepositoryImpl(dataSource);
        TasksService tasksService = new TasksServiceImpl(tasksRepository);

        PeriodsRepository periodsRepository = new PeriodsRepositoryImpl(dataSource);
        PeriodService periodService = new PeriodServiceImpl(periodsRepository);

        EventsRepository eventsRepository = new EventsRepositoryImpl(dataSource);
        EventsService eventsService = new EventsServiceImpl(eventsRepository, periodService);


//        String fieldName ="id";// new String[]{"id", "noteId", "eventId", "userId"};
//       // for (String fieldName : fieldsName) {
//            for (Class tClass : new Class[]{User.class, Timetable.class, Task.class, Note.class, Event.class, Period.class, UserTimetable.class}) {
//                try {
//                    System.out.println();
//                    System.out.println(SQLGenerator.insert(tClass));
//                    System.out.println(SQLGenerator.update(tClass, tClass.getDeclaredField(fieldName)));
////                    //SQLGenerator.update(tClass, tClass.getDeclaredField(fieldName)).getUniqueFields().forEach(System.out::println);
//                    System.out.println(SQLGenerator.delete(tClass, tClass.getDeclaredField(fieldName)));
//                    System.out.println(SQLGenerator.selectByUniqueField(tClass, tClass.getDeclaredField(fieldName)));
//                    System.out.println(SQLGenerator.selectUniqueCheck(tClass, Optional.empty()));
//                    System.out.println(SQLGenerator.selectUniqueCheck(tClass, Optional.of(tClass.getDeclaredField(fieldName))));
//                } catch (SQLGeneratorException e) {
//                    System.out.println(e.getMessage());
//                } catch (NoSuchFieldException e) {
//                    System.out.println("--------");
//                }
//            }
        //}
//        System.out.println((new StringBuilder("")));


//        User user = new User();
//        user.setEmail("ockap20030408@gmail.ru");
//        user.setPasswordHash("12345");
//        user.setNickname("Houy3");
//        user.setAccessRights(User.AccessRights.REGULAR);
//        usersService.add(user);
//        System.out.println(user);
//
//        Timetable timetable = new Timetable();
//        timetable.setName("НЕ учеба");
//        timetablesService.add(timetable);
//        System.out.println(timetable);
//
//        UserTimetable user_timetable = new UserTimetable();
//        user_timetable.setUserId(user.getId());
//        user_timetable.setTimetableId(timetable.getId());
//        user_timetable.setAccessRights(Timetable.AccessRights.OWNER);
//        timetablesService.add(user_timetable);
//        System.out.println(user_timetable);
//
//
//        Note note = new Note();
//        note.setName("Пара по терверу");
//        note.setBody("а что тут придумать");
//        note.setTimetableId(timetable.getId());
//        note.setInArchive(false);
//        notesService.add(note);
//        System.out.println(note);
//
//        Task task = new Task();
//        task.setNoteId(note.getId());
//        task.setNotificationStartDate(dateFormat.parse("08.11.2022 00:00:00"));
//        task.setDeadlineTime(dateFormat.parse("12.11.2022 00:00:00"));
//        task.setDone(false);
//        tasksService.add(task);
//        System.out.println(task);
//
//        Event event = new Event();
//        event.setNoteId(note.getId());
//        event.setPlace("1409 кабинет");
//
//        Period period = new Period();
//        period.setEventId(1L);
//        period.setStartTime(dateFormat.parse("08.11.2022 18:00:00"));
//        period.setEndTime(dateFormat.parse("09.11.2022 00:00:00"));
//        period.setRepeatability(Period.Repeatability.DAY);
//        period.setRepeatabilityEndTime(dateFormat.parse("12.11.2022 19:00:00"));
////        periodService.add(period);
//        event.addPeriod(period);
////
//        eventsService.add(event);
//        System.out.println(event);
//        System.out.println(period);





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

//        Map<Timetable, Timetable.AccessRights> timetables = timetablesService.getTimetablesForUserId(10L);
//        timetables.keySet().stream()
//                .filter(t -> timetables.get(t).equals(Timetable.AccessRights.OWNER))
//                .toList();
//
//        String v = "12";
//        String[] vv = v.split("\\.");
//        System.out.println(vv[0]);
//        Arrays.stream(v.split(SPLIT_CHAR)).map(Long::parseLong).toList();//.forEach(System.out::println);
////        Map<Timetable, Timetable.AccessRights> tt = timetablesService.getTimetablesForUserId(2L);
//
//        for (Timetable timetable : tt.keySet()) {
//            System.out.println(timetable + " " + tt.get(timetable));
////        }
//
//        try {
//            System.out.println(UserTimetable.class.getDeclaredField("userId").equals(UserTimetable.class.getDeclaredField("userId")));
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        }

//        List<String> attributes = Arrays.stream(SignInPage.Parameters.values()).map(Enum::name).toList();
//        attributes.add(ERROR_MESSAGE_NAME.name());
//        attributes.forEach(System.out::println);
    }
}
