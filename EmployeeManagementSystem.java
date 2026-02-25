import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String department;
    private String position;
    private double salary;
    private Date joinDate;

    public Employee(String id, String name, String department,
                    String position, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.position = position;
        this.salary = salary;
        this.joinDate = new Date();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getPosition() { return position; }
    public double getSalary() { return salary; }
    public Date getJoinDate() { return joinDate; }

    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setPosition(String position) { this.position = position; }
    public void setSalary(double salary) { this.salary = salary; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("ID: %s | Name: %s | Dept: %s | Position: %s | Salary: ₹%.2f | Joined: %s",
                id, name, department, position, salary, sdf.format(joinDate));
    }
}

public class EmployeeManagementSystem {

    private static ArrayList<Employee> employees = new ArrayList<>();
    private static HashMap<String, Employee> employeeMap = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "employees.dat";

    public static void main(String[] args) {
        loadFromFile();
        menu();
    }

    public static void menu() {
        while (true) {
            System.out.println("\n===== EMPLOYEE MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. Generate Report");
            System.out.println("7. Save & Exit");
            System.out.print("Enter choice: ");

            int choice = getInt();

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewEmployees();
                    break;
                case 3:
                    searchEmployee();
                    break;
                case 4:
                    updateEmployee();
                    break;
                case 5:
                    deleteEmployee();
                    break;
                case 6:
                    generateReport();
                    break;
                case 7:
                    saveToFile();
                    System.out.println("Data Saved. Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ================= ADD =================
    private static void addEmployee() {
        scanner.nextLine();

        System.out.print("Enter ID: ");
        String id = scanner.nextLine();

        if (employeeMap.containsKey(id)) {
            System.out.println("Employee already exists!");
            return;
        }

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Department: ");
        String dept = scanner.nextLine();

        System.out.print("Enter Position: ");
        String pos = scanner.nextLine();

        System.out.print("Enter Salary: ");
        double salary = getDouble();

        Employee emp = new Employee(id, name, dept, pos, salary);

        employees.add(emp);
        employeeMap.put(id, emp);

        System.out.println("Employee Added Successfully!");
    }

    // ================= VIEW =================
    private static void viewEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found!");
            return;
        }

        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    // ================= SEARCH =================
    private static void searchEmployee() {
        scanner.nextLine();
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine();

        Employee emp = employeeMap.get(id);

        if (emp != null)
            System.out.println(emp);
        else
            System.out.println("Employee not found!");
    }

    // ================= UPDATE =================
    private static void updateEmployee() {
        scanner.nextLine();
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine();

        Employee emp = employeeMap.get(id);

        if (emp == null) {
            System.out.println("Employee not found!");
            return;
        }

        System.out.print("New Name: ");
        emp.setName(scanner.nextLine());

        System.out.print("New Department: ");
        emp.setDepartment(scanner.nextLine());

        System.out.print("New Position: ");
        emp.setPosition(scanner.nextLine());

        System.out.print("New Salary: ");
        emp.setSalary(getDouble());

        System.out.println("Employee Updated Successfully!");
    }

    // ================= DELETE =================
    private static void deleteEmployee() {
        scanner.nextLine();
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine();

        Employee emp = employeeMap.remove(id);

        if (emp != null) {
            employees.remove(emp);
            System.out.println("Employee Deleted Successfully!");
        } else {
            System.out.println("Employee not found!");
        }
    }

    // ================= REPORT =================
    private static void generateReport() {
        if (employees.isEmpty()) {
            System.out.println("No employees available.");
            return;
        }

        double total = 0;
        double highest = employees.get(0).getSalary();
        double lowest = employees.get(0).getSalary();

        for (Employee e : employees) {
            total += e.getSalary();
            if (e.getSalary() > highest) highest = e.getSalary();
            if (e.getSalary() < lowest) lowest = e.getSalary();
        }

        System.out.println("\n===== REPORT =====");
        System.out.println("Total Employees: " + employees.size());
        System.out.println("Total Salary: ₹" + total);
        System.out.println("Average Salary: ₹" + (total / employees.size()));
        System.out.println("Highest Salary: ₹" + highest);
        System.out.println("Lowest Salary: ₹" + lowest);
    }

    // ================= FILE SAVE =================
    private static void saveToFile() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(employees);

        } catch (IOException e) {
            System.out.println("Error saving file!");
        }
    }

    // ================= FILE LOAD =================
    @SuppressWarnings("unchecked")
    private static void loadFromFile() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            employees = (ArrayList<Employee>) ois.readObject();

            for (Employee emp : employees) {
                employeeMap.put(emp.getId(), emp);
            }

        } catch (Exception e) {
            System.out.println("No previous data found.");
        }
    }

    // ================= INPUT VALIDATION =================
    private static int getInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("Enter valid number!");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static double getDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.println("Enter valid salary!");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
