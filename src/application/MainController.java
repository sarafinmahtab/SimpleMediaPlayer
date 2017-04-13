package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * @author Arafin
 *
 */

public class MainController implements Initializable {

	final FileChooser fileChooser = new FileChooser();
	final Timeline slideIn = new Timeline();
	final Timeline slideOut = new Timeline();
	
	@FXML
	private MediaView mediaView;
	private MediaPlayer mp;
	private Media me;
	@FXML
	private Slider volumeSilder;
	@FXML
	private Slider timeSlider;
	private Desktop desktop = Desktop.getDesktop();
	private String directory;
	@FXML
	private Label volumeLabel;
	@FXML
	private Label durationLabel;
	@FXML
	private VBox vbox;
	@FXML
	private MenuBar menuBar;
	
	private ObservableList<String> data;
	private ArrayList<File> arrayList = new ArrayList<File>();
	
	private int i = 0;
//	private int hours, minutes, seconds;
	
	public void fileChooser() {
		Window stage = mediaView.getScene().getWindow();
//      configureFileChooser(fileChooser);
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Video Files", "*.mp4", "*.mpeg"),
				new ExtensionFilter("Audio Files", "*.mp3"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			try {
				if(arrayList.size() != 0) {
					mp.stop();
				}
				
				desktop.open(selectedFile);
				directory = selectedFile.getAbsolutePath();
				
				playMedia(directory);
				
				arrayList.add(new File(directory));
//				System.out.println(arrayList.get(i).getName());
				i++;
				

			} catch (IOException ex) {
				Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
/*	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("Open Videos");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	}
*/
	
	@FXML
	public void exit(){
		System.exit(1);
		arrayList.clear();
	}
	
	@FXML
	public void play() {
		mp.play();
		mp.setRate(1);
	}

	@FXML
	public void pause() {
		mp.pause();
	}

	@FXML
	public void fast() {
		mp.setRate(2);
	}

	@FXML
	public void slow() {
		mp.setRate(0.5);
	}
	
	@FXML
	public void stop() {
		mp.seek(mp.getStartTime());
		mp.stop();
	}
	
	@FXML
	public void playlist() {
		
        final Stage videoList = new Stage();
        videoList.initModality(Modality.APPLICATION_MODAL);
        videoList.initOwner(Main.getStage());
                
        data = FXCollections.observableArrayList();
        
        for(int j = 0; j < arrayList.size(); j++) {
        	data.add(arrayList.get(j).getName());
        }
        
        ListView<String> list = new ListView<String>();
        list.setItems(data);
        
        StackPane stack = new StackPane();
        stack.getChildren().add(list);
        
        Scene dialogScene = new Scene(stack, 450, 200);
        videoList.setScene(dialogScene);
        videoList.setTitle("Playlist");
        videoList.getIcons().add(new Image("icon/Simple Media Player.png"));
        videoList.show();
        
/*        list.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				mp.stop();
				playMedia(arrayList.get(i).getAbsolutePath());
				mp.play();
			}
        	
        });*/
    }
	
	@FXML
	public void prevTrack() {
		if(i == 0) {
			mp.stop();
		} else if(i > 0) {
			i = i - 1;
			mp.stop();
			playMedia(arrayList.get(i).getAbsolutePath());
			mp.play();
		}
	}
	
	@FXML
	public void nextTrack() {
		if(i == arrayList.size()-1) {
			i = 0;
			mp.stop();
			playMedia(arrayList.get(0).getAbsolutePath());
			mp.play();
		} else if(i < arrayList.size()) {
			i = i + 1;
			mp.stop();
			playMedia(arrayList.get(i).getAbsolutePath());
			mp.play();
		}
	}
	
	public void playMedia(String path) {
		me = new Media(new File(path).toURI().toString());
		mp = new MediaPlayer(me);
		mediaView.setMediaPlayer(mp);
		
		mp.play();
		
// 		Main.getStage().setTitle("Now Playing " + arrayList.get(i).getName());
		
  		DoubleProperty width = mediaView.fitWidthProperty();
  		DoubleProperty height = mediaView.fitHeightProperty();
  		width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
  		height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
  		
		
		mp.setOnReady(new Runnable(){

			@Override
			public void run() {
				
//     			int w = mp.getMedia().getWidth();
//				int h = mp.getMedia().getHeight();
/*				
				Main.getStage().setMinWidth(w);
				Main.getStage().setMinHeight(h);
*/
				timeSlider.setMin(0);
				timeSlider.setValue(0);
				timeSlider.setMax(mp.getTotalDuration().toSeconds());
				
				//System.out.println(vbox.getPrefHeight());
				
//				Animation
				slideOut.getKeyFrames().addAll(
						new KeyFrame(new Duration(0),
								new KeyValue(menuBar.translateYProperty(), menuBar.getPrefHeight()-26),
								new KeyValue(menuBar.opacityProperty(), 0.9)
								),
						new KeyFrame(new Duration(300),
								new KeyValue(menuBar.translateYProperty(), menuBar.getPrefHeight()-26),
								new KeyValue(menuBar.opacityProperty(), 0.0)
								)
						);
				slideIn.getKeyFrames().addAll(
						new KeyFrame(new Duration(0),
								new KeyValue(menuBar.translateYProperty(), menuBar.getPrefHeight()-26),
								new KeyValue(menuBar.opacityProperty(), 0.0)
								),
						new KeyFrame(new Duration(300),
								new KeyValue(menuBar.translateYProperty(), menuBar.getPrefHeight()-26),
								new KeyValue(menuBar.opacityProperty(), 0.9)
								)
						);
				
				slideOut.getKeyFrames().addAll(
						new KeyFrame(new Duration(0),
								new KeyValue(vbox.translateYProperty(), vbox.getPrefHeight()),
								new KeyValue(vbox.opacityProperty(), 0.9)
								),
						new KeyFrame(new Duration(300),
								new KeyValue(vbox.translateYProperty(), vbox.getPrefHeight()),
								new KeyValue(vbox.opacityProperty(), 0.0)
								)
						);
				slideIn.getKeyFrames().addAll(
						new KeyFrame(new Duration(0),
								new KeyValue(vbox.translateYProperty(), vbox.getPrefHeight()),
								new KeyValue(vbox.opacityProperty(), 0.0)
								),
						new KeyFrame(new Duration(300),
								new KeyValue(vbox.translateYProperty(), vbox.getPrefHeight()),
								new KeyValue(vbox.opacityProperty(), 0.9)
								)
						);
			}
		});
		
		mp.currentTimeProperty().addListener(new ChangeListener<Duration>(){

			@Override
			public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration currentDuration) {
				timeSlider.setValue(currentDuration.toSeconds());
				
				long hours = (long) currentDuration.toHours();
				long minutes = (long) currentDuration.toMinutes()%60;
				long seconds = (long) currentDuration.toSeconds()%60;
				
				durationLabel.setText(String.format("%02d", hours) + ":" 
						+ String.format("%02d", minutes) + ":" 
						+ String.format("%02d", seconds));

			}
			
		});
		
		timeSlider.setOnMousePressed(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent mouseEvent) {
				mp.seek(Duration.seconds(timeSlider.getValue()));
			}
			
		});
		
//		Volume Control
		
		volumeSilder.setValue(mp.getVolume() * 100);
		volumeLabel.setText(String.valueOf((int)(volumeSilder.getValue())+"%"));
		volumeSilder.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				double x = volumeSilder.getValue()/100;
				mp.setVolume(x);
				volumeLabel.setText(String.valueOf((int)(x*100)+"%"));
			}
		});

	}
	
//	Animation
	@FXML
	public void mouseEntered() {
		slideIn.play();
	}
	
	@FXML
	public void mouseExited() {
		slideOut.play();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
}
