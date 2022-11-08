package webapp;

public record Constants() {

    //сервисы
    public static final String USERS_SERVICE = "usersService";
    public static final String TIMETABLES_SERVICE  = "timetablesService";
    public static final String NOTES_SERVICE = "notesService";
    public static final String TASKS_SERVICE = "tasksService";
    public static final String PERIODS_SERVICE = "periodsService";
    public static final String EVENTS_SERVICE = "eventsService";

    //для сервлетов
    public static final String USER = "user";
    public static final String CHOSEN_TIMETABLES = "chosenTimetables";
    public static final String EMAIL_VALIDATOR = "emailValidator";
    public static final String PASSWORD_VALIDATOR = "passwordValidator";
    public static final String PASSWORD_ENCRYPTOR= "passwordEncryptor";
    public static final String NICKNAME_VALIDATOR = "nicknameValidator";
    public static final String SPLIT_CHAR = "A";



    //пути
    public static final String SIGN_IN = "/signIn";
    public static final String SIGN_UP = "/signUp";
    public static final String SIGN_OUT = "/signOut";

    public static final String TIMETABLE = "/timetable";
    public static final String CHOOSE_TIMETABLES = "/chooseTimetables";

}
