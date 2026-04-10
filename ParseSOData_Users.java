import java.io.*;
import java.sql.*;
import java.util.*;

import org.apache.commons.lang3.StringEscapeUtils;

public class ParseSOData {

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
    c = DriverManager.getConnection("jdbc:sqlite:so.db");
    System.out.println("Opened database successfully");
    String sql = "INSERT INTO so_users (Id, DisplayName, Location, "
       + "Reputation, Views, UpVotes, DownVotes, AccountId, "
       + "CreationDate, LastAccessDate, WebsiteUrl, AboutMe) "
       + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
        st.setString(2, parseStringColumn(s, "DisplayName"));
        st.setString(3, parseStringColumn(s, "Location"));
        st.setInt(4, parseIntColumn(s, "Reputation"));
        st.setInt(5, parseIntColumn(s, "Views"));
        st.setInt(6, parseIntColumn(s, "UpVotes"));
        st.setInt(7, parseIntColumn(s, "DownVotes"));
        st.setInt(8, parseIntColumn(s, "AccountId"));
        st.setString(9, parseStringColumn(s, "CreationDate"));
        st.setString(10, parseStringColumn(s, "LastAccessDate"));
        st.setString(11, parseStringColumn(s, "WebsiteUrl"));
        st.setString(12, parseStringColumn(s, "AboutMe"));

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
