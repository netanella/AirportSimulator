import java.awt.BorderLayout;
import java.util.Scanner;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;

public class myGUI extends JFrame {

	private JPanel contentPane;
	private JTextField techNum;
	private JTextField workTime;

	public static Scanner sc = new Scanner(System.in);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					myGUI frame = new myGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public myGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 502, 348);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNumberOfTechnical = new JLabel("number of technical crews");
		lblNumberOfTechnical.setBounds(55, 105, 194, 20);
		contentPane.add(lblNumberOfTechnical);

		JLabel lblWorkTimeFor = new JLabel("work time for security");
		lblWorkTimeFor.setBounds(289, 105, 174, 20);
		contentPane.add(lblWorkTimeFor);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent exit) {
				System.exit(0);
			}
		});
		btnExit.setBounds(306, 232, 115, 29);
		contentPane.add(btnExit);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent start) {
				Airport air = new Airport(); //create new airport
				String techCrewNum = techNum.getText();
				String secWorkTime = workTime.getText();
				if(techCrewNum.contains(".") || secWorkTime.contains(".")){ //if input is not an integer
					System.out.println("The input is invalid. Please try again");
				}
				else{
					try{ //convert string to integer
						int techCrewNumber = Integer.parseInt(techNum.getText());
						long securityWorkTime = Long.parseLong(workTime.getText());
						air.newDay(techCrewNumber,securityWorkTime);
						
					}catch(NumberFormatException e){ //input is not a number
						System.out.println("The input is invalid. Please try again");
					}
				}

			}
		});
		btnStart.setBounds(55, 232, 115, 29);
		contentPane.add(btnStart);

		JSeparator separator = new JSeparator();
		separator.setBounds(101, 78, 259, 2);
		contentPane.add(separator);

		JLabel lblWelcomeToNetanella = new JLabel("Welcome to Netanella & Dovi's airport!");
		lblWelcomeToNetanella.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblWelcomeToNetanella.setBounds(44, 42, 421, 20);
		contentPane.add(lblWelcomeToNetanella);

		techNum = new JTextField();
		techNum.setText("1");
		techNum.setBounds(46, 149, 146, 26);
		contentPane.add(techNum);
		techNum.setColumns(10);

		workTime = new JTextField();
		workTime.setText("2");
		workTime.setBounds(288, 149, 146, 26);
		contentPane.add(workTime);
		workTime.setColumns(10);
	}
}
