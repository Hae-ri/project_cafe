import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Color;
import javax.swing.border.LineBorder;

public class WinOrder extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton btnBeverage;
	private JButton btnDessert;
	private int count=0;
	private Vector menus;
	private Vector order;
	private JTable table=new JTable();
	private DefaultTableModel dtm;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinOrder dialog = new WinOrder();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public WinOrder() throws ClassNotFoundException, SQLException {
		initGUI();
	}
	
//	public viewMenu(String sql) {
//		
//	}
	
	public void initGUI() throws ClassNotFoundException, SQLException {
		setTitle("주문하기");
		setBounds(100, 100, 966, 675);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(0, 0));
			{ // 오른쪽 주문 내역 테이블
				JPanel panel_orderTable = new JPanel(); 
				panel.add(panel_orderTable, BorderLayout.EAST);
				panel_orderTable.setLayout(new BorderLayout(0, 0));
				{
					JScrollPane scrollPane = new JScrollPane();
					panel_orderTable.add(scrollPane, BorderLayout.CENTER);
					{
						Vector<String> columnNames = new Vector<>();
						columnNames.add("메뉴명");
						columnNames.add("가격");
						columnNames.add("수량");
						columnNames.add("합계");

						order = new Vector<>();
						
						// 버튼 클릭하면 데이터 추가 됨
						
						
						dtm = new DefaultTableModel(order, columnNames);
						table = new JTable(dtm);
						
						scrollPane.setViewportView(table);
					}
				}
				{
					JPanel panel_1 = new JPanel();
					panel_orderTable.add(panel_1, BorderLayout.SOUTH);
					{
						JButton btnCard = new JButton("카드");
						panel_1.add(btnCard);
					}
					{
						JButton btnCash = new JButton("현금");
						panel_1.add(btnCash);
					}
				}
				{
					JPanel panel_1 = new JPanel();
					panel_orderTable.add(panel_1, BorderLayout.NORTH);
					{
						JButton btnNewButton = new JButton("주문내역");
						btnNewButton.setBackground(new Color(255, 255, 255));
						btnNewButton.setOpaque(true);
						panel_1.add(btnNewButton);
					}
				}
			} // 오른쪽 주문 내역 테이블
			{ // 왼쪽
				JPanel panel_center = new JPanel();
				panel_center.setBackground(new Color(255, 245, 238));
				panel.add(panel_center, BorderLayout.CENTER);
				panel_center.setLayout(new BorderLayout(0, 0));
				{
					JPanel panel_classBar = new JPanel();
					panel_center.add(panel_classBar, BorderLayout.NORTH);
					{
							btnBeverage = new JButton("음료");
							btnBeverage.addActionListener(new ActionListener() {
								public void actionPerformed(java.awt.event.ActionEvent e) {
									
									Class.forName("com.mysql.cj.jdbc.Driver");
									String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
									Connection con = DriverManager.getConnection(strCon);
									Statement stmt = con.createStatement();
									
									String sql = "select count(*) from menuTBL where mclass='음료'";
									ResultSet rs = stmt.executeQuery(sql);
									
									if(rs.next()) {
										count= rs.getInt(1);
									}
									
									sql = "select * from menuTBL where mclass='음료'";
									rs = stmt.executeQuery(sql);
									
									while(rs.next()) {
										menus = new Vector<>();
										for(int i=0;i<count;i++) 
											menus.add(rs.getString("mname"));
											menus.add(rs.getString("mprice"));		
									}
									
									
								}
							});
							
							panel_classBar.add(btnBeverage);
							btnDessert = new JButton("디저트");
							panel_classBar.add(btnDessert);
					}
					{
						JPanel panel_menuButtonBar = new JPanel();
						panel_menuButtonBar.setBorder(new LineBorder(null, 1, true));
						panel_center.add(panel_menuButtonBar, BorderLayout.CENTER);
						panel_menuButtonBar.setLayout(new GridLayout(4, 4, 10, 10));
						{
							JPanel panel_1 = new JPanel();
							panel_center.add(panel_1, BorderLayout.SOUTH);
							{
								JButton btnNewButton_1 = new JButton("◀");
								panel_1.add(btnNewButton_1);
							}
							{
								JLabel lblNewLabel = new JLabel("|");
								panel_1.add(lblNewLabel);
							}
							{
								JButton btnNewButton_2 = new JButton("▶");
								panel_1.add(btnNewButton_2);
							}
						}
						{
							
							for(int i=0;i<16;i++) {
								btnBeverage = new JButton("1");
								panel_menuButtonBar.add(btnBeverage);
								}

						}
						{

//									// 버튼을 클릭하면 오른쪽 테이블에 행 추가(메뉴명, 가격, 수량+1) 
//							
//							//for(int i=0;i<count;i++) {
//							
//									btnBeverage.addActionListener(new ActionListener() { // 음료 버튼 클릭
//									public void actionPerformed(ActionEvent e) {
//										String sql ="select * from menuTBL where mclass='음료'";
//										//viewMenu(sql);
//										
//										for(int i=0;i<16;i++) 
//											btnBeverage = new JButton("메뉴");
//							panel_menuButtonBar.add(btnBeverage);
//		
//											// DB-project_cafe.menutbl에서 데이터 불러오기
//											
//											Class.forName("com.mysql.cj.jdbc.Driver");
//											String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
//											Connection con = DriverManager.getConnection(strCon);
//											Statement stmt = con.createStatement();
//											
//											String sql = "select * from menuTBL";
//											ResultSet rs = stmt.executeQuery(sql);
//											
//											while(rs.next()) {
//												menus = new Vector<>();
//												for(i=0;i<3;i++) 
//													menus.add(rs.getString(i+1));			
////												rs.getString("mClass");
////												rs.getString("mname");
////												rs.getString("mprice");		
//												count++ ;
//											}
////											rs.close();
////											stmt.close();
////											con.close();
//											
//											
//											
//										String menu = "";
//										int price = 0;
//										int amount=0;
//
//										// 테이블에 데이터 넣기
//										String[] row = new String[3];
//										row[0]= menu;
//										row[1]= Integer.toString(price);
//										row[2]= Integer.toString(amount);
//										row[2]= Integer.toString(price*amount);
//										
//										dtm.addRow(row);
//										
//										
//										
//										//addMenu();// db에 추가
//									}
//								});
//								}
						}
					}
				}
			}
		}
}
}
