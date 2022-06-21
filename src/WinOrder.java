import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class WinOrder extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPanel panel_menuButtonBar = new JPanel();
	private JButton [] btnCommon = new JButton[16];
	private JButton btnBeverage;
	private JButton btnDessert;
	private int count=0;
	private Vector menus;
	private List<String> mclass = new ArrayList<String>(); // db에서 가져온 mclass
	private List<String> mname = new ArrayList<String>(); // db에서 가져온 mname
	private List<String> mprice = new ArrayList<String>(); // db에서 가져온 mprice
	//private String tclass; // 화면(테이블)에 표시되는 mclass
	private Vector order; // 화면(테이블)에 들어갈 주문 내역
	private Vector orderList; // db에 들어갈 실제 주문 내역
	private Vector<String> columnNames = new Vector<>();
	private JTable table=new JTable();
	private DefaultTableModel dtm;
	private JButton [] btnmenu = new JButton[16];
	private int amount=0; // 메뉴 버튼을 클릭했을 때 수량(+1)
	private int total; // 총금액
	private int stamp; // 적립할 스탬프 개수
	private JTextField txtTotal; // 총 금액
	private JTextField txtCharge; // 받은 금액
	private JTextField txtChange; // 거스름돈
	private JTextField txtDC; // 할인 금액
	private Font font;
	private String uphone;
	private String ustamp;

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
		font = new Font("나눔스퀘어_ac", Font.PLAIN, 13);
		initGUI();
	}
	
	public void tblClear() { // 주문 내역 테이블 내용 지우기
		while(dtm.getRowCount() > 0){
			   for(int i = 0 ; i < dtm.getRowCount();i++){
				   stamp=0;
				   total=0;
				   txtTotal.setText(Integer.toString(total));
				   txtCharge.setText(Integer.toString(total));
				   txtChange.setText("");
				   dtm.removeRow(i);
			   }
			}
	}
	
	public void initGUI() throws ClassNotFoundException, SQLException {
		Dimension dimension = new Dimension(100, 100);
		setTitle("주문하기");
		setBounds(100, 100, 966, 675);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem popmnuPlus = new JMenuItem("수량 추가");
		JMenuItem popmnuDel = new JMenuItem("메뉴 삭제");
		JMenuItem popmnuAllDel = new JMenuItem("모두 삭제");
		popupMenu.add(popmnuPlus);
		popmnuPlus.addActionListener(new ActionListener() { // 수량 추가 버튼 클릭하면 수량이 +1
			public void actionPerformed(ActionEvent e) {
				int rowIndex = table.getSelectedRow(); // 선택한 행
				int accamount = Integer.parseInt(table.getValueAt(rowIndex,3).toString());
				int tPrice = Integer.parseInt(table.getValueAt(rowIndex,2).toString());
				table.setValueAt(accamount+1, rowIndex, 3);
				table.setValueAt((accamount+1)*tPrice, rowIndex, 4);
				
				total=total+tPrice;
				//System.out.println(total);
				txtTotal.setText(Integer.toString(total));
				
			}
		});
		popupMenu.add(popmnuDel);
		popmnuDel.addActionListener(new ActionListener() { // 메뉴 삭제 버튼 클릭하면 해당 메뉴 삭제
			public void actionPerformed(ActionEvent e) {
				int rowIndex = table.getSelectedRow(); 
				int ttotal = Integer.parseInt(String.valueOf(table.getValueAt(rowIndex, 4)));
				
				
				total=total-ttotal;
				//System.out.println(total);
				txtTotal.setText(Integer.toString(total));
				
				dtm.removeRow(rowIndex); 
			}
		});
		popupMenu.add(popmnuAllDel);
		popmnuAllDel.addActionListener(new ActionListener() { // 모두 삭제 버튼 클릭하면 테이블에 있는 데이터 모두 삭제
			public void actionPerformed(ActionEvent e) {
				while(dtm.getRowCount() > 0){
					   for(int i = 0 ; i < dtm.getRowCount();i++){
						   total=0;
						   txtTotal.setText(Integer.toString(total));
						   dtm.removeRow(i);
					   }
					}		
			}
		});
		
		
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(5, 0));
			{ // 오른쪽 주문 내역 테이블
				JPanel panel_orderTable = new JPanel(); 
				panel.add(panel_orderTable, BorderLayout.EAST);
				panel_orderTable.setLayout(new BorderLayout(0, 5));
				{
					JScrollPane scrollPane = new JScrollPane();
					panel_orderTable.add(scrollPane, BorderLayout.CENTER);
					{
						columnNames = new Vector<>();
						columnNames.add("구분");
						columnNames.add("메뉴명");
						columnNames.add("가격");
						columnNames.add("수량");
						columnNames.add("합계");

						order = new Vector<>();
						
						// 버튼 클릭하면 데이터 추가 됨
						
						
						dtm = new DefaultTableModel(order,columnNames);
						table = new JTable(dtm);
						table.setFont(font);
						table.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseReleased(MouseEvent e) {
				                	if (e.getButton() == 3) {
				                	popmnuPlus.setEnabled(true);
				                	popmnuDel.setEnabled(true);
				                	popmnuAllDel.setEnabled(true);
				                	popupMenu.show(e.getComponent(), e.getX(), e.getY());
				                }
							}
						});
						
						scrollPane.setViewportView(table);
					}
				}
				{
					JPanel panel_1 = new JPanel();
					panel_orderTable.add(panel_1, BorderLayout.SOUTH);
					panel_1.setLayout(new BorderLayout(0, 0));
					{
						JPanel panel_2 = new JPanel();
						panel_1.add(panel_2, BorderLayout.WEST);
						panel_2.setLayout(new GridLayout(0, 2, 0, 0));
						{
							JLabel lblNewLabel_2 = new JLabel("총 금액 :");
							lblNewLabel_2.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 14));
							panel_2.add(lblNewLabel_2);
						}
						{
							txtTotal = new JTextField("0");
							txtTotal.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 14));
							txtTotal.setEditable(false);
							txtTotal.setColumns(10);
							panel_2.add(txtTotal);
						}
						{
							JLabel lblNewLabel_2 = new JLabel("할인 금액 :");
							lblNewLabel_2.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 14));
							panel_2.add(lblNewLabel_2);
						}
						{
							txtDC = new JTextField("0");
							txtDC.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 14));
							txtDC.setEditable(false);
							txtDC.setColumns(15);
							panel_2.add(txtDC);
						}
						{
							JLabel lblNewLabel_3 = new JLabel("받은 금액 :");
							lblNewLabel_3.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 14));
							panel_2.add(lblNewLabel_3);
						}
						{
							txtCharge = new JTextField("0");
							txtCharge.setEditable(false);

							txtCharge.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 14));
							panel_2.add(txtCharge);
							txtCharge.setColumns(10);
						}
						{
							JLabel lblNewLabel_4 = new JLabel("거스름돈 :");
							lblNewLabel_4.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 14));
							panel_2.add(lblNewLabel_4);
						}
						{
							txtChange = new JTextField("0");
							txtChange.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 14));
							txtChange.setEditable(false);
							panel_2.add(txtChange);
							txtChange.setColumns(10);
						}
					}
					{
						JPanel panel_2 = new JPanel();
						panel_1.add(panel_2, BorderLayout.CENTER);
						panel_2.setLayout(new GridLayout(3, 0, 0, 0));
						{
							JButton btnCard = new JButton("카드");
							btnCard.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
// 카드 결제 시작 ================================================================================================
									txtCharge.setText(txtTotal.getText());
									int result = JOptionPane.showConfirmDialog(null, "적립하시겠습니까?","스탬프 적립",JOptionPane.YES_NO_CANCEL_OPTION);
									int cnt =0;
									String sql;
									
									if(result == JOptionPane.YES_OPTION) { // 예
										String sphone = JOptionPane.showInputDialog("적립할 핸드폰 번호를 입력하세요.");
										
										
										// 핸드폰 번호(11자리)를 정확히 입력하지 않은 경우
										// 입력할 때까지
										while(sphone.length() != 11) {
											JOptionPane.showMessageDialog(null, "핸드폰 번호를 정확히 입력해주세요.");
											sphone = JOptionPane.showInputDialog("적립할 핸드폰 번호를 입력하세요.");											
										}
										
										
										if (sphone.length() == 11) { // 핸드폰 번호를 입력한 경우
											/*
											 insert membertbl
											 음료 1잔당 스탬프 1개 적립
											 핸드폰 번호 입력
											 * 
											 */
											
											for(int i = 0 ; i < dtm.getRowCount();i++){ // 주문 내역 테이블 데이터 불러와서
												// table.getValueAt(i,3).toString().equal("음료") 인 것만 stamp++;
												
												if (table.getValueAt(i,0).equals("음료")) {
//													System.out.println(table.getValueAt(i,1));
													stamp++;
//													System.out.println("스탬프" + stamp);
												}
												
//												int accamount = Integer.parseInt(table.getValueAt(i,3).toString());
//												cnt = cnt+accamount;
											}
											sql = "insert into membertbl values ('" + sphone+ "'," + stamp +",sysdate())";
											System.out.println(sql);
											try {
												insertmembertbl(sql, stamp);
											} catch (ClassNotFoundException | SQLException e2) {
												// TODO Auto-generated catch block
												e2.printStackTrace();
											}
											
											/*
											 salestbl 
											 snum(순번) // null
											 stotal(합계) //  Integer.parseInt(txtTotal.getText());
											 sphone(핸드폰번호) // sphone
											 날짜 //curdate()
											 결제수단(카드) //1
											 insert into salestbl values ();
											 * 
											 */
											
											sql ="insert into salestbl values (null," + Integer.parseInt(txtTotal.getText()) + ",'" +sphone+ "',sysdate(),'카드')";
											System.out.println(sql);
											
											try {
												insertsalestbl(sql);
											} catch (ClassNotFoundException | SQLException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
		
											/*
											 ordertbl
											 메뉴명 //table.getValueAt(i,1).toString();
											 가격 //Integer.parseInt(table.getValueAt(i,2).toString());
											 수량 //Integer.parseInt(table.getValueAt(i,3).toString());
											 합계 //Integer.parseInt(table.getValueAt(i,4).toString());
											 날짜 //curdate()
											 핸드폰번호 //sphone
											 결제수단(카드) //1
											 insert into ordertbl values ();
											 * 
											 */
											
											for(int i = 0 ; i < dtm.getRowCount();i++){ // 주문 내역 테이블 데이터 불러와서
												
												sql ="insert into ordertbl values ('"+table.getValueAt(i,1).toString()+"',"+ Integer.parseInt(table.getValueAt(i,2).toString()) +","+ Integer.parseInt(table.getValueAt(i,3).toString()) +"," + Integer.parseInt(table.getValueAt(i,4).toString()) +",sysdate(),'"+ sphone +"','카드');";
												try {
													insertordertbl(sql);
												} catch (ClassNotFoundException | SQLException e1) {
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
											}
											
									}else {

									}
										tblClear();	
									}else if(result == JOptionPane.NO_OPTION) { // 아니오
											
										/*
										 salestbl 
										 snum(순번) // null
										 stotal(합계) //  Integer.parseInt(txtTotal.getText()) ;
										 sphone(핸드폰번호) // 일괄 1 입력
										 날짜 //curdate()
										 결제수단(카드) //1
										 insert into salestbl values ();
										 * 
										 */
										sql ="insert into salestbl(snum,stotal,sdate,spay) values (null," + Integer.parseInt(txtTotal.getText()) + ",sysdate(),'카드')";
										System.out.println(sql);
										
										try {
											insertsalestbl(sql);
										} catch (ClassNotFoundException | SQLException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}

		
										/*
										 ordertbl
										 메뉴명 //table.getValueAt(i,1).toString();
										 가격 //Integer.parseInt(table.getValueAt(i,2).toString());
										 수량 //Integer.parseInt(table.getValueAt(i,3).toString());
										 합계 //Integer.parseInt(table.getValueAt(i,4).toString());
										 날짜 //curdate()
										 핸드폰번호 // 일괄 1 입력
										 결제수단(카드) //1
										 insert into ordertbl values ();
										 * 
										 */
										
										for(int i = 0 ; i < dtm.getRowCount();i++){ // 주문 내역 테이블 데이터 불러와서
											
											sql ="insert into ordertbl (menu, price, amount,total, mdate, pay) values ('"+table.getValueAt(i,1).toString()+"',"+ Integer.parseInt(table.getValueAt(i,2).toString()) +","+ Integer.parseInt(table.getValueAt(i,3).toString()) +"," + Integer.parseInt(table.getValueAt(i,4).toString()) +",sysdate(),'카드');";
											try {
												insertordertbl(sql);
											} catch (ClassNotFoundException | SQLException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
										}
										tblClear();
									}else { // 버튼 선택 안 할 경우
										// 되돌아가기
									}	
								}								
							});
//  카드 결제 끝 ================================================================================================
							btnCard.setFont(new Font("나눔스퀘어 Bold", Font.PLAIN, 12));
							panel_2.add(btnCard);
						}
						{
							JButton btnCash = new JButton("현금");
							btnCash.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									String cash = JOptionPane.showInputDialog("받은 금액 : ");
									if(Integer.parseInt(txtTotal.getText())+Integer.parseInt(txtDC.getText())>Integer.parseInt(cash)) {
										JOptionPane.showMessageDialog(null, "받은 금액이 받아야 할 총 금액보다 적습니다. 받은 금액을 확인해주세요.");
									}else {
// 현금 결제 시작 ================================================================================================
										txtCharge.setText(cash);
										int Change = Integer.parseInt(txtCharge.getText())-Integer.parseInt(txtTotal.getText())+Integer.parseInt(txtDC.getText());
										
										txtChange.setText(Integer.toString(Change)); // 거스름돈
										
										int result = JOptionPane.showConfirmDialog(null, "적립하시겠습니까?","스탬프 적립",JOptionPane.YES_NO_CANCEL_OPTION);
										int cnt =0;
										String sql;
										
										if(result == JOptionPane.YES_OPTION) { // 예
											String sphone = JOptionPane.showInputDialog("적립할 핸드폰 번호를 입력하세요.");
											
											
											// 핸드폰 번호(11자리)를 정확히 입력하지 않은 경우
											// 입력할 때까지
											while(sphone.length() != 11) {
												JOptionPane.showMessageDialog(null, "핸드폰 번호를 정확히 입력해주세요.");
												sphone = JOptionPane.showInputDialog("적립할 핸드폰 번호를 입력하세요.");											
											}
											
											
											if (sphone.length() == 11) { // 핸드폰 번호를 입력한 경우
												/*
												 insert membertbl
												 음료 1잔당 스탬프 1개 적립
												 핸드폰 번호 입력
												 * 
												 */
												
												for(int i = 0 ; i < dtm.getRowCount();i++){ // 주문 내역 테이블 데이터 불러와서
													// table.getValueAt(i,3).toString().equal("음료") 인 것만 stamp++;
													
													if (table.getValueAt(i,0).equals("음료")) {
														System.out.println(table.getValueAt(i,1));
														stamp++;
														System.out.println("스탬프" + stamp);
													}
													
	//												int accamount = Integer.parseInt(table.getValueAt(i,3).toString());
	//												cnt = cnt+accamount;
												}
												sql = "insert into membertbl values ('" + sphone+ "'," + stamp +",sysdate())";
												System.out.println(sql);
												try {
													insertmembertbl(sql, stamp);
												} catch (ClassNotFoundException | SQLException e2) {
													// TODO Auto-generated catch block
													e2.printStackTrace();
												}
												
												/*
												 salestbl 
												 snum(순번) // null
												 stotal(합계) //  Integer.parseInt(txtTotal.getText());
												 sphone(핸드폰번호) // sphone
												 날짜 //curdate()
												 결제수단(현금) //2
												 insert into salestbl values ();
												 * 
												 */
												
												sql ="insert into salestbl values (null," + Integer.parseInt(txtTotal.getText()) + ",'" +sphone+ "',sysdate(),'현금')";
												System.out.println(sql);
												
												try {
													insertsalestbl(sql);
												} catch (ClassNotFoundException | SQLException e1) {
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
			
												/*
												 ordertbl
												 메뉴명 //table.getValueAt(i,1).toString();
												 가격 //Integer.parseInt(table.getValueAt(i,2).toString());
												 수량 //Integer.parseInt(table.getValueAt(i,3).toString());
												 합계 //Integer.parseInt(table.getValueAt(i,4).toString());
												 날짜 //curdate()
												 핸드폰번호 //sphone
												 결제수단(현금) //2
												 insert into ordertbl values ();
												 * 
												 */
												
												for(int i = 0 ; i < dtm.getRowCount();i++){ // 주문 내역 테이블 데이터 불러와서
													
													sql ="insert into ordertbl values ('"+table.getValueAt(i,1).toString()+"',"+ Integer.parseInt(table.getValueAt(i,2).toString()) +","+ Integer.parseInt(table.getValueAt(i,3).toString()) +"," + Integer.parseInt(table.getValueAt(i,4).toString()) +",sysdate(),'"+ sphone +"','현금');";
													try {
														insertordertbl(sql);
													} catch (ClassNotFoundException | SQLException e1) {
														// TODO Auto-generated catch block
														e1.printStackTrace();
													}
												}
												
										}else {
	
										}
											tblClear();	
										}else if(result == JOptionPane.NO_OPTION) { // 아니오
												
											/*
											 salestbl 
											 snum(순번) // null
											 stotal(합계) //  Integer.parseInt(txtTotal.getText()) ;
											 sphone(핸드폰번호) // 일괄 1 입력
											 날짜 //curdate()
											 결제수단(현금) //2
											 insert into salestbl values ();
											 * 
											 */
											sql ="insert into salestbl(snum,stotal,sdate,spay) values (null," + Integer.parseInt(txtTotal.getText()) + ",sysdate(),'현금')";
											System.out.println(sql);
											
											try {
												insertsalestbl(sql);
											} catch (ClassNotFoundException | SQLException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
	
			
											/*
											 ordertbl
											 메뉴명 //table.getValueAt(i,1).toString();
											 가격 //Integer.parseInt(table.getValueAt(i,2).toString());
											 수량 //Integer.parseInt(table.getValueAt(i,3).toString());
											 합계 //Integer.parseInt(table.getValueAt(i,4).toString());
											 날짜 //curdate()
											 핸드폰번호 // 일괄 1 입력
											 결제수단(현금) //2
											 insert into ordertbl values ();
											 * 
											 */
											
											for(int i = 0 ; i < dtm.getRowCount();i++){ // 주문 내역 테이블 데이터 불러와서
												
												sql ="insert into ordertbl (menu, price, amount,total, mdate, pay) values ('"+table.getValueAt(i,1).toString()+"',"+ Integer.parseInt(table.getValueAt(i,2).toString()) +","+ Integer.parseInt(table.getValueAt(i,3).toString()) +"," + Integer.parseInt(table.getValueAt(i,4).toString()) +",sysdate(),'현금');";
												try {
													insertordertbl(sql);
												} catch (ClassNotFoundException | SQLException e1) {
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
											}
											tblClear();
										}else { // 버튼 선택 안 할 경우
											// 되돌아가기
										}	
									}
								}
							});
//  현금 결제 끝 ================================================================================================
							btnCash.setFont(new Font("나눔스퀘어 Bold", Font.PLAIN, 12));
							panel_2.add(btnCash);
						}
						{
							JButton btnStamp = new JButton("스탬프");
							btnStamp.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									if(Integer.parseInt(txtTotal.getText()) > 0 ) {
										// 핸드폰 번호 입력하면 적립내역 확인
										WinStamp dlg= new WinStamp();
										dlg.setModal(true);
										dlg.setVisible(true);
										
										
										
										
									}else {
										JOptionPane.showMessageDialog(null, "3000원 이상 주문 시 사용 가능합니다.");
									}
								}
							});
							
							btnStamp.setFont(new Font("나눔스퀘어 Bold", Font.PLAIN, 12));
							panel_2.add(btnStamp);
						}
					}
				}
				{
					JPanel panel_1 = new JPanel();
					panel_orderTable.add(panel_1, BorderLayout.NORTH);
					{
						JLabel lblNewLabel_1 = new JLabel("주문 내역");
						lblNewLabel_1.setFont(new Font("나눔스퀘어 Bold", Font.BOLD, 17));
						panel_1.add(lblNewLabel_1);
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
// 음료 버튼 클릭 ================================================================================================						
							btnBeverage = new JButton("음료");
							btnBeverage.setFont(new Font("나눔스퀘어 Bold", Font.PLAIN, 12));
							btnBeverage.addActionListener(new ActionListener() { // 음료 버튼 클릭
								public void actionPerformed(java.awt.event.ActionEvent e) {
									mname.removeAll(mname);
									mprice.removeAll(mprice);
									
									// 원래의 버튼들을 삭제한다.
									Component[] componentList = panel_menuButtonBar.getComponents();
									for(Component c: componentList) {
										if(c instanceof JButton)
											panel_menuButtonBar.remove(c);
									}
									panel_menuButtonBar.revalidate();
									panel_menuButtonBar.repaint();
									
									for(int i=0;i<btnmenu.length;i++) {
										btnmenu[i] = new JButton(Integer.toString(i+1));
										btnmenu[i].setPreferredSize(dimension);
								        int x=i*60;
								        for(int j=0;j<4;j++) {
								        	int y=j*60;
								        	btnmenu[i].setBounds(x,y,50,50);
								        	panel_menuButtonBar.add(btnmenu[i]); 
								        }
									}
									
									try {
										Class.forName("com.mysql.cj.jdbc.Driver");
										String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
										Connection con = DriverManager.getConnection(strCon);
										Statement stmt = con.createStatement();
										
										String sql = "select count(*) from menuTBL where mclass='음료'";
										ResultSet rs = stmt.executeQuery(sql);
										
										if(rs.next()) {
											count= rs.getInt(1);
											//System.out.println(count);
										}
										sql = "select * from menuTBL where mclass='음료'";
										rs = stmt.executeQuery(sql);
											
										while(rs.next()) {
											mname.add(rs.getString("mname"));
											mprice.add(rs.getString("mprice"));	
										}
									} catch (ClassNotFoundException | SQLException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
									
									for(int i=0;i<count;i++) {
										String tclass = mclass.get(i);
										btnmenu[i].setText("<HTML><body style='text-align:center;'>"+mname.get(i)+"<br>"+mprice.get(i)+"</body></HTML>");


										btnmenu[i].addActionListener(new ActionListener() {
											
											@Override
											public void actionPerformed(ActionEvent e) { // 메뉴 버튼 클릭하면 메뉴명, 가격, 수량을 테이블에 입력

												JButton btn1 = (JButton) e.getSource();
												String[] choice = btn1.getText().split("<br>"); // 클릭한 버튼의 텍스트 가져오기
												String mmenu = choice[0].substring(39); // 클릭한 버튼의 메뉴명 가져오기
												String mprice = choice[1].substring(0,choice[1].indexOf("<")); // 클릭한 버튼의 가격 가져오기
											
		//  테이블에서 메뉴명 같은 것 찾기 ===================================================================										
												
												int rowIndex = 0;
														
												for(int i = 0 ; i < dtm.getRowCount();i++){
													if(table.getValueAt(i,1).equals(mmenu)) {
														rowIndex=i+1;
													}
												}
												
												if(rowIndex == 0) {
													amount++;
													order = new Vector<>();

													order.add(tclass);
													order.add(mmenu);
													order.add(mprice);
													order.add(amount);
													order.add(Integer.toString(Integer.parseInt(mprice)*amount));
													
													total=total+Integer.parseInt(mprice)*amount;
													//System.out.println(total);
													txtTotal.setText(Integer.toString(total));
													
													amount=0;														

													dtm.addRow(order);
												}else {
													rowIndex=rowIndex-1;
													int accamount = Integer.parseInt(table.getValueAt(rowIndex,3).toString());
													int tPrice = Integer.parseInt(table.getValueAt(rowIndex,2).toString());
													table.setValueAt(accamount+1, rowIndex, 3);
													table.setValueAt((accamount+1)*tPrice, rowIndex, 4);
													
													total=total+tPrice;
													txtTotal.setText(Integer.toString(total));
												}									
		//  테이블에서 메뉴명 같은 것 찾기 ===================================================================
											}
							
									});
									
								
									}
								}
							});
// 음료 버튼 끝 ================================================================================================	
							
					}
							{
// 전체 버튼 클릭 ================================================================================================
								JButton btnAll = new JButton("전체");
								btnAll.setFont(new Font("나눔스퀘어 Bold", Font.PLAIN, 12));
								btnAll.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										mname.removeAll(mname);
										mprice.removeAll(mprice);
										
										// 원래의 버튼들을 삭제한다.
										Component[] componentList = panel_menuButtonBar.getComponents();
										for(Component c: componentList) {
											if(c instanceof JButton)
												panel_menuButtonBar.remove(c);
										}
										panel_menuButtonBar.revalidate();
										panel_menuButtonBar.repaint();	
										
										for(int i=0;i<btnmenu.length;i++) {
											btnmenu[i] = new JButton(Integer.toString(i+1));
											btnmenu[i].setPreferredSize(dimension);
									        int x=i*60;
									        for(int j=0;j<4;j++) {
									        	int y=j*60;
									        	btnmenu[i].setBounds(x,y,50,50);
									        	panel_menuButtonBar.add(btnmenu[i]); 
									        }
										}
										
										try {
											Class.forName("com.mysql.cj.jdbc.Driver");
											String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
											Connection con = DriverManager.getConnection(strCon);
											Statement stmt = con.createStatement();

											String sql = "select count(*) from menuTBL";
											ResultSet rs = stmt.executeQuery(sql);
											
											if(rs.next()) {
												count= rs.getInt(1);
												//System.out.println(count);
											}
											
											 sql = "select * from menuTBL";
											 rs = stmt.executeQuery(sql);
												
											while(rs.next()) {
												mclass.add(rs.getString("mclass"));
												mname.add(rs.getString("mname"));
												mprice.add(rs.getString("mprice"));

											}
										} catch (ClassNotFoundException | SQLException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}

										for(int i=0;i<count;i++) {
											String tclass = mclass.get(i);
											btnmenu[i].setText("<HTML><body style='text-align:center;'>"+mname.get(i)+"<br>"+mprice.get(i)+"</body></HTML>");

											btnmenu[i].addActionListener(new ActionListener() {
												
												@Override
												public void actionPerformed(ActionEvent e) { // 메뉴 버튼 클릭하면 메뉴명, 가격, 수량을 테이블에 입력

													JButton btn1 = (JButton) e.getSource();
													String[] choice = btn1.getText().split("<br>"); // 클릭한 버튼의 텍스트 가져오기
													String mmenu = choice[0].substring(39); // 클릭한 버튼의 메뉴명 가져오기
													String mprice = choice[1].substring(0,choice[1].indexOf("<")); // 클릭한 버튼의 가격 가져오기
												
			//  테이블에서 메뉴명 같은 것 찾기 ===================================================================										
													
													int rowIndex = 0;
															
													for(int i = 0 ; i < dtm.getRowCount();i++){
														if(table.getValueAt(i,1).equals(mmenu)) {
															rowIndex=i+1;
														}
													}
													
													if(rowIndex == 0) {
														amount++;
														order = new Vector<>();

														order.add(tclass);
														order.add(mmenu);
														order.add(mprice);
														order.add(amount);
														order.add(Integer.toString(Integer.parseInt(mprice)*amount));
														
														total=total+Integer.parseInt(mprice)*amount;
														//System.out.println(total);
														txtTotal.setText(Integer.toString(total));
														
														amount=0;														

														dtm.addRow(order);
													}else {
														rowIndex=rowIndex-1;
														int accamount = Integer.parseInt(table.getValueAt(rowIndex,3).toString());
														int tPrice = Integer.parseInt(table.getValueAt(rowIndex,2).toString());
														table.setValueAt(accamount+1, rowIndex, 3);
														table.setValueAt((accamount+1)*tPrice, rowIndex, 4);
														
														total=total+tPrice;
														txtTotal.setText(Integer.toString(total));
													}									
			//  테이블에서 메뉴명 같은 것 찾기 ===================================================================
												}
											});
										}
									}
								});
// 전체 버튼 끝 ================================================================================================	
								
								panel_classBar.add(btnAll);
							}
							panel_classBar.add(btnBeverage);
							btnDessert = new JButton("디저트");
							btnDessert.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									mname.removeAll(mname);
									mprice.removeAll(mprice);
									int count1=0;
									// 원래의 버튼들을 삭제한다.
									Component[] componentList = panel_menuButtonBar.getComponents();
									for(Component c: componentList) {
										if(c instanceof JButton)
											panel_menuButtonBar.remove(c);
									}
									panel_menuButtonBar.revalidate();
									panel_menuButtonBar.repaint();
									
									
									for(int i=0;i<btnmenu.length;i++) {
										btnmenu[i] = new JButton(Integer.toString(i+1));
										btnmenu[i].setPreferredSize(dimension);
								        int x=i*60;
								        for(int j=0;j<4;j++) {
								        	int y=j*60;
								        	btnmenu[i].setBounds(x,y,50,50);
								        	panel_menuButtonBar.add(btnmenu[i]); 
								        }
									}
									
									try {
										Class.forName("com.mysql.cj.jdbc.Driver");
										String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
										Connection con = DriverManager.getConnection(strCon);
										Statement stmt = con.createStatement();
										
										String sql = "select count(*) from menuTBL where mclass='디저트'";
										ResultSet rs = stmt.executeQuery(sql);
										
										if(rs.next()) {
											count= rs.getInt(1);
										}
										sql = "select * from menuTBL where mclass='디저트'";
										rs = stmt.executeQuery(sql);

										while(rs.next()) {
											mname.add(rs.getString("mname"));
											mprice.add(rs.getString("mprice"));	
										}
									} catch (ClassNotFoundException | SQLException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
									for(int i=0;i<count;i++) {
										String tclass = mclass.get(i);
										btnmenu[i].setText("<HTML><body style='text-align:center;'>"+mname.get(i)+"<br>"+mprice.get(i)+"</body></HTML>");					
										
										btnmenu[i].addActionListener(new ActionListener() {
											
											@Override
											public void actionPerformed(ActionEvent e) { // 메뉴 버튼 클릭하면 메뉴명, 가격, 수량을 테이블에 입력

												JButton btn1 = (JButton) e.getSource();
												String[] choice = btn1.getText().split("<br>"); // 클릭한 버튼의 텍스트 가져오기
												String mmenu = choice[0].substring(39); // 클릭한 버튼의 메뉴명 가져오기
												String mprice = choice[1].substring(0,choice[1].indexOf("<")); // 클릭한 버튼의 가격 가져오기
											
		//  테이블에서 메뉴명 같은 것 찾기 ===================================================================										
												
												int rowIndex = 0;
														
												for(int i = 0 ; i < dtm.getRowCount();i++){
													if(table.getValueAt(i,1).equals(mmenu)) {
														rowIndex=i+1;
													}
												}
												
												if(rowIndex == 0) {
													amount++;
													order = new Vector<>();

													order.add(tclass);
													order.add(mmenu);
													order.add(mprice);
													order.add(amount);
													order.add(Integer.toString(Integer.parseInt(mprice)*amount));
													
													total=total+Integer.parseInt(mprice)*amount;
													//System.out.println(total);
													txtTotal.setText(Integer.toString(total));
													
													amount=0;														

													dtm.addRow(order);
												}else {
													rowIndex=rowIndex-1;
													int accamount = Integer.parseInt(table.getValueAt(rowIndex,3).toString());
													int tPrice = Integer.parseInt(table.getValueAt(rowIndex,2).toString());
													table.setValueAt(accamount+1, rowIndex, 3);
													table.setValueAt((accamount+1)*tPrice, rowIndex, 4);
													
													total=total+tPrice;
													txtTotal.setText(Integer.toString(total));
												}									
		//  테이블에서 메뉴명 같은 것 찾기 ===================================================================
												
											}
										});
									}
								}
							});
							btnDessert.setFont(new Font("나눔스퀘어 Bold", Font.PLAIN, 12));
							panel_classBar.add(btnDessert);
					}
					{
						panel_menuButtonBar = new JPanel();
						panel_menuButtonBar.setBorder(null);
						
						}
						
						panel_center.add(panel_menuButtonBar, BorderLayout.CENTER);
						FlowLayout fl_panel_menuButtonBar = new FlowLayout(FlowLayout.CENTER);
						fl_panel_menuButtonBar.setVgap(10);
						fl_panel_menuButtonBar.setHgap(10);
						panel_menuButtonBar.setLayout(fl_panel_menuButtonBar);
						
{
							
// 시작화면 ================================================================================================
							mname.removeAll(mname);
							mprice.removeAll(mprice);
							
							// 원래의 버튼들을 삭제한다.
							Component[] componentList = panel_menuButtonBar.getComponents();
							for(Component c: componentList) {
								if(c instanceof JButton)
									panel_menuButtonBar.remove(c);
							}
							panel_menuButtonBar.revalidate();
							panel_menuButtonBar.repaint();
							
							for(int i=0;i<btnmenu.length;i++) {
								btnmenu[i] = new JButton(Integer.toString(i+1));
								btnmenu[i].setPreferredSize(dimension);
						        int x=i*60;
						        for(int j=0;j<4;j++) {
						        	int y=j*60;
						        	btnmenu[i].setBounds(x,y,50,50);
						        	panel_menuButtonBar.add(btnmenu[i]); 
						        }
							}
							
							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
								Connection con = DriverManager.getConnection(strCon);
								Statement stmt = con.createStatement();

								String sql = "select count(*) from menuTBL";
								ResultSet rs = stmt.executeQuery(sql);
								
								if(rs.next()) {
									count= rs.getInt(1);
									//System.out.println(count);
								}
								
								 sql = "select * from menuTBL";
								 rs = stmt.executeQuery(sql);
									
								while(rs.next()) {
									mclass.add(rs.getString("mclass"));
									mname.add(rs.getString("mname"));
									mprice.add(rs.getString("mprice"));

								}
							} catch (ClassNotFoundException | SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							for(int i=0;i<count;i++) {
								String tclass = mclass.get(i);
								btnmenu[i].setText("<HTML><body style='text-align:center;'>"+mname.get(i)+"<br>"+mprice.get(i)+"</body></HTML>");

								btnmenu[i].addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent e) { // 메뉴 버튼 클릭하면 메뉴명, 가격, 수량을 테이블에 입력

										JButton btn1 = (JButton) e.getSource();
//										amount++;
										String[] choice = btn1.getText().split("<br>"); // 클릭한 버튼의 텍스트 가져오기
										String mmenu = choice[0].substring(39); // 클릭한 버튼의 메뉴명 가져오기
										String mprice = choice[1].substring(0,choice[1].indexOf("<")); // 클릭한 버튼의 가격 가져오기
									
										
										/* 메뉴 추가

										
										order = new Vector<>();

										
										order.add(tclass);
										order.add(mmenu);
										order.add(mprice);
										order.add(amount);
										order.add(Integer.toString(Integer.parseInt(mprice)*amount));
										
										total=total+Integer.parseInt(mprice)*amount;
										//System.out.println(total);
										txtTotal.setText(Integer.toString(total));
										
										amount=0;														

										dtm.addRow(order);
										
								 		*/
//  테이블에서 메뉴명 같은 것 찾기 ===================================================================										
										
										int rowIndex = 0;
												
										for(int i = 0 ; i < dtm.getRowCount();i++){
											if(table.getValueAt(i,1).equals(mmenu)) {
												rowIndex=i+1;
											}
										}
										
										if(rowIndex == 0) {
											amount++;
											order = new Vector<>();

											order.add(tclass);
											order.add(mmenu);
											order.add(mprice);
											order.add(amount);
											order.add(Integer.toString(Integer.parseInt(mprice)*amount));
											
											total=total+Integer.parseInt(mprice)*amount;
											//System.out.println(total);
											txtTotal.setText(Integer.toString(total));
											
											amount=0;														

											dtm.addRow(order);
										}else {
											rowIndex=rowIndex-1;
											int accamount = Integer.parseInt(table.getValueAt(rowIndex,3).toString());
											int tPrice = Integer.parseInt(table.getValueAt(rowIndex,2).toString());
											table.setValueAt(accamount+1, rowIndex, 3);
											table.setValueAt((accamount+1)*tPrice, rowIndex, 4);
											
											total=total+tPrice;
											txtTotal.setText(Integer.toString(total));
										}									
//  테이블에서 메뉴명 같은 것 찾기 ===================================================================
			
			}
		});
	}
	// 시작화면 ================================================================================================						}
}

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

						}			
					}
				}

	protected void insertordertbl(String sql) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project_cafe?user=root&password=1234");
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);

		stmt.close();
		con.close();	
		
	}

	private void insertsalestbl(String sql) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project_cafe?user=root&password=1234");
		Statement stmt = con.createStatement();

		try {
			if(stmt.executeUpdate(sql) >= 1) {
				JOptionPane.showMessageDialog(null, "결제 완료");
			}else
				JOptionPane.showMessageDialog(null, "결제 오류");
		} catch (HeadlessException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		stmt.close();
		con.close();		
	}
	
	protected void insertmembertbl(String sql, int stamp) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/project_cafe?user=root&password=1234");
		Statement stmt = con.createStatement();
		
		try {
			if(stmt.executeUpdate(sql) >= 1) {
				JOptionPane.showMessageDialog(null, stamp + "개 적립 완료");
			}else
				JOptionPane.showMessageDialog(null, "적립 오류");
		} catch (HeadlessException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		stmt.close();
		con.close();
		
	}
	
	
//	public void useStamp(String uphone, String ustamp) {
//		this.uphone = uphone;
//		this.ustamp = ustamp;
//		
//	}
	
}



/*
 * 


if(table.getColumn(0).getHeaderValue() != null) { // 테이블에 이미 해당 메뉴가 있으면 수량 +1
	System.out.println(table.getColumn(0).getHeaderValue());
	
	 * table.getColumn("메뉴명").getIdentifier()
	
    for(Vector<String> in : outer){
        if(word.equals(in.get(index))){
            sresult.add(in);
        }
    }
    // 검색결과 체크후 테이블에 표시
    if(sresult.size()>0){
        model.setDataVector(sresult, title);
    }else{
        showMessage("검색 결과가 없습니다.");
    }
}
 

	
	
	table.getColumn(mmenu);
	
	int rowIndex = table.getSelectedRow(); 
	
	int accamount = Integer.parseInt(table.getValueAt(rowIndex,2).toString());
	int tPrice = Integer.parseInt(table.getValueAt(rowIndex,1).toString());
	table.setValueAt(accamount+1, rowIndex, 2);
	table.setValueAt((accamount+1)*tPrice, rowIndex, 3);
	
	
	
	 
}else {
		order = new Vector<>();
		order.add(mmenu);
		order.add(mprice);
		order.add(amount);
		order.add(Integer.toString(Integer.parseInt(mprice)*amount));
		amount=0;														
		
		dtm.addRow(order);
	}
	 */		
