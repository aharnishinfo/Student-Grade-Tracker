package Project;


import java.sql.*;
import java.util.*;


public class Code_Alpha_Task1 {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/JavaJdbc";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Integer> grades = new ArrayList<>();

       
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            createTable(conn); 

            boolean exit = false;
            while (!exit) {
                System.out.println("\n=== Student Grade Tracker ===");
                System.out.println("1. Add Student Grades");
                System.out.println("2. View Grade Analysis");
                System.out.println("3. Save Grades to Database");
                System.out.println("4. Retrieve Grades from Database");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                try {
                    int choice = sc.nextInt();
                    sc.nextLine(); 

                    switch (choice) {
                        case 1:
                            
                            System.out.print("Enter the number of students: ");
                            int numStudents = sc.nextInt();
                            sc.nextLine();

                            for (int i = 0; i < numStudents; i++) {
                                System.out.print("Enter grade for student " + (i + 1) + ": ");
                                grades.add(sc.nextInt());
                            }
                            break;

                        case 2:
                            
                            displayGradeAnalysis(grades);
                            break;

                        case 3:
                         
                            saveGradesToDatabase(conn, grades);
                            break;

                        case 4:
                            
                            grades = retrieveGradesFromDatabase(conn);
                            displayGradeAnalysis(grades);
                            break;

                        case 5:
                            exit = true;
                            break;

                        default:
                            System.out.println("Invalid option. Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    sc.nextLine(); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static void displayGradeAnalysis(ArrayList<Integer> grades) {
        if (grades.isEmpty()) {
            System.out.println("No grades available.");
            return;
        }

        double average = calculateAverage(grades);
        int highest = findHighest(grades);
        int lowest = findLowest(grades);

        System.out.println("\nGrade Analysis:");
        System.out.println("Average Grade: " + average);
        System.out.println("Highest Grade: " + highest);
        System.out.println("Lowest Grade: " + lowest);
    }

    
    public static double calculateAverage(ArrayList<Integer> grades) {
        int sum = 0;
        for (int grade : grades) {
            sum += grade;
        }
        return (double) sum / grades.size();
    }

    
    public static int findHighest(ArrayList<Integer> grades) {
        return Collections.max(grades);
    }


    public static int findLowest(ArrayList<Integer> grades) {
        return Collections.min(grades);
    }


    public static void createTable(Connection conn) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS student_grades (id INT AUTO_INCREMENT PRIMARY KEY, grade INT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        }
    }

   
    public static void saveGradesToDatabase(Connection conn, ArrayList<Integer> grades) throws SQLException {
        String insertSQL = "INSERT INTO student_grades (grade) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            for (int grade : grades) {
                pstmt.setInt(1, grade);
                pstmt.executeUpdate();
            }
            System.out.println("Grades saved to the database successfully.");
        }
    }

   
    public static ArrayList<Integer> retrieveGradesFromDatabase(Connection conn) throws SQLException {
        ArrayList<Integer> grades = new ArrayList<>();
        String selectSQL = "SELECT grade FROM student_grades";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            while (rs.next()) {
                grades.add(rs.getInt("grade"));
            }
            System.out.println("Grades retrieved from the database successfully.");
        }
        return grades;
    }
}
