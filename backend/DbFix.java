import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbFix {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/luoyang_hanfu?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
        String user = "root";
        String pass = "liwenlong";
        
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Checking columns...");
            
            try {
                stmt.execute("ALTER TABLE service_package ADD COLUMN image varchar(255) COMMENT '图片' AFTER description");
                System.out.println("Added image column.");
            } catch (Exception e) {
                System.out.println("Image column might already exist: " + e.getMessage());
            }
            
            try {
                stmt.execute("ALTER TABLE service_package ADD COLUMN subtitle varchar(200) COMMENT '副标题' AFTER name");
                System.out.println("Added subtitle column.");
            } catch (Exception e) {
                System.out.println("Subtitle column might already exist: " + e.getMessage());
            }
            
            System.out.println("Done.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
