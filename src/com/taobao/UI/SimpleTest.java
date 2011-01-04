package com.taobao.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SimpleTest extends JFrame {
	private ImageIcon image;
	private JLabel label;
	private JButton button;
	private JPanel buttonPanel, imagePanel;
	private JScrollPane scrollPane;

	public SimpleTest() {
		super("Add Image");

		button = new JButton("Add Image");
		button.setPreferredSize(new Dimension(80, 25));
		button.setMargin(new Insets(0, 5, 0, 5));

		image = new ImageIcon(
				"C:/Users/lenovo/Desktop/T1vMhVXX0yXXagIWvX_114212.jpg_b.jpg");
		imagePanel = new JPanel(new MyFlowLayout(FlowLayout.LEFT, 5, 5));
		scrollPane = new JScrollPane(imagePanel);
		// imagePanel.setPreferredSize(new Dimension(xPixels,
		// yPixels));//这是关键的2句
		// scrollPane.setPreferredSize(new Dimension(xPixels, yPixels));

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String xml = "<html><img src='http://img03.taobaocdn.com/bao/uploaded/i3/T1ax8UXdVJXXabVN75_055914.jpg_b.jpg'/><br/><a href='http://www.baidu.com'>搜索的关<FONT COLOR=red>要在在</FONT>搜索的关键字搜索的关键字搜索的关键字搜索的关键字搜索的关键字搜索的关键字</a></html>"; 
				label = new JLabel(xml);
				label.setPreferredSize(new Dimension(220,280));
				label.setSize(new Dimension(220,280));
				label.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
				imagePanel.add(label);
				/*StringBuilder builder = new StringBuilder();
			    char[] chars = xml.toCharArray();
			    FontMetrics fontMetrics = label.getFontMetrics(label.getFont());
			    for (int beginIndex = 0, limit = 1;; limit++) {
			        System.out.println(beginIndex + " " + limit + " " + (beginIndex + limit));
			        if (fontMetrics.charsWidth(chars, beginIndex, limit) < label.getWidth()) {
			            if (beginIndex + limit < chars.length) {
			                continue;
			            }
			            builder.append(chars, beginIndex, limit);
			            break;
			        }
			        builder.append(chars, beginIndex, limit - 1).append("<br/>");
			        beginIndex += limit - 1;
			        limit = 1;
			    }
			   // builder.append("</html>");
			    label.setText(builder.toString());*/
				validate();
			}
		});

		buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		buttonPanel.add(button);

		add(buttonPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(300, 400);
		setVisible(true);
	}

	public static void main(String[] args) {
		new SimpleTest();
	}
}