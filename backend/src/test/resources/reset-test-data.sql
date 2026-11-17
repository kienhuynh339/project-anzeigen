-- Prüfung von Fremdschlüsselbedingungen deaktivieren
SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE messages;
TRUNCATE TABLE chats;
TRUNCATE TABLE item_favorites;
TRUNCATE TABLE item_images;
TRUNCATE TABLE items;
TRUNCATE TABLE item_categories;
TRUNCATE TABLE users;

-- Prüfung von Fremdschlüsselbedingungen aktivieren
SET REFERENTIAL_INTEGRITY TRUE;

-- Testdaten erneut einfügen
RUNSCRIPT FROM 'src/test/resources/test-data.sql';
