package de.esri.tourexplorer.tools;

import java.util.Locale;
import java.util.Optional;

import com.esri.arcgisruntime.mapping.view.GeoView;

import de.esri.tourexplorer.core.App;
import de.esri.tourexplorer.ui.dialogs.LanguageDialog;

public class LanguageCommand extends Command{
	
	public LanguageCommand(GeoView geoView) {
		super(geoView);
	}

	@Override
	public void execute() {
		LanguageDialog languageDialog = new LanguageDialog();
		Optional<Locale> result = languageDialog.showAndWait();
		if(result.isPresent()){
			Locale locale = result.get();
			Locale.setDefault(locale);			
			App.getInstance().notifyLocaleChanged();
		}
	}
}
