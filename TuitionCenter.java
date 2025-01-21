import java.util.*;

class TuitionCenter {
    private static Scanner scanner = new Scanner(System.in);

    static class User {
        String username;
        String password;

        User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    static class Admin extends User {
        Admin(String username, String password) {
            super(username, password);
        }
    }

    static class Teacher extends User {
        String subject;

        Teacher(String username, String password, String subject) {
            super(username, password);
            this.subject = subject;
        }
    }

    static class Student extends User {
        Student(String username, String password) {
            super(username, password);
        }
    }

    static class Schedule {
        String studentList;
        String teacherName;
        String subject;
        String time;
        String day;

        Schedule(String studentList, String teacherName, String subject, String time, String day) {
            this.studentList = studentList;
            this.teacherName = teacherName;
            this.subject = subject;
            this.time = time;
            this.day = day;
        }
    }

    static class Homework {
        String title;
        String description;

        Homework(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }

    private static List<Admin> admins = new ArrayList<>();
    private static List<Teacher> teachers = new ArrayList<>();
    private static List<Student> students = new ArrayList<>();
    private static List<Schedule> schedules = new ArrayList<>();
    private static Map<Teacher, List<Homework>> teacherHomework = new HashMap<>();
    private static Map<Teacher, Map<Student, Integer>> teacherAttendance = new HashMap<>();

    public static void main(String[] args) {
        // Sample admin for demonstration purposes
        admins.add(new Admin("admin", "password"));

        while (true) {
            System.out.println("Welcome to Tuition Center Management System");
            System.out.println("1. Admin Login\n2. Teacher Login\n3. Student Login\n4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> adminMenu();
                case 2 -> teacherMenu();
                case 3 -> studentMenu();
                case 4 -> {
                    System.out.println("Exiting system. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void adminMenu() {
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();

        Admin loggedInAdmin = admins.stream().filter(admin -> admin.username.equals(username) && admin.password.equals(password)).findFirst().orElse(null);

        if (loggedInAdmin != null) {
            System.out.println("Admin Login Successful!");
            while (true) {
                System.out.println("1. Input Teacher Details\n2. Input Student Details\n3. Input Schedule\n4. View Teacher List\n5. View Student List\n6. View Schedule List\n7. Logout");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> addTeacher();
                    case 2 -> addStudent();
                    case 3 -> addSchedule();
                    case 4 -> viewTeacherList();
                    case 5 -> viewStudentList();
                    case 6 -> viewScheduleList();
                    case 7 -> {
                        System.out.println("Logging out...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }

    private static void addTeacher() {
        System.out.print("Enter Teacher Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Teacher Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Subject: ");
        String subject = scanner.nextLine();

        teachers.add(new Teacher(username, password, subject));
        System.out.println("Teacher added successfully!");
    }

    private static void addStudent() {
        System.out.print("Enter Student Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Student Password: ");
        String password = scanner.nextLine();

        students.add(new Student(username, password));
        System.out.println("Student added successfully!");
    }

    private static void addSchedule() {
        System.out.print("Enter Teacher Name: ");
        String teacherName = scanner.nextLine();
        Teacher teacher = teachers.stream().filter(t -> t.username.equals(teacherName)).findFirst().orElse(null);
        if (teacher == null) {
            System.out.println("Invalid teacher name. Schedule not added.");
            return;
        }

        System.out.print("Enter Student List (comma-separated): ");
        String studentList = scanner.nextLine();
        String[] studentNames = studentList.split(",");
        boolean allStudentsValid = Arrays.stream(studentNames).allMatch(name -> students.stream().anyMatch(s -> s.username.equals(name.trim())));

        if (!allStudentsValid) {
            System.out.println("Invalid student name(s). Schedule not added.");
            return;
        }

        System.out.print("Enter Subject: ");
        String subject = scanner.nextLine();
        System.out.print("Enter Time: ");
        String time = scanner.nextLine();
        System.out.print("Enter Day: ");
        String day = scanner.nextLine();

        schedules.add(new Schedule(studentList, teacherName, subject, time, day));
        System.out.println("Schedule added successfully!");
    }

    private static void viewTeacherList() {
        System.out.println("Teacher List:");
        for (Teacher teacher : teachers) {
            System.out.println("- " + teacher.username + " (Subject: " + teacher.subject + ")");
        }
    }

    private static void viewStudentList() {
        System.out.println("Student List:");
        for (Student student : students) {
            System.out.println("- " + student.username);
        }
    }

    private static void viewScheduleList() {
        System.out.println("Schedule List:");
        for (Schedule schedule : schedules) {
            System.out.println(schedule.day + " " + schedule.time + " - " + schedule.subject + " (Teacher: " + schedule.teacherName + ", Students: " + schedule.studentList + ")");
        }
    }

    private static void teacherMenu() {
        System.out.print("Enter Teacher Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Teacher Password: ");
        String password = scanner.nextLine();

        Teacher loggedInTeacher = teachers.stream().filter(teacher -> teacher.username.equals(username) && teacher.password.equals(password)).findFirst().orElse(null);

        if (loggedInTeacher != null) {
            System.out.println("Teacher Login Successful!");
            while (true) {
                System.out.println("1. Manage Homework\n2. View Student List\n3. View Schedule\n4. Logout");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> manageHomework(loggedInTeacher);
                    case 2 -> viewStudentList();
                    case 3 -> viewTeacherSchedule(loggedInTeacher);
                    case 4 -> {
                        System.out.println("Logging out...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }

    private static void manageHomework(Teacher teacher) {
        System.out.println("1. Add Homework\n2. Delete Homework\n3. View Homework");
        int choice = scanner.nextInt();
        scanner.nextLine();

        List<Homework> homeworkList = teacherHomework.getOrDefault(teacher, new ArrayList<>());
        teacherHomework.put(teacher, homeworkList);

        switch (choice) {
            case 1 -> {
                System.out.print("Enter Homework Title: ");
                String title = scanner.nextLine();
                System.out.print("Enter Homework Description: ");
                String description = scanner.nextLine();

                homeworkList.add(new Homework(title, description));
                System.out.println("Homework added successfully!");
            }
            case 2 -> {
                System.out.println("Select Homework to Delete:");
                for (int i = 0; i < homeworkList.size(); i++) {
                    System.out.println((i + 1) + ". " + homeworkList.get(i).title);
                }
                int deleteIndex = scanner.nextInt() - 1;
                scanner.nextLine();

                if (deleteIndex >= 0 && deleteIndex < homeworkList.size()) {
                    homeworkList.remove(deleteIndex);
                    System.out.println("Homework deleted successfully!");
                } else {
                    System.out.println("Invalid index.");
                }
            }
            case 3 -> {
                System.out.println("Homework List:");
                for (Homework hw : homeworkList) {
                    System.out.println("- " + hw.title + ": " + hw.description);
                }
            }
            default -> System.out.println("Invalid option. Try again.");
        }
    }

    private static void viewTeacherSchedule(Teacher teacher) {
        System.out.println("Schedule for " + teacher.username + ":");
        for (Schedule schedule : schedules) {
            if (schedule.teacherName.equals(teacher.username)) {
                System.out.println(schedule.day + " " + schedule.time + " - " + schedule.subject + " (" + schedule.studentList + ")");
            }
        }
    }

    private static void studentMenu() {
        System.out.print("Enter Student Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Student Password: ");
        String password = scanner.nextLine();

        Student loggedInStudent = students.stream().filter(student -> student.username.equals(username) && student.password.equals(password)).findFirst().orElse(null);

        if (loggedInStudent != null) {
            System.out.println("Student Login Successful!");
            while (true) {
                System.out.println("1. View Homework\n2. View Schedule\n3. Logout");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> viewStudentHomework(loggedInStudent);
                    case 2 -> viewStudentSchedule(loggedInStudent);
                    case 3 -> {
                        System.out.println("Logging out...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }

    private static void viewStudentHomework(Student student) {
        System.out.println("Homework:");
        teacherHomework.forEach((teacher, homeworkList) -> {
            for (Homework hw : homeworkList) {
                System.out.println("- " + hw.title + ": " + hw.description);
            }
        });
    }

    private static void viewStudentSchedule(Student student) {
        System.out.println("Schedule:");
        for (Schedule schedule : schedules) {
            if (Arrays.asList(schedule.studentList.split(",")).contains(student.username)) {
                System.out.println(schedule.day + " " + schedule.time + " - " + schedule.subject + " (Teacher: " + schedule.teacherName + ")");
            }
        }
    }
}
