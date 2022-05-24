import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRadioButton;

public class WinMenu extends JFrame {

	private JPanel contentPane;
	private Vector data;
	private JTable table=new JTable();
	//private JPopupMenu popupMenu = new JPopupMenu();
	private JTextField txtMenu;
	private JTextField txtPrice;



	public WinMenu() {
		setTitle("판매 메뉴 관리");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 739, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane, BorderLayout.CENTER);
		
		Vector<String> columnNames = new Vector<>();
		columnNames.add("분류");
		columnNames.add("메뉴명");
		columnNames.add("가격");

		data = new Vector<>();
		
		try {
			ShowMenu();
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // data를 가져온다.
		
		DefaultTableModel dtm = new DefaultTableModel(data, columnNames);
		table = new JTable(dtm);
		
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JRadioButton rdbtnCoffee = new JRadioButton("음료");
		panel_1.add(rdbtnCoffee);
		
		JRadioButton rdbtnNoneCoffee = new JRadioButton("디저트");
		panel_1.add(rdbtnNoneCoffee);
		
		ButtonGroup rg = new ButtonGroup(); 
		rg.add(rdbtnCoffee);rg.add(rdbtnNoneCoffee);
		rdbtnCoffee.setSelected(true);
		
		JLabel lblMenu = new JLabel("메뉴명 :");
		panel_1.add(lblMenu);
		
		txtMenu = new JTextField();
		panel_1.add(txtMenu);
		txtMenu.setColumns(10);
		
		JLabel lblPrice = new JLabel("판매가격:");
		panel_1.add(lblPrice);
		
		txtPrice = new JTextField();
		panel_1.add(txtPrice);
		txtPrice.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("추가");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String menu = txtMenu.getText();
				String price = txtPrice.getText();
				String mclass = null;
				if(rdbtnCoffee.isSelected())
					mclass="음료";
				else 
					mclass="디저트";
				
				// 테이블에 데이터 넣기
				String[] row = new String[3];
				row[0]= mclass;
				row[1]= menu;
				row[2]= price;
				
				dtm.addRow(row);
				
				txtMenu.setText(""); //빈칸 ""을 집어넣음으로써 
				txtPrice.setText("");
				rdbtnCoffee.setSelected(true);
				
				String sql = "insert into menutbl values ('"+ row[0] + "','" + row[1] + "'," + Integer.parseInt(row[2])+ ")";
				try {
					addMenu(sql);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // db에 추가
			}
		});
		panel_1.add(btnNewButton_1);
		
		JButton btnNewButton = new JButton("삭제");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowIndex = table.getSelectedRow(); 
				if(rowIndex==-1) return; //선택 값이 없을 경우 아무 일도 하지 않음
				

				String sql = "delete from menutbl where mname= '" + table.getValueAt(rowIndex,1).toString() + "';";

				dtm.removeRow(rowIndex); 
				
				try {
					subMenu(sql); // db에서 데이터 삭제
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel_1.add(btnNewButton);
		
		/*
		JButton btnUpdate = new JButton("새로 고침");
		
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				data = new Vector<>();
				try {
					ShowMenu();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

				
			}
		});
		
		
		panel_1.add(btnUpdate);
		*
		*/

	}

	protected void subMenu(String sql) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project_cafe?user=root&password=1234");
		Statement stmt = con.createStatement();

		if (stmt.executeUpdate(sql)>=1) {
			JOptionPane.showMessageDialog(null, "삭제 완료");
		}
		
		stmt.close();
		con.close();
		
	}

	protected void addMenu(String sql) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project_cafe?user=root&password=1234");
		Statement stmt = con.createStatement();

		if (stmt.executeUpdate(sql)>=1) {
			JOptionPane.showMessageDialog(null, "추가 완료");
		}
		
		stmt.close();
		con.close();
		
	}

	private void ShowMenu() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project_cafe?user=root&password=1234");
		Statement stmt = con.createStatement();
		String sql = "select * from menutbl";
		ResultSet rs = stmt.executeQuery(sql);
		
		
		while(rs.next()) {
			Vector row = new Vector<>();
			for(int i=0;i<3;i++) 
				row.add(rs.getString(i+1));			
			data.add(row);
		}
//		rs.close();
//		stmt.close();
//		con.close();
		
	}
	}