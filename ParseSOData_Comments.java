import java.io.*;
import java.sql.*;
import java.util.*;

import org.apache.commons.lang3.StringEscapeUtils;

public class ParseSOData_Comments {

  static int parseIntColumn(String s, String tag) throws Exception {
    String val = parseStringColumn(s, tag);
    if (val == null)
      return 0;
    return Integer.parseInt(val);
  }

  static String parseStringColumn(String s, String tag) throws Exception {
    String xmlTag = " " + tag + "=\"";
    int idx = s.indexOf(xmlTag);
    if (idx == -1)
      return null;
    idx += xmlTag.length();
    int endIdx = s.indexOf("\"", idx);
    if (endIdx == -1)
      return null;
    return StringEscapeUtils.unescapeXml(s.substring(idx, endIdx));
  }

  public static void main(String[] args) throws Exception {
    Connection c = null;
    Class.forName("org.sqlite.JDBC");
    c = DriverManager.getConnection("jdbc:sqlite:so_comments.db");
    System.out.println("Opened database successfully");

    String sql = "INSERT INTO so_comments (Id, PostId, Score, Text, "
       + "CreationDate, UserId, UserDisplayName) "
       + "values (?, ?, ?, ?, ?, ?, ?)";

    Statement stmt = c.createStatement();
    stmt.execute("PRAGMA journal_mode = OFF;");
    stmt.execute("PRAGMA synchronous = OFF;");
    stmt.execute("PRAGMA temp_store = MEMORY;");
    stmt.execute("PRAGMA cache_size = 1000000;");

    c.setAutoCommit(false);
    PreparedStatement st = c.prepareStatement(sql);
    BufferedReader fr = new BufferedReader(new InputStreamReader(System.in));

    String s;
    int rows = 0;

    while ((s = fr.readLine()) != null) {
        if (!s.startsWith("  <row "))
            continue;

        st.setInt(1, parseIntColumn(s, "Id"));
        st.setInt(2, parseIntColumn(s, "PostId"));
        st.setInt(3, parseIntColumn(s, "Score"));
        st.setString(4, parseStringColumn(s, "Text"));
        st.setString(5, parseStringColumn(s, "CreationDate"));
        st.setInt(6, parseIntColumn(s, "UserId"));
        st.setString(7, parseStringColumn(s, "UserDisplayName"));

        st.addBatch();

        if (++rows % 10000 == 0) {
            st.executeBatch();
            c.commit();
            System.out.print(".");
        }
    }

    st.executeBatch(); // flush remaining
    c.commit();
    stmt.close();
    fr.close();
    System.out.println();
    st.close();
    c.close();
  }
}