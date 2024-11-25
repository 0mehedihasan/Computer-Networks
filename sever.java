package mehedi;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;

    // User credentials (username -> password)
    private static final Map<String, String> userCredentials = new HashMap<>();
    private static final Map<String, List<String>> studentCourses = new HashMap<>();
    private static final List<String> studentList = Collections.synchronizedList(new ArrayList<>());

    static {
        userCredentials.put("admin", "admin123");
        userCredentials.put("user1", "password1");
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private String loggedInUser = null;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                // Login Process
                while (loggedInUser == null) {
                    out.println("Enter username: ");
                    String username = in.readLine();
                    out.println("Enter password: ");
                    String password = in.readLine();

                    if (username == null || password == null) {
                        out.println("Connection closed.");
                        break;
                    }

                    if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
                        loggedInUser = username;
                        out.println("Login successful! Welcome " + loggedInUser);
                    } else {
                        out.println("Invalid credentials. Please try again.");
                    }
                }

                // Post-login: Menu-based interaction
                while (loggedInUser != null) {
                    showMenu(out);
                    String choice = in.readLine();

                    if (choice == null) {
                        out.println("Connection closed.");
                        break;
                    }

                    switch (choice) {
                        case "1":
                            out.println("Enter student name: ");
                            String studentName = in.readLine();
                            addStudent(studentName, out);
                            break;

                        case "2":
                            viewStudents(out);
                            break;

                        case "3":
                            out.println("Enter student name to delete: ");
                            String studentToDelete = in.readLine();
                            deleteStudent(studentToDelete, out);
                            break;

                        case "4":
                            out.println("Enter student name: ");
                            String studentForCourse = in.readLine();
                            out.println("Enter course name: ");
                            String courseName = in.readLine();
                            addCourse(studentForCourse, courseName, out);
                            break;

                        case "5":
                            out.println("Enter student name: ");
                            String studentToView = in.readLine();
                            viewCourses(studentToView, out);
                            break;

                        case "6":
                            out.println("Enter student name: ");
                            String studentForDeleteCourse = in.readLine();
                            out.println("Enter course name to delete: ");
                            String courseToDelete = in.readLine();
                            deleteCourse(studentForDeleteCourse, courseToDelete, out);
                            break;

                        case "7":
                            out.println("Goodbye!");
                            return;

                        default:
                            out.println("Invalid choice! Please select a valid option.");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void showMenu(PrintWriter out) {
            out.println("\n--- Menu ---");
            out.println("1. Add Student");
            out.println("2. View Students");
            out.println("3. Delete Student");
            out.println("4. Add Course for Student");
            out.println("5. View Courses of a Student");
            out.println("6. Delete Course from Student");
            out.println("7. Exit");
            out.println("Enter your choice: ");
        }

        private void addStudent(String studentName, PrintWriter out) {
            synchronized (studentList) {
                if (!studentList.contains(studentName)) {
                    studentList.add(studentName);
                    studentCourses.putIfAbsent(studentName, new ArrayList<>());
                    out.println("Student added: " + studentName);
                } else {
                    out.println("Student already exists: " + studentName);
                }
            }
        }

        private void viewStudents(PrintWriter out) {
            synchronized (studentList) {
                if (studentList.isEmpty()) {
                    out.println("No students available.");
                } else {
                    out.println("Students: " + studentList);
                }
            }
        }

        private void deleteStudent(String studentName, PrintWriter out) {
            synchronized (studentList) {
                if (studentList.remove(studentName)) {
                    studentCourses.remove(studentName);
                    out.println("Student deleted: " + studentName);
                } else {
                    out.println("Student not found: " + studentName);
                }
            }
        }

        private void addCourse(String studentName, String courseName, PrintWriter out) {
            synchronized (studentList) {
                if (studentList.contains(studentName)) {
                    studentCourses.get(studentName).add(courseName);
                    out.println("Course added for " + studentName + ": " + courseName);
                } else {
                    out.println("Student not found: " + studentName);
                }
            }
        }

        private void viewCourses(String studentName, PrintWriter out) {
            synchronized (studentList) {
                if (studentList.contains(studentName)) {
                    List<String> courses = studentCourses.get(studentName);
                    out.println("Courses for " + studentName + ": " + courses);
                } else {
                    out.println("Student not found: " + studentName);
                }
            }
        }

        private void deleteCourse(String studentName, String courseName, PrintWriter out) {
            synchronized (studentList) {
                if (studentList.contains(studentName)) {
                    List<String> courses = studentCourses.get(studentName);
                    if (courses.remove(courseName)) {
                        out.println("Course removed for " + studentName + ": " + courseName);
                    } else {
                        out.println("Course not found: " + courseName);
                    }
                } else {
                    out.println("Student not found: " + studentName);
                }
            }
        }
    }
}
