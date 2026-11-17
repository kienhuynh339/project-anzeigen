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

-- Damit Default-Werte verwendet werden, müssen andere Spalten explizit angegeben werden
INSERT INTO users (id, profile_name, account_name, password_hash, created_at)
VALUES (1, 'Samwise', 'samwise', '$2a$10$s8YJDZ5VN/c8A8ULp0g/heEdyuKG9GvlGS8XxGzrHm.KxagWyruiS', '2020-01-01 01:01:00+02:00'),
       (2, 'Merry', 'merry', '$2a$10$TLxIDNTFEs35vLPHVi3BKejJD2IROU6AR9wcpXIZHFFLju2YPXbDe', '2022-02-02 02:02:00+02:00'),
       (3, 'Pippin', 'pippin', '$2a$10$GD7dU0C2qy/morusnemzUeFCQ8N6akGpg2P1GIYG62/Y7uRF0Q2uy', '2024-04-04 04:04:00+02:00'),
       (4, 'Frodo', 'frodo', '$2a$10$ANKEnq3WwF51zjGiTy8PteZY8ODxb31zaCECVQ3skYEr2jTEf0gAe', '2025-05-05 05:05:00+02:00');
ALTER TABLE users ALTER COLUMN id RESTART WITH 5;

INSERT INTO item_categories(id, name)
VALUES (1, 'Alle Kategorien'),
       (2, 'Kategorie 1'),
       (3, 'Kategorie 2'),
       (4, 'Kategorie 3');
ALTER TABLE item_categories ALTER COLUMN id RESTART WITH 5;

-- status: 1(DRAFT)/2(ACTIVE)/3(RESERVED)/4(DELETED)
INSERT INTO items(id, poster_id, title, category_id, description, price, location_id,postcode, status, created_at)
VALUES (1, 1, 'Anzeige 1', 2, 'Beschreibung...', 100, 26488,'42103', 3,
        '2026-09-01 08:00:00+02'),
       (2, 1, 'Anzeige 2', 2, 'Beschreibung...', 101, 26488,'42103', 2,
        '2026-09-01 08:15:00+02'),
       (3, 1, 'Anzeige 3', 2, 'Beschreibung...', 102, 26488,'42109', 2,
        '2026-09-02 08:30:00+02'),
       (4, 2, 'Anzeige 4', 3, 'Beschreibung...', 200, 15879,'40212', 2,
        '2026-08-01 12:00:00+02'),
       (5, 3, 'Anzeige 5', 3, 'Beschreibung...', 300, 19511,'50733', 2,
        '2026-07-01 08:00:00+02'),
       (6, 3, 'Anzeige 6', 4, 'Beschreibung...', 301, 19511,'50733', 2,
        '2026-07-02 08:15:00+02');
ALTER TABLE items ALTER COLUMN id RESTART WITH 19;

INSERT INTO item_images (content_type,image, item_id, display_order)
VALUES
    ('image/jpeg',FILE_READ('src/test/resources/images/user_1_pc_videospiele_nintendoswitch2.jpg'), 1, 0),
    ('image/jpeg',FILE_READ('src/test/resources/images/user1_audio_headset (1).jpg'), 2, 0),
    ('image/jpeg',FILE_READ('src/test/resources/images/user1_audio_headset (2).jpg'), 3, 0),
    ('image/jpeg',FILE_READ('src/test/resources/images/user2_Uhren_3_Uhren (1).jpg'), 4, 0),
    ('image/jpeg',FILE_READ('src/test/resources/images/user2_Uhren_3_Uhren (2).jpg'), 4, 1),
    ('image/jpeg',FILE_READ('src/test/resources/images/user2_Uhren_3_Uhren (3).jpg'), 4, 2),
    ('image/jpeg',FILE_READ('src/test/resources/images/user3_fahrzeuge_fahrrad (2).jpg'), 5, 0),
    ('image/jpeg',FILE_READ('src/test/resources/images/user3_fahrzeuge_fahrrad (3).jpg'), 6, 0);


INSERT INTO item_favorites(user_id, item_id)
VALUES (4,1),
       (4,2),
       (4,4),
       (2,1),
       (2,2),
       (2,3),
       (2,5),
       (3,1),
       (3,2);

INSERT INTO chats(id, item_id, poster_id, interested_user_id, first_deleted_by_user_id)
VALUES (1,1,1,2,2),
       (2,1,1,3,null),
       (3,2,1,4,null),
       (4,4,2,1,null);
ALTER TABLE chats ALTER COLUMN id RESTART WITH 8;

INSERT INTO messages(id, chat_id, sender_id, content, is_read, created_at)
VALUES
-- Chat 1: Nintendo Switch 2
(1, 1, 2, 'Message 1', TRUE, '2026-10-09 18:00:00+02'),
(2, 1, 1, 'Message 2', TRUE, '2026-10-09 18:02:00+02'),

-- Chat 2: Nintendo Switch 2
(3, 2, 3, 'Message 3', TRUE, '2026-11-15 20:00:00+01'),
(4, 2, 1, 'Message 4', TRUE, '2026-11-15 20:02:00+01'),
(5, 2, 3, 'Message 5', TRUE, '2026-11-15 20:04:00+01'),
(6, 2, 1, 'Message 6', TRUE, '2026-11-15 20:06:00+01'),

-- Chat 3: Gaming Headset
(7, 3, 4, 'Message 7', TRUE, '2026-11-15 12:15:00+01'),
(8, 3, 1, 'Message 8', TRUE, '2026-11-15 12:17:00+01'),
(9, 3, 4, 'Message 9', TRUE, '2026-11-15 12:19:00+01'),
(10, 3, 1, 'Message 10', TRUE, '2026-11-15 12:21:00+01'),
(11, 3, 4, 'Message 11', FALSE, '2026-11-15 12:23:00+01'),
(12, 3, 4, 'Message 12', FALSE, '2026-11-15 12:27:00+01'),

-- Chat 4: Rolex, Fossil und Omega Uhren
(13, 4, 1, 'Message 13', TRUE, '2026-11-15 14:00:00+01'),
(14, 4, 2, 'Message 14', TRUE, '2026-11-15 14:02:00+01'),
(15, 4, 1, 'Message 15', TRUE, '2026-11-15 14:04:00+01');
-- IDENTITY-Zähler auf 82 setzen, ansonsten startet er nach dem Seeding mit 1
ALTER TABLE messages
    ALTER COLUMN id RESTART WITH 16;


