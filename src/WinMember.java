import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Panel;
import javax.swing.JTabbedPane;
import java.awt.Toolkit;

public class WinMember extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private String strPhone;
	private String Members[][] = new String [100][2];
	private Member member[];
	private String mphone;
	
	/**
	 * Create the dialog.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */

	public WinMember() throws SQLException, ClassNotFoundException {
		setTitle("회원별 적립내역");
		setBounds(100, 100, 450, 391);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabbedPane, BorderLayout.CENTER);
		setSize(250,350);

		int count = SelectMember();
		
		MemberAll[] member = new MemberAll[count];
		// ==========================
		Class.forName("com.mysql.cj.jdbc.Driver");
		String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
		Connection con = DriverManager.getConnection(strCon);
		Statement stmt = con.createStatement();
		
		
		String sql="select mphone, mstamp, mdate from membertbl group by mphone;";			
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) { 
			Members[count][0] = rs.getString("mphone");
			Members[count][1] = String.valueOf("mstamp");
			Members[count][2] = String.valueOf(rs.getString("mdate"));
			count++;
		}
		// ==========================


			for(int i=0;i<count;i++) {
				member[i] = new MemberAll(Members[i][0],Members[i][1], Members[i][2]);
				tabbedPane.addTab(Members[i][0], member[i]);
		}
		
	}


	private int SelectMember() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
		Connection con = DriverManager.getConnection(strCon);
		Statement stmt = con.createStatement();
		String sql = "select * from membertbl";

		ResultSet rs = stmt.executeQuery(sql);
		int count=0;
		while(rs.next()) {
			count++;	
		}
		return count;
	}
	
}
