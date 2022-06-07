import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.ScrollPaneConstants;

public class WinViewOrder extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private Vector<String> columnNames = new Vector<>();
	private Vector sales;
	private DefaultTableModel dtm;
	private JLabel lblCount;
	private JLabel lblTotal;
	private String [] days = {"0","31","28","31","30","31","30","31","31","30","31","30","31"};


	/**
	 * Create the frame.
	 */
	public WinViewOrder() {
		Font font = new Font("나눔스퀘어_ac", Font.PLAIN, 13);
		setTitle("주문 조회");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 672, 540);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel_north = new JPanel();
		contentPane.add(panel_north, BorderLayout.NORTH);
		
		JComboBox comboBoxYear = new JComboBox();
		comboBoxYear.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
		comboBoxYear.setModel(new DefaultComboBoxModel(new String[] {"2022"}));
		panel_north.add(comboBoxYear);
		
		JLabel lblNewLabel = new JLabel("년");
		lblNewLabel.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 13));
		panel_north.add(lblNewLabel);
		
		JComboBox comboBoxMonth  = new JComboBox();
		JComboBox comboBoxDay = new JComboBox();
		comboBoxMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 월을 선택했을 때 날짜가 콤보박스에 나오게
				comboBoxDay.removeAllItems();
				comboBoxDay.addItem("전체");
				int day = Integer.parseInt(days[comboBoxMonth.getSelectedIndex()]);
				System.out.println(day);
					for(int i=0;i<day;i++) {
						comboBoxDay.addItem(i+1);
					}
			}
		});
		comboBoxMonth.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
		comboBoxMonth.setModel(new DefaultComboBoxModel(new String[] {"전체", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));
		panel_north.add(comboBoxMonth);
		
		JLabel lblNewLabel_1 = new JLabel("월");
		lblNewLabel_1.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
		panel_north.add(lblNewLabel_1);
		

		comboBoxDay.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
		panel_north.add(comboBoxDay);
		
		JLabel lblNewLabel_3 = new JLabel("일");
		lblNewLabel_3.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
		panel_north.add(lblNewLabel_3);
		
		JButton btnSearch = new JButton("조회");
		btnSearch.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				while(dtm.getRowCount() > 0){ // 테이블 내 데이터 전체 삭제
				   for(int i = 0 ; i < dtm.getRowCount();i++){
					   dtm.removeRow(i);
				   }
				}
				String serch = "";
				String sYear = (String) comboBoxYear.getSelectedItem(); // 년
				String sMonth = (String) comboBoxMonth.getSelectedItem(); // 월
				String sDay = String.valueOf(comboBoxDay.getSelectedItem()); // 일
				
				if(!sMonth.equals("전체")) {// 월이 전체가 아닐 떄		
					if(Integer.parseInt(sMonth)<10)
						sMonth="-0" +sMonth;	
					if(Integer.parseInt(sMonth)>10)
						sMonth="-" +sMonth;
					if(!sDay.equals("전체")) {// 일이 전체가 아닐 떄	
						if (Integer.parseInt(sDay)<10)
							sDay="-0" +sDay;	
						if(Integer.parseInt(sDay)>10) 
							sDay="-" +sDay;
					}else {
						sDay="";
					}
				}else {
					sMonth="";
					sDay="";
				}
				serch = sYear+sMonth+sDay;
				System.out.println(serch);

				
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
					Connection con = DriverManager.getConnection(strCon);
					Statement stmt = con.createStatement();
										
					String sql = "select mdate, menu, amount, total, pay from ordertbl where mdate like '"+ serch + "%'";
					ResultSet rs = stmt.executeQuery(sql);
					
					while(rs.next()) {

						sales = new Vector<>();
						for(int i=0;i<5;i++) 
							sales.add(rs.getString(i+1));		
						dtm.addRow(sales);
					}

				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 

				// 테이블 돌려서 금액 합계, 건수
				int cnt = 0;
				int sum = 0;
				for(int i = 0 ; i < dtm.getRowCount();i++){ // 주문 내역 테이블 데이터 불러와서
					cnt++;
					int temp = Integer.parseInt(table.getValueAt(i,3).toString());
					sum = sum + temp;
					}
//				System.out.println(cnt + "건");
//				System.out.println(sum + "원");
				
				String strSum =Integer.toString(sum);
				strSum = strSum.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
				lblCount.setText(Integer.toString(cnt)); // 건수
				lblTotal.setText(strSum); // 합계 금액
			}
		});

		panel_north.add(btnSearch);
		
		JPanel panel_center = new JPanel();
		contentPane.add(panel_center, BorderLayout.CENTER);
		panel_center.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel_center.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblNewLabel_2 = new JLabel("합계 :");
		lblNewLabel_2.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 14));
		panel.add(lblNewLabel_2);
		
		lblCount = new JLabel();
		lblCount.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 14));
		lblCount.setText("0");
		//lblCount.setText(Integer.toString(count)); // 건수(count)
		lblCount.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblCount);
		
		JLabel lblNewLabel_4 = new JLabel("건");
		lblNewLabel_4.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 14));
		panel.add(lblNewLabel_4);
		
		lblTotal = new JLabel();
		lblTotal.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 14));
		lblTotal.setText("0");
		//lblTotal.setText(Integer.toString(total));// 합계 금액(total)
		lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblTotal);
		
		JLabel lblNewLabel_6 = new JLabel("원");
		lblNewLabel_6.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 14));
		panel.add(lblNewLabel_6);
		
		JPanel panel_1 = new JPanel();
		panel_center.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_1.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		
		columnNames = new Vector<>();
		columnNames.add("날짜");
		columnNames.add("메뉴명");
		columnNames.add("수량");
		columnNames.add("금액");
		columnNames.add("지불"); // 현금, 카드

		sales = new Vector<>();
		
		
		dtm = new DefaultTableModel(sales,columnNames);
		table = new JTable(dtm);
		table.setFont(font);
//		table.getColumnModel().getColumn(0).setPreferredWidth(60);
//		table.getColumnModel().getColumn(1).setPreferredWidth(40);
//		table.getColumnModel().getColumn(2).setPreferredWidth(10);
//		table.getColumnModel().getColumn(3).setPreferredWidth(10);
//		table.getColumnModel().getColumn(4).setPreferredWidth(10);
		
		
		table.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(table);
		
		
		
		JPanel panel_south = new JPanel();
		contentPane.add(panel_south, BorderLayout.SOUTH);
	}

}
