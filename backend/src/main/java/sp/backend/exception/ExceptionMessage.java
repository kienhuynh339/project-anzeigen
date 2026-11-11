package sp.backend.exception;

public class ExceptionMessage {
    public static final String USER_NOT_FOUND = "Der Nutzer wurde nicht gefunden.";
    public static final String USER_NOT_AUTHENTICATED = "Der Nutzer ist nicht angemeldet.";
    public static final String ACCESS_DENIED = "Zugriff verweigert.";
    public static final String ACCOUNT_NAME_ALREADY_EXISTS = "Der Benutzername existiert bereits.";
    public static final String ITEM_NOT_FOUND = "Die Anzeige wurde nicht gefunden.";
    public static final String FAVORITE_ITEM_NOT_FOUND = "Die Anzeige existiert nicht in der Merkliste.";
    public static final String FAVORITE_ITEM_ALREADY_EXISTS = "Die Anzeige existiert bereits in der Merkliste.";
    public static final String POSTER_CANNOT_FAVOR_OWN_ITEM = "Der Poster darf seine eigene Anzeige nicht zur Merkliste hinzufügen..";
    public static final String CANNOT_FAVOR_DELETED_ITEM = "Eine gelöschte Anzeige kann nicht zur Merkliste hinzugefügt werden.";
    public static final String CATEGORY_NOT_FOUND = "Die Kategorie wurde nicht gefunden.";
    public static final String CHAT_NOT_FOUND = "Der Chat wurde nicht gefunden.";
    public static final String CANNOT_START_NEW_CHAT_FOR_DELETED_ITEM = "Für eine gelöschte Anzeige kann kein neuer Chat gestartet werden.";
    public static final String MESSAGE_NOT_FOUND = "Die Nachricht wurde nicht gefunden.";
    public static final String LOCATION_NOT_FOUND = "Der Ort wurde nicht gefunden.";
    public static final String IMAGE_NOT_FOUND = "Das Bild wurde nicht gefunden.";
    public static final String ERROR_SAVING_IMAGE = "Fehler beim Speichern des Bildes.";
    public static final String DELETED_ITEM_EDITING_NOT_ALLOWED = "Eine gelöschte Anzeige darf nicht geändert werden.";

    // Benutzereingabe-Validation
    public static final String ITEM_ID_NOT_NULL = "Die Anzeige-ID ist erforderlich.";
    public static final String ITEM_ID_POSITIVE = "Die Anzeige-ID muss größer als 0 sein.";
    public static final String ITEM_TITEL_NOT_BLANK = "Der Titel ist erforderlich.";
    public static final String ITEM_TITEL_SIZE = "Der Titel muss zwischen 5 und 100 Zeichen lang sein.";
    public static final String USER_ID_NOT_NULL = "Die User-ID ist erforderlich.";
    public static final String USER_ID_POSITIVE = "Die User-ID muss größer als 0 sein.";
    public static final String CATEGORY_ID_NOT_NULL = "Die Kategorie-ID ist erforderlich.";
    public static final String CATEGORY_ID_POSITIVE = "Die Kategorie-ID muss größer als 0 sein.";
    public static final String DESCRIPTION_SIZE = "Die Beschreibung darf maximal 4000 Zeichen lang sein.";
    public static final String LOCATION_ID_NOT_NULL = "Die Standort-ID ist erforderlich.";
    public static final String LOCATION_ID_POSITIVE = "Die Standort-ID muss größer als 0 sein.";
    public static final String POSTCODE_NOT_BLANK = "Die Postleitzahl ist erforderlich.";
    public static final String POSTCODE_PATTERN = "Die Postleitzahl muss genau 5 Ziffern enthalten.";
    public static final String LOCATION_EXTRA_INFO_SIZE = "Die zusätzlichen Ortsinformationen dürfen maximal 50 Zeichen lang sein.";
    public static final String PRICE_NOT_NULL = "Der Preis ist erforderlich.";
    public static final String PRICE_POSITIVE_OR_ZERO = "Der Preis muss mindestens 0 betragen.";
    public static final String STATUS_NOT_NULL = "Der Status ist erforderlich.";
    public static final String STATUS_NOT_VALID = "Der Status ist ungültig.";
    public static final String IMAGES_AMOUNT = "Es dürfen maximal 10 Bilder hochgeladen werden.";
    public static final String IMAGES_TOTAL_SIZE = "Die Gesamtgröße der Bilder darf maximal 50 MB betragen.";
    public static final String PROFILE_NAME_NOT_BLANK = "Der Profilname ist erforderlich.";
    public static final String PROFILE_NAME_SIZE = "Der Profilname muss zwischen 1 und 30 Zeichen lang sein.";
    public static final String ACCOUNT_NAME_NOT_BLANK = "Der Benutzername ist erforderlich.";
    public static final String ACCOUNT_NAME_SIZE = "Der Benutzername muss zwischen 5 und 30 Zeichen lang sein.";
    public static final String PASSWORD_NOT_BLANK = "Das Passwort ist erforderlich.";
    public static final String PASSWORD_SIZE = "Das Passwort muss zwischen 5 und 30 Zeichen lang sein.";
    public static final String MESSAGE_NOT_EMPTY = "Die Nachricht darf nicht leer sein.";
    public static final String MESSAGE_SIZE = "Die Nachricht muss zwischen 1 und 4000 Zeichen lang sein.";
}
