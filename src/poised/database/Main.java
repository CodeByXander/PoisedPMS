package poised.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    // Get database connection
    Connection conn = DatabaseConnection.getConnection();
    Scanner sc = new Scanner(System.in);

    int userChoice;

    do {
      // Main menu
      System.out.println("--- Poised Project Management ---\n");
      System.out.println("1. View all projects\n2. Add new project\n3. Update a project\n"
          + "4. Delete a project\n5. Finalise a project\n6. View incomplete projects\n"
          + "7. View overdue projects\n8. Find Project\n9. Edit project members\n10. Exit\n");
      System.out.println("Enter your choice: ");

      userChoice = sc.nextInt();
      sc.nextLine();

      // Handle user menu selection
      switch (userChoice) {
        case 1:
          viewAllProjects();
          break;
        case 2:
          createNewProject(sc);
          break;
        case 3:
          updateProject(sc);
          break;
        case 4:
          deleteProject(sc);
          break;
        case 5:
          finaliseProject(sc);
          break;
        case 6:
          viewIncompleteProjects();
          break;
        case 7:
          viewOverdueProjects();
          break;
        case 8:
          findProject(sc);
          break;
        case 9:
          projectMembers(sc);
          break;
        default:
          System.out.println("Invalid choice, please try again.");
      }
    } while (userChoice != 10);

    sc.close();
  }

  // Display all projects
  public static void viewAllProjects() {
    String query = "SELECT * FROM Project";

    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      System.out.println("\n--- All Projects ---\n");

      while (rs.next()) {
        System.out.println("ID: " + rs.getInt("project_id") + ", Name: "
            + rs.getString("project_name") + ", Type: " + rs.getString("building_type")
            + ", Status: " + rs.getString("status") + ", Deadline: " + rs.getString("deadline"));
      }

      System.out.println();

    } catch (SQLException e) {
      System.out.println("Error fetching projects...");
      e.printStackTrace();
    }
  }

  // Add a new project
  public static void createNewProject(Scanner sc) {
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO Project (project_name, building_type, address, erf_number, total_fee, amount_paid, deadline, completion_date, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

      System.out.println("\n--- Add New Project ---");

      // Gather project details from user
      System.out.print("Enter building type (House, Apartment, Store, etc.): ");
      String buildingType = sc.nextLine();

      System.out.print("Enter customer last name: ");
      String customerLastName = sc.nextLine();

      // Auto-generate project name
      String projectName = buildingType + " " + customerLastName;

      System.out.print("Enter project address: ");
      String address = sc.nextLine();

      System.out.print("Enter ERF number: ");
      String erfNumber = sc.nextLine();

      System.out.print("Enter total fee: ");
      double totalFee = Double.parseDouble(sc.nextLine());

      System.out.print("Enter amount paid to date: ");
      double amountPaid = Double.parseDouble(sc.nextLine());

      System.out.print("Enter deadline (YYYY-MM-DD): ");
      String deadline = sc.nextLine();

      // New projects are not yet finalised
      String completionDate = null;
      String status = "In Progress";

      // Set parameters and execute insert
      pstmt.setString(1, projectName);
      pstmt.setString(2, buildingType);
      pstmt.setString(3, address);
      pstmt.setString(4, erfNumber);
      pstmt.setDouble(5, totalFee);
      pstmt.setDouble(6, amountPaid);
      pstmt.setString(7, deadline);
      pstmt.setString(8, completionDate);
      pstmt.setString(9, status);

      int rowsInserted = pstmt.executeUpdate();
      System.out
          .println(rowsInserted > 0 ? "Project added successfully!" : "Failed to add project.");

    } catch (SQLException e) {
      System.out.println("Database error occurred while adding project.");
      e.printStackTrace();
    } catch (NumberFormatException e) {
      System.out.println("Invalid number entered. Please try again.");
    }
  }

  // Update project details
  public static void updateProject(Scanner sc) {
    try (Connection conn = DatabaseConnection.getConnection()) {

      System.out.println("\n--- Update Project ---");
      viewAllProjects(); // Display existing projects

      System.out.print("\nEnter the project ID you want to update: ");
      int projectId = Integer.parseInt(sc.nextLine());

      // Let user choose field to update
      System.out.println("What do you want to update?");
      System.out.println(
          "1. Project Name\n2. Building Type\n3. Address\n4. ERF Number\n5. Total Fee\n6. Amount Paid\n7. Deadline\n8. Status");

      System.out.print("Enter your choice: ");
      int fieldChoice = Integer.parseInt(sc.nextLine());

      String column = "";
      String newValue = "";

      switch (fieldChoice) {
        case 1 -> {
          column = "project_name";
          System.out.print("Enter new project name: ");
          newValue = sc.nextLine();
        }
        case 2 -> {
          column = "building_type";
          System.out.print("Enter new building type: ");
          newValue = sc.nextLine();
        }
        case 3 -> {
          column = "address";
          System.out.print("Enter new address: ");
          newValue = sc.nextLine();
        }
        case 4 -> {
          column = "erf_number";
          System.out.print("Enter new ERF number: ");
          newValue = sc.nextLine();
        }
        case 5 -> {
          column = "total_fee";
          System.out.print("Enter new total fee: ");
          newValue = sc.nextLine();
        }
        case 6 -> {
          column = "amount_paid";
          System.out.print("Enter new amount paid: ");
          newValue = sc.nextLine();
        }
        case 7 -> {
          column = "deadline";
          System.out.print("Enter new deadline (YYYY-MM-DD): ");
          newValue = sc.nextLine();
        }
        case 8 -> {
          column = "status";
          System.out.print("Enter new status: ");
          newValue = sc.nextLine();
        }
        default -> {
          System.out.println("Invalid choice.");
          return;
        }
      }

      String sql = "UPDATE Project SET " + column + " = ? WHERE project_id = ?";
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        // Handle numeric fields
        if (column.equals("total_fee") || column.equals("amount_paid")) {
          pstmt.setDouble(1, Double.parseDouble(newValue));
        } else {
          pstmt.setString(1, newValue);
        }
        pstmt.setInt(2, projectId);

        int rowsUpdated = pstmt.executeUpdate();
        System.out.println(
            rowsUpdated > 0 ? "Project updated successfully!" : "No project found with that ID.");
      }

    } catch (SQLException e) {
      System.out.println("Database error occurred while updating project.");
      e.printStackTrace();
    } catch (NumberFormatException e) {
      System.out.println("Invalid number entered. Please try again.");
    }
  }

  // Delete a project
  public static void deleteProject(Scanner sc) {
    try (Connection conn = DatabaseConnection.getConnection()) {

      System.out.println("\n--- Delete Project ---");
      viewAllProjects();

      System.out.print("\nEnter the project ID you want to delete: ");
      int projectId = Integer.parseInt(sc.nextLine());

      // Confirm deletion
      System.out.print("Are you sure you want to delete this project? (y/n): ");
      String confirm = sc.nextLine();
      if (!confirm.equalsIgnoreCase("yes")) {
        System.out.println("Deletion cancelled.");
        return;
      }

      // Delete linked project-person relationships first
      String sqlProjectPerson = "DELETE FROM Project_Person WHERE project_id = ?";
      try (PreparedStatement pstmt = conn.prepareStatement(sqlProjectPerson)) {
        pstmt.setInt(1, projectId);
        pstmt.executeUpdate();
      }

      // Delete project
      String sqlProject = "DELETE FROM Project WHERE project_id = ?";
      try (PreparedStatement pstmt = conn.prepareStatement(sqlProject)) {
        pstmt.setInt(1, projectId);
        int rowsDeleted = pstmt.executeUpdate();
        System.out.println(
            rowsDeleted > 0 ? "Project deleted successfully!" : "No project found with that ID.");
      }

    } catch (SQLException e) {
      System.out.println("Database error occurred while deleting project.");
      e.printStackTrace();
    } catch (NumberFormatException e) {
      System.out.println("Invalid number entered. Please try again.");
    }
  }

  // Finalise project
  public static void finaliseProject(Scanner sc) {
    try (Connection conn = DatabaseConnection.getConnection()) {

      System.out.println("\n--- Finalise Project ---");
      viewAllProjects();

      System.out.print("\nEnter the project ID you want to finalise: ");
      int projectId = Integer.parseInt(sc.nextLine());

      System.out.print("Enter completion date (YYYY-MM-DD): ");
      String completionDate = sc.nextLine();

      String sql = "UPDATE Project SET status = ?, completion_date = ? WHERE project_id = ?";
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, "Finalised");
        pstmt.setString(2, completionDate);
        pstmt.setInt(3, projectId);

        int rowsUpdated = pstmt.executeUpdate();
        System.out.println(
            rowsUpdated > 0 ? "Project finalised successfully!" : "No project found with that ID.");
      }

    } catch (SQLException e) {
      System.out.println("Database error occurred while finalising project.");
      e.printStackTrace();
    }
  }

  // Display incomplete projects
  public static void viewIncompleteProjects() {
    String query = "SELECT * FROM Project WHERE status != 'Finalised'";

    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      System.out.println("\n--- Incomplete Projects ---");
      boolean hasProjects = false;

      while (rs.next()) {
        hasProjects = true;
        System.out.println("ID: " + rs.getInt("project_id") + ", Name: "
            + rs.getString("project_name") + ", Type: " + rs.getString("building_type")
            + ", Status: " + rs.getString("status") + ", Deadline: " + rs.getString("deadline"));
      }

      if (!hasProjects) {
        System.out.println("No incomplete projects found.");
      }

    } catch (SQLException e) {
      System.out.println("Error fetching incomplete projects.");
      e.printStackTrace();
    }
  }

  // Display overdue projects
  public static void viewOverdueProjects() {
    String query = "SELECT * FROM Project WHERE status != 'Finalised' AND deadline < CURDATE()";

    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      System.out.println("\n--- Overdue Projects ---");
      boolean hasProjects = false;

      while (rs.next()) {
        hasProjects = true;
        System.out.println("ID: " + rs.getInt("project_id") + ", Name: "
            + rs.getString("project_name") + ", Type: " + rs.getString("building_type")
            + ", Status: " + rs.getString("status") + ", Deadline: " + rs.getString("deadline"));
      }

      if (!hasProjects) {
        System.out.println("No overdue projects found.");
      }

    } catch (SQLException e) {
      System.out.println("Error fetching overdue projects.");
      e.printStackTrace();
    }
  }

  // Find project by ID or name
  public static void findProject(Scanner sc) {
    System.out.println("\n--- Find a Project ---");
    System.out.print("Enter project ID or project name: ");
    String input = sc.nextLine();

    String query = "SELECT * FROM Project WHERE project_id = ? OR project_name LIKE ?";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {

      int projectId = -1;
      try {
        projectId = Integer.parseInt(input);
      } catch (NumberFormatException e) {
      }

      pstmt.setInt(1, projectId);
      pstmt.setString(2, "%" + input + "%");

      try (ResultSet rs = pstmt.executeQuery()) {
        boolean found = false;
        while (rs.next()) {
          found = true;
          System.out.println("ID: " + rs.getInt("project_id") + ", Name: "
              + rs.getString("project_name") + ", Type: " + rs.getString("building_type")
              + ", Status: " + rs.getString("status") + ", Deadline: " + rs.getString("deadline"));
        }

        if (!found) {
          System.out.println("No project found with that ID or name.");
        }
      }

    } catch (SQLException e) {
      System.out.println("Error searching for project.");
      e.printStackTrace();
    }
  }

  // Manage project members
  public static void projectMembers(Scanner sc) {
    int userChoice;

    do {
      System.out.println("--- Project Members ---\n");
      System.out.println(
          "1. View all project members\n2. Create new project member\n3. Update project member\n"
              + "4. Delete project member\n5. Assign project members\n6. Exit");

      userChoice = sc.nextInt();
      sc.nextLine();

      switch (userChoice) {
        case 1 -> viewAllPeople();
        case 2 -> addPerson(sc);
        case 3 -> updatePerson(sc);
        case 4 -> deletePerson(sc);
        case 5 -> assignPeopleToProject(sc);
        case 6 -> System.out.println("Exiting Program...");
        default -> System.out.println("Invalid option, please select again.");
      }
    } while (userChoice != 6);
  }

  // Display all people
  public static void viewAllPeople() {
    String query = "SELECT * FROM Person";

    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      System.out.println("\n--- All People ---");
      while (rs.next()) {
        System.out.println("ID: " + rs.getInt("person_id") + ", Name: " + rs.getString("first_name")
            + " " + rs.getString("last_name") + ", Phone: " + rs.getString("phone") + ", Email: "
            + rs.getString("email") + ", Address: " + rs.getString("address"));
      }

    } catch (SQLException e) {
      System.out.println("Error fetching people.");
      e.printStackTrace();
    }
  }

  // Add new person
  public static void addPerson(Scanner sc) {
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO Person (first_name, last_name, phone, email, address) VALUES (?, ?, ?, ?, ?)")) {

      System.out.println("\n--- Add New Person ---");

      System.out.print("Enter first name: ");
      String firstName = sc.nextLine();

      System.out.print("Enter last name: ");
      String lastName = sc.nextLine();

      System.out.print("Enter phone number: ");
      String phone = sc.nextLine();

      System.out.print("Enter email address: ");
      String email = sc.nextLine();

      System.out.print("Enter physical address: ");
      String address = sc.nextLine();

      pstmt.setString(1, firstName);
      pstmt.setString(2, lastName);
      pstmt.setString(3, phone);
      pstmt.setString(4, email);
      pstmt.setString(5, address);

      int rowsInserted = pstmt.executeUpdate();
      System.out.println(rowsInserted > 0 ? "Person added successfully!" : "Failed to add person.");

    } catch (SQLException e) {
      System.out.println("Database error occurred while adding person.");
      e.printStackTrace();
    }
  }

  // Update person details
  public static void updatePerson(Scanner sc) {
    try (Connection conn = DatabaseConnection.getConnection()) {

      System.out.println("\n--- Update Person ---");
      viewAllPeople();

      System.out.print("\nEnter the person ID you want to update: ");
      int personId = Integer.parseInt(sc.nextLine());

      System.out.println("Which field do you want to update?");
      System.out.println("1. First Name\n2. Last Name\n3. Phone\n4. Email\n5. Address");
      System.out.print("Enter your choice: ");
      int choice = Integer.parseInt(sc.nextLine());

      String column = "";
      String newValue = "";

      switch (choice) {
        case 1 -> {
          column = "first_name";
          System.out.print("Enter new first name: ");
          newValue = sc.nextLine();
        }
        case 2 -> {
          column = "last_name";
          System.out.print("Enter new last name: ");
          newValue = sc.nextLine();
        }
        case 3 -> {
          column = "phone";
          System.out.print("Enter new phone: ");
          newValue = sc.nextLine();
        }
        case 4 -> {
          column = "email";
          System.out.print("Enter new email: ");
          newValue = sc.nextLine();
        }
        case 5 -> {
          column = "address";
          System.out.print("Enter new address: ");
          newValue = sc.nextLine();
        }
        default -> {
          System.out.println("Invalid choice.");
          return;
        }
      }

      String sql = "UPDATE Person SET " + column + " = ? WHERE person_id = ?";
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, newValue);
        pstmt.setInt(2, personId);

        int rowsUpdated = pstmt.executeUpdate();
        System.out.println(
            rowsUpdated > 0 ? "Person updated successfully!" : "No person found with that ID.");
      }

    } catch (SQLException e) {
      System.out.println("Database error occurred while updating person.");
      e.printStackTrace();
    }
  }

  // Delete person
  public static void deletePerson(Scanner sc) {
    try (Connection conn = DatabaseConnection.getConnection()) {

      System.out.println("\n--- Delete Person ---");
      viewAllPeople();

      System.out.print("\nEnter the person ID you want to delete: ");
      int personId = Integer.parseInt(sc.nextLine());

      // Confirm deletion
      System.out.print("Are you sure you want to delete this person? (y/n): ");
      String confirm = sc.nextLine();
      if (!confirm.equalsIgnoreCase("yes")) {
        System.out.println("Deletion cancelled.");
        return;
      }

      String sql = "DELETE FROM Person WHERE person_id = ?";
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, personId);
        int rowsDeleted = pstmt.executeUpdate();
        System.out.println(
            rowsDeleted > 0 ? "Person deleted successfully!" : "No person found with that ID.");
      }

    } catch (SQLException e) {
      System.out.println("Database error occurred while deleting person.");
      e.printStackTrace();
    }
  }

  // Assign people to a project
  public static void assignPeopleToProject(Scanner sc) {
    try (Connection conn = DatabaseConnection.getConnection()) {

      System.out.println("\n--- Assign People to Project ---");
      viewAllProjects();
      System.out.print("Enter the project ID to assign people to: ");
      int projectId = Integer.parseInt(sc.nextLine());

      viewAllPeople();
      System.out.print("Enter the person ID to assign: ");
      int personId = Integer.parseInt(sc.nextLine());

      String sql = "INSERT INTO Project_Person (project_id, person_id) VALUES (?, ?)";
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, projectId);
        pstmt.setInt(2, personId);

        int rowsInserted = pstmt.executeUpdate();
        System.out.println(rowsInserted > 0 ? "Person assigned to project successfully!"
            : "Failed to assign person.");
      }

    } catch (SQLException e) {
      System.out.println("Database error occurred while assigning person to project.");
      e.printStackTrace();
    }
  }

}
