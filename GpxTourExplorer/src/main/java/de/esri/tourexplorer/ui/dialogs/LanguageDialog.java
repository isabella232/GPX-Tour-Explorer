package de.esri.tourexplorer.ui.dialogs;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.util.Callback;

public class LanguageDialog extends Dialog<Locale>{
	private ResourceBundle res = ResourceBundle.getBundle("res.locale.LanguageDialog");
	private Map<String, Integer> languages = new HashMap<>();

	public LanguageDialog(){
		setTitle(res.getString("title"));
		
		VBox vbox = new VBox();
		vbox.setSpacing(10.0);
		vbox.setPadding(new Insets(15,20,15,20));
		Label languageLabel = new Label(res.getString("languageChoice"));	
		Language germanLanguage = new Language(res.getString("german"));
		Image germanImage = new Image("/res/images/FlagGermany.png");
		germanLanguage.setImage(germanImage);
		germanLanguage.setLocale(new Locale("de"));
		languages.put("de", 0);
		Language englishLanguage = new Language(res.getString("english"));
		Image englishImage = new Image("/res/images/FlagGreatBritain.png");
		englishLanguage.setImage(englishImage);
		englishLanguage.setLocale(new Locale("en"));
		languages.put("en", 1);
		
		ComboBox<Language> languageChoice = new ComboBox<>();
		languageChoice.getItems().addAll(germanLanguage, englishLanguage);
		String lang = Locale.getDefault().getLanguage();
		languageChoice.getSelectionModel().select(languages.get(lang));
		languageChoice.setCellFactory(new Callback<ListView<Language>, ListCell<Language>>() {
			@Override
			public ListCell<Language> call(ListView<Language> param) {
				return new ListCell<Language>(){
					@Override
					protected void updateItem(Language language, boolean empty) {
						super.updateItem(language, empty);
                        if (language == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(language.getName());
                            setGraphic(new ImageView(language.getImage()));
                        }
					}
				};
			}
		});

		vbox.getChildren().addAll(languageLabel, languageChoice);
		getDialogPane().setContent(vbox);
		
		ButtonType okButtonType = new ButtonType(res.getString("ok"), ButtonData.OK_DONE);
		ButtonType closeButtonType = new ButtonType(res.getString("cancel"), ButtonData.CANCEL_CLOSE);
		getDialogPane().getButtonTypes().addAll(okButtonType, closeButtonType);
		
		setResultConverter(new Callback<ButtonType, Locale>() {
			@Override
			public Locale call(ButtonType buttonType) {
				Locale locale = null;
				if(buttonType == okButtonType){
					Language language = languageChoice.getSelectionModel().getSelectedItem();
					locale = language.getLocale();
				}
				return locale;
			}
		});
	}
	
	private class Language{
		private String name;
		private Image image;
		private Locale locale;
		
		public Language(){
		}
		
		public Language(String name){
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public Image getImage() {
			return image;
		}
		
		public void setImage(Image image) {
			this.image = image;
		}
		
		public Locale getLocale() {
			return locale;
		}

		public void setLocale(Locale locale) {
			this.locale = locale;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
