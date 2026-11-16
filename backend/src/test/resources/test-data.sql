-- Damit Default-Werte verwendet werden, müssen andere Spalten explizit angegeben werden
INSERT INTO users (id, profile_name, account_name, password_hash, created_at)
VALUES (1, 'Samwise', 'samwise', '$2a$10$s8YJDZ5VN/c8A8ULp0g/heEdyuKG9GvlGS8XxGzrHm.KxagWyruiS', '2020-01-01 01:01:00+02:00'),
       (2, 'Merry', 'merry', '$2a$10$TLxIDNTFEs35vLPHVi3BKejJD2IROU6AR9wcpXIZHFFLju2YPXbDe', '2022-02-02 02:02:00+02:00'),
       (3, 'Pippin', 'pippin', '$2a$10$GD7dU0C2qy/morusnemzUeFCQ8N6akGpg2P1GIYG62/Y7uRF0Q2uy', '2024-04-04 04:04:00+02:00'),
       (4, 'Frodo', 'frodo', '$2a$10$ANKEnq3WwF51zjGiTy8PteZY8ODxb31zaCECVQ3skYEr2jTEf0gAe', '2025-05-05 05:05:00+02:00');
ALTER TABLE users ALTER COLUMN id RESTART WITH 5;

INSERT INTO item_categories(id, name)
VALUES (1, 'Alle Kategorien'),
       (2, 'Antiquitäten & Kunst'),
       (3, 'Fahrzeuge'),
       (4, 'Fahrzeuge: Teile'),
       (5, 'Baby'),
       (6, 'Bastel- & Künstlerbedarf'),
       (7, 'Beauty & Gesundheit'),
       (8, 'Briefmarken'),
       (9, 'Bücher'),
       (10, 'Büro & Schreibwaren'),
       (11, 'Business & Industrie'),
       (12, 'Computer, Tablets & Netzwerk'),
       (13, 'Feinschmecker'),
       (14, 'Filme & DVDs'),
       (15, 'Foto & Camcorder'),
       (16, 'Garten & Terrasse'),
       (17, 'Handys & Kommunikation'),
       (18, 'Haushaltsgeräte'),
       (19, 'Haustierbedarf'),
       (20, 'Heimwerker'),
       (21, 'Immobilien'),
       (22, 'Kleidung & Accessoires'),
       (23, 'Modellbau'),
       (24, 'Möbel & Wohnen'),
       (25, 'Münzen'),
       (26, 'Musik'),
       (27, 'Musikinstrumente'),
       (28, 'PC- & Videospiele'),
       (29, 'Reisen'),
       (30, 'Sammeln & Seltenes'),
       (31, 'Spielzeug'),
       (32, 'Sport'),
       (33, 'Tickets'),
       (34, 'TV, Video & Audio'),
       (35, 'Uhren & Schmuck'),
       (36, 'Verschiedenes');
ALTER TABLE item_categories ALTER COLUMN id RESTART WITH 37;

-- status: 1(DRAFT)/2(ACTIVE)/3(RESERVED)/4(DELETED)
INSERT INTO items(id, poster_id, title, category_id, description, price, location_id,postcode, status, created_at)
VALUES (1, 1, 'Nintendo switch 2', 28, 'Beschreibung...', 100, 26488,'42103', 3,
        '2026-09-01 08:00:00+02'),
       (2, 1, 'Gaming Headset', 34, 'Beschreibung...', 101, 26488,'42103', 2,
        '2026-09-01 08:15:00+02'),
       (3, 1, 'sony headset weiß', 34, 'Beschreibung...', 102, 26488,'42109', 2,
        '2026-09-02 08:30:00+02'),
       (16, 2, '3 Uhren rolex, fossil, omega', 35, 'Beschreibung...', 200, 15879,'40212', 2,
        '2026-08-01 12:00:00+02'),
       (17, 3, 'mobike Fahrrad', 3, 'Beschreibung...', 300, 19511,'50733', 2, '2026-07-01 08:00:00+02'),
       (18, 3, 'HERCULES Fahrrad', 3, 'Beschreibung...', 301, 19511,'50733', 2, '2026-07-02 08:15:00+02');
ALTER TABLE items ALTER COLUMN id RESTART WITH 19;

INSERT INTO item_images (content_type,image, item_id, display_order)
VALUES
    ('image/jpeg',FILE_READ('src/main/resources/images/user_1_pc_videospiele_nintendoswitch2.jpg'), 1, 0),
    ('image/jpeg',FILE_READ('src/main/resources/images/user1_audio_headset (1).jpg'), 2, 0),
    ('image/jpeg',FILE_READ('src/main/resources/images/user1_audio_headset (2).jpg'), 3, 0),
    ('image/jpeg',FILE_READ('src/main/resources/images/user2_Uhren_3_Uhren (1).jpg'), 16, 0),
    ('image/jpeg',FILE_READ('src/main/resources/images/user2_Uhren_3_Uhren (2).jpg'), 16, 1),
    ('image/jpeg',FILE_READ('src/main/resources/images/user2_Uhren_3_Uhren (3).jpg'), 16, 2),
    ('image/jpeg',FILE_READ('src/main/resources/images/user3_fahrzeuge_fahrrad (2).jpg'), 17, 0),
    ('image/jpeg',FILE_READ('src/main/resources/images/user3_fahrzeuge_fahrrad (3).jpg'), 18, 0);


INSERT INTO item_favorites(user_id, item_id)
VALUES (4,1),
       (4,2),
       (4,16),
       (2,1),
       (2,2),
       (2,3),
       (2,17),
       (3,1),
       (3,2);

INSERT INTO chats(id, item_id, poster_id, interested_user_id, first_deleted_by_user_id)
VALUES (1,1,1,2,2),
       (2,1,1,3,null),
       (3,2,1,4,null),
       (4,16,2,1,null);
ALTER TABLE chats ALTER COLUMN id RESTART WITH 8;

INSERT INTO messages(id, chat_id, sender_id, content, is_read, created_at)
VALUES
-- Chat 1: Nintendo Switch 2
(1, 1, 2, 'Hallo, ist die Nintendo Switch 2 noch verfügbar?', TRUE, '2026-10-09 18:00:00+02'),
(2, 1, 1, 'Hallo! Ja, die Nintendo Switch 2 ist noch verfügbar.', TRUE, '2026-10-09 18:02:00+02'),

-- Chat 2: Nintendo Switch 2
(3, 2, 3, 'Hallo, ich interessiere mich für deine Nintendo Switch 2. Ist sie noch verfügbar?', TRUE, '2026-11-15 20:00:00+01'),
(4, 2, 1, 'Hallo! Ja, die Nintendo Switch 2 ist noch verfügbar.', TRUE, '2026-11-15 20:02:00+01'),
(5, 2, 3, 'Wann hast du die Konsole denn gekauft?', TRUE, '2026-11-15 20:04:00+01'),
(6, 2, 1, 'Ich habe sie vor ungefähr vier Monaten gekauft.', TRUE, '2026-11-15 20:06:00+01'),

-- Chat 3: Gaming Headset
(7, 3, 4, 'Hallo, ich interessiere mich für dein Gaming-Headset. Ist es noch verfügbar?', TRUE, '2026-11-15 12:15:00+01'),
(8, 3, 1, 'Hallo! Ja, das Headset ist noch verfügbar.', TRUE, '2026-11-15 12:17:00+01'),
(9, 3, 4, 'Wann hast du es denn gekauft?', TRUE, '2026-11-15 12:19:00+01'),
(10, 3, 1, 'Ich habe es vor ungefähr acht Monaten gekauft.', TRUE, '2026-11-15 12:21:00+01'),
(11, 3, 4, 'Wie lange hält der Akku bei voller Ladung?', FALSE, '2026-11-15 12:23:00+01'),
(12, 3, 4, 'Ist das komplette Zubehör noch vorhanden?', FALSE, '2026-11-15 12:27:00+01'),

-- Chat 4: Rolex, Fossil und Omega Uhren
(13, 4, 1, 'Hallo, ich interessiere mich für deine drei Uhren. Sind alle noch verfügbar?', TRUE, '2026-11-15 14:00:00+01'),
(14, 4, 2, 'Hallo! Ja, alle drei Uhren sind noch verfügbar.', TRUE, '2026-11-15 14:02:00+01'),
(15, 4, 1, 'Kannst du mir die einzelnen Preise für die Rolex, Omega und Fossil nennen?', TRUE, '2026-11-15 14:04:00+01');

-- IDENTITY-Zähler auf 82 setzen, ansonsten startet er nach dem Seeding mit 1
ALTER TABLE messages
    ALTER COLUMN id RESTART WITH 16;


