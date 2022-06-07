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

public class WinViewMember extends JDialog {

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

	public WinViewMember(String strPhone) throws SQLException, ClassNotFoundException {
		this.strPhone = strPhone;
		setTitle(strPhone + "님의 적립내역");
		setBounds(100, 100, 450, 391);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabbedPane, BorderLayout.CENTER);
		setSize(250,350);

		int count = SelectMember();
		
		ViewMember[] member = new ViewMember[count];
		// ==========================
		Class.forName("com.mysql.cj.jdbc.Driver");
		String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
		Connection con = DriverManager.getConnection(strCon);
		Statement stmt = con.createStatement();

		
		String sql="select mphone, sum(mstamp) from membertbl where mphone like '%" + strPhone + "%' group by mphone;";			
		ResultSet rs = stmt.executeQuery(sql);
		count=0;
		while (rs.next()) { 
			Members[count][0] = rs.getString("mphone");
			Members[count][1] = String.valueOf(rs.getInt(2));
			count++;
		}
		// ==========================


			for(int i=0;i<count;i++) {
				member[i] = new ViewMember(Members[i][0],Members[i][1]);
				tabbedPane.addTab(Members[i][0], member[i]);
		}
		
	}

	public WinViewMember() throws ClassNotFoundException, SQLException {
		setIconImage(Toolkit.getDefaultToolkit().getImage(WinViewMember.class.getResource("/img/coffee01.png")));
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabbedPane, BorderLayout.CENTER);

	}

	private int SelectMember() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
		Connection con = DriverManager.getConnection(strCon);
		Statement stmt = con.createStatement();
		String sql = "select * from membertbl where mphone like '%" + strPhone + "%' group by mphone;";

		ResultSet rs = stmt.executeQuery(sql);
		int count=0;
		while(rs.next()) {
//			Members[count][0] = rs.getString("mphone");
//			Members[count][1] = Integer.toString(rs.getInt(2));
			count++;	
		}
		return count;
	}
	
}
