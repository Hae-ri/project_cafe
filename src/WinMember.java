import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Panel;
import javax.swing.JTabbedPane;
import java.awt.Toolkit;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WinMember extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private String Members[][] = new String [100][3];
	private Member member[];
	private Vector<String> phone = new Vector<>();
	private JTextField txtPhone;
	private JTable table=new JTable();
	private DefaultTableModel dtm;
	private Vector<String> columnNames = new Vector<>();
	private Vector stamp;
	private int count;
	private JLabel lblCount;
	private JLabel lblNewLabel_1;
	
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
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("핸드폰 번호 :");
		panel_1.add(lblNewLabel);
		
		txtPhone = new JTextField();
		panel_1.add(txtPhone);
		txtPhone.setColumns(10);
		
		JButton btnSearch = new JButton("조회");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				while(dtm.getRowCount() > 0){
					   for(int i = 0 ; i < dtm.getRowCount();i++){
						   dtm.removeRow(i);
					   }
					}
				lblCount.setText("");
				lblNewLabel_1.setText("");
				
				try {
					SearchMember();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel_1.add(btnSearch);
		
		JButton btnAll = new JButton("전체 보기");
		btnAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				while(dtm.getRowCount() > 0){
					   for(int i = 0 ; i < dtm.getRowCount();i++){
						   dtm.removeRow(i);
					   }
					}
				lblCount.setText("");
				lblNewLabel_1.setText("");
				
				try {
					count = SelectMember();
					All();
					lblCount.setText(Integer.toString(count));
					lblNewLabel_1.setText("명");
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel_1.add(btnAll);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane, BorderLayout.CENTER);
		
		count = SelectMember();
		
		columnNames = new Vector<>();
		columnNames.add("핸드폰번호");
		columnNames.add("수량");
		columnNames.add("적립날짜");

		stamp = new Vector<>();
		
		dtm = new DefaultTableModel(stamp,columnNames);
		table = new JTable(dtm);
		scrollPane.setViewportView(table);
		
		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel.add(panel_3, BorderLayout.SOUTH);
		
		lblCount = new JLabel("");
		panel_3.add(lblCount);
		lblCount.setText(Integer.toString(count));
		
		lblNewLabel_1 = new JLabel("명");
		panel_3.add(lblNewLabel_1);
		setSize(416,452);
		
		All();
	}
	
	private void All() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
		Connection con = DriverManager.getConnection(strCon);
		Statement stmt = con.createStatement();
		
		String sql="select * from membertbl;";			
		ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) { 
				
				stamp = new Vector<>();
				stamp.add(rs.getString(1));
				stamp.add(String.valueOf(rs.getInt(2)));
				stamp.add(String.valueOf(rs.getString("mdate")));
//				count++;
				dtm.addRow(stamp);
				
			}
	}
	
	private int SelectMember() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
		Connection con = DriverManager.getConnection(strCon);
		Statement stmt = con.createStatement();
		String sql = "select distinct mphone from membertbl;";

		ResultSet rs = stmt.executeQuery(sql);
		int count=0;
		while(rs.next()) {
			phone.add(rs.getString(1));
			count++;	
		}
		return count;
	}

	private void SearchMember() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
		Connection con = DriverManager.getConnection(strCon);
		Statement stmt = con.createStatement();

			String sql="select * from membertbl where mphone like '%" + txtPhone.getText() + "%';";			
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) { 
				stamp = new Vector<>();
				stamp.add(rs.getString(1));
				stamp.add(String.valueOf(rs.getInt(2)));
				stamp.add(String.valueOf(rs.getString("mdate")));
				dtm.addRow(stamp);			
			}
		}

}