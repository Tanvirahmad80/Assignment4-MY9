
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.data.category.DefaultCategoryDataset;

public class MyApp extends JFrame {

	private JPanel mainPane;
	private static final long serialVersionUID = 1L;
	private JFreeChart lineChart;
	private ChartPanel chartPanel;

	public MyApp() {

		setTitle("Line Chart");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 450);
		mainPane = new JPanel();
		mainPane.setBackground(Color.WHITE);
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPane);
		mainPane.setLayout(null);

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		HashMap<String, Double> genresData = getGenresData();
		Iterator<String> keys = genresData.keySet().iterator();
		while (keys.hasNext()) {

			String genre = keys.next();
			double value = genresData.get(genre);
			dataset.addValue(value, "Genre", genre);

		}

		lineChart = ChartFactory.createLineChart("Genres Data", "Genre", "No. of Movies", dataset);
		chartPanel = new ChartPanel(lineChart);
		chartPanel.setBounds(6, 6, 788, 416);
		getContentPane().add(chartPanel);
		
		chartPanel.addChartMouseListener(new ChartMouseListener() {

			@Override
			public void chartMouseClicked(ChartMouseEvent event) {
				
				ChartEntity entity = event.getEntity();
				if(entity.getToolTipText() != null) {
					String line = entity.getToolTipText().trim();
					String genre = line.substring(line.indexOf(' ') + 1, line.indexOf(')'));
					String count = line.substring(line.lastIndexOf(' ') + 1);
					JOptionPane.showMessageDialog(null, "Genre: "+genre+"\nNo. of Movies: "+count);
				}
			}

			@Override
			public void chartMouseMoved(ChartMouseEvent arg0) {}
			
		});

	}

	public HashMap<String, Double> getGenresData() {

		HashMap<String, Double> genresData = new HashMap<>();
		String filename = "data/movies.csv";
		// Reading the file.
		String[] tokens = null;
		try {

			Scanner scan = new Scanner(new File(filename));
			scan.nextLine();
			// reading.
			while (scan.hasNextLine()) {

				try {
					tokens = split(scan.nextLine());
					String genres = tokens[2];
					genres = genres.replaceAll("\"", "").trim();
					String[] genresToken = genres.split("\\|");
					for(String genre: genresToken) {
						if(genresData.containsKey(genre)) {
							genresData.replace(genre, genresData.get(genre) + 1);
						}else {
							genresData.put(genre, 1.0);
						}
					}
					
				} catch (Exception e) {
				}

			}
			scan.close();

		} catch (Exception e) {
			System.out.println(Arrays.toString(tokens));
			e.printStackTrace();
		}
		
		return genresData;

	}
	
	public String[] split(String data) {
		
		ArrayList<String> tokens = new ArrayList<String>();
		boolean isComma = true;
		int start = 0;
		for (int i = 0; i < data.length() - 1; i++) {
			if (data.charAt(i) == ',' && isComma) {
				tokens.add(data.substring(start, i));
				start = i + 1;
			} else if (data.charAt(i) == '"')
				isComma = !isComma;
		}
		tokens.add(data.substring(start));
		return tokens.toArray(new String[] {});
	
	}

	public static void main(String[] args) {

		new MyApp().setVisible(true);
		;

	}

}
