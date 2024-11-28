package SERVER;

import java.io.*;
import java.net.*;
import java.util.*;

class Student {
    int id;
    String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name;
    }
}

public class Server {
    private static List<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3334)) {
            System.out.println("Server started on port 3334...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            showMenu(out);

            while (true) {
                String choice = in.readLine();
                if (choice == null) break;

                switch (choice) {
                    case "1": // Add Student
                        out.println("Enter Student ID: ");
                        int studentId = Integer.parseInt(in.readLine());
                        out.println("Enter Student Name: ");
                        String studentName = in.readLine();
                        addStudent(studentId, studentName, out);
                        break;

                    case "2": // View Students
                        viewStudents(out);
                        break;

                    case "3": // Delete Student
                        out.println("Enter Student ID to delete: ");
                        int deleteId = Integer.parseInt(in.readLine());
                        deleteStudent(deleteId, out);
                        break;

                    case "4": // Exit
                        out.println("Goodbye!");
                        return;

                    default:
                        out.println("Invalid choice! Try again.");
                }
                showMenu(out);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        }
    }

    private static void showMenu(PrintWriter out) {
        out.println("\n******** Student Management System ********");
        out.println("**** Developed By Md Mehedi Hasan ****");
        out.println("1. Add Student");
        out.println("2. View Students");
        out.println("3. Delete Student");
        out.println("4. Exit");
        out.println("Enter your choice: ");
    }

    private static synchronized void addStudent(int id, String name, PrintWriter out) {
        if (id <= 0 || name.trim().isEmpty()) {
            out.println("Invalid input. ID must be positive, and name cannot be empty.");
            return;
        }
        students.add(new Student(id, name));
        out.println("Student added successfully.");
    }

    private static synchronized void viewStudents(PrintWriter out) {
        if (students.isEmpty()) {
            out.println("No students found.");
        } else {
            out.println("Students List:");
            for (Student student : students) {
                out.println(student);
            }
        }
    }

    private static synchronized void deleteStudent(int id, PrintWriter out) {
        Iterator<Student> iterator = students.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().id == id) {
                iterator.remove();
                out.println("Student deleted successfully.");
                return;
            }
        }
        out.println("Student ID not found.");
    }
}
