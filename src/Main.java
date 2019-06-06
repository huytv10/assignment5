import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String user;
        String password;

        System.out.print("Input user: ");
        user = scanner.nextLine();

        System.out.print("Input password: ");
        password = scanner.nextLine();
        Connection connection = null;
        try {
            connection = connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        checkUserPassword(user, password, connection);
//        register(connection, new User("a", "111", "a@gmail.com"));
        getUserList(connection);


    }

    static void checkUserPassword(String user, String password, Connection conn) {
        List<User> userList = new ArrayList<>();
        Statement statement = null;
        try {
            statement = conn.createStatement();
            String sql = "Select * from user";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                userList.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
            List<User> result = userList.stream()
                    .filter(e -> e.getUser().equals(user))
                    .filter(e -> e.getPassword().equals(password))
                    .collect(Collectors.toList());

            if (result.size() > 0) {
                System.out.println("correct success");
            } else {
                System.out.println("correct fail");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            finallys(conn);
        }
    }

    static void getUserList(Connection conn) {
        List<User> userList = new ArrayList<>();
        Statement statement = null;
        try {
            statement = conn.createStatement();
            String sql = "Select * from user";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                userList.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }

            userList.forEach(e -> System.out.println("id: " + e.getId() + ", userName: " + e.getUser() + ", password: " + e.getPassword() + ", email: " + e.getEmail()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            finallys(conn);
        }
    }

    static void register(Connection conn, User u) {
        try {
            String user = u.getUser();
            String password = u.getPassword();
            String email = u.getEmail();

            String sqlInsert = "INSERT INTO user(user,password,email) VALUES(?, ?, ?)";
            conn.prepareStatement(sqlInsert);
            PreparedStatement stmt = conn.prepareStatement(sqlInsert);
            stmt.setString(1, user);
            stmt.setString(2, password);
            stmt.setString(3, email);

            stmt.execute();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            finallys(conn);
        }
    }

    public static Connection connect() throws Exception {
        Connection conn = null;
        conn = DriverManager.getConnection("jdbc:sqlite:C:/sqlite3/huy.db");
        return conn;
    }

    private static void finallys(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
