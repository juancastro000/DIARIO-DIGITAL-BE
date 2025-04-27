INSERT INTO users (id_user, user_name, password, user_email) VALUES
  (1, 'korejack',
   '$2a$10$5HjwN77TH1pHv.Lrw1wzVOY7NxlMHQ9tpNyrL9Gg26u86MzKkqBCq',
   'jucastror2002@gmail.com');

-- Insertar tags (ahora usan ENUM y se guardan como String)
INSERT INTO tags (id, name) VALUES
  (1, 'PERSONAL'),
  (2, 'TRABAJO'),
  (3, 'SALUD'),
  (4, 'OCIO'),
  (5, 'FINANZAS'),
  (6, 'APRENDIZAJE'),
  (7, 'FAMILIA'),
  (8, 'DEPORTE');

INSERT INTO entries (id_entry, content, date, mood, productivity, user_id) VALUES
  (1,  'Entrada 1',  '2025-04-01 08:00:00', 'Feliz',      'Alta', 1),
  (2,  'Entrada 2',  '2025-04-02 09:00:00', 'Triste',     'Media', 1),
  (3,  'Entrada 3',  '2025-04-03 10:00:00', 'Neutral',    'Baja',  1),
  (4,  'Entrada 4',  '2025-04-04 11:00:00', 'Emocionado', 'Alta',  1),
  (5,  'Entrada 5',  '2025-04-05 12:00:00', 'Ansioso',    'Media', 1),
  (6,  'Entrada 6',  '2025-04-06 13:00:00', 'Feliz',      'Baja',  1),
  (7,  'Entrada 7',  '2025-04-07 14:00:00', 'Triste',     'Alta',  1),
  (8,  'Entrada 8',  '2025-04-08 15:00:00', 'Neutral',    'Media', 1),
  (9,  'Entrada 9',  '2025-04-09 16:00:00', 'Emocionado', 'Baja',  1),
  (10, 'Entrada 10', '2025-04-10 17:00:00', 'Ansioso',    'Alta',  1),
  (11, 'Entrada 11', '2025-04-11 08:30:00', 'Feliz',      'Media', 1),
  (12, 'Entrada 12', '2025-04-12 09:30:00', 'Triste',     'Baja',  1),
  (13, 'Entrada 13', '2025-04-13 10:30:00', 'Neutral',    'Alta',  1),
  (14, 'Entrada 14', '2025-04-14 11:30:00', 'Emocionado', 'Media', 1),
  (15, 'Entrada 15', '2025-04-15 12:30:00', 'Ansioso',    'Baja',  1),
  (16, 'Entrada 16', '2025-04-16 13:30:00', 'Feliz',      'Alta',  1),
  (17, 'Entrada 17', '2025-04-17 14:30:00', 'Triste',     'Media', 1),
  (18, 'Entrada 18', '2025-04-18 15:30:00', 'Neutral',    'Baja',  1),
  (19, 'Entrada 19', '2025-04-19 16:30:00', 'Emocionado', 'Alta',  1),
  (20, 'Entrada 20', '2025-04-20 17:30:00', 'Ansioso',    'Media', 1),
  (21, 'Entrada 21', '2025-04-21 09:00:00', 'Feliz',      'Baja',  1),
  (22, 'Entrada 22', '2025-04-22 10:00:00', 'Triste',     'Alta',  1),
  (23, 'Entrada 23', '2025-04-23 11:00:00', 'Neutral',    'Media', 1),
  (24, 'Entrada 24', '2025-04-24 12:00:00', 'Emocionado', 'Baja',  1),
  (25, 'Entrada 25', '2025-04-25 13:00:00', 'Ansioso',    'Alta',  1),
  (26, 'Entrada 26', '2025-04-26 14:00:00', 'Feliz',      'Media', 1),
  (27, 'Entrada 27', '2025-04-27 15:00:00', 'Triste',     'Baja',  1),
  (28, 'Entrada 28', '2025-04-28 16:00:00', 'Neutral',    'Alta',  1),
  (29, 'Entrada 29', '2025-04-29 17:00:00', 'Emocionado', 'Media', 1),
  (30, 'Entrada 30', '2025-04-30 18:00:00', 'Ansioso',    'Baja',  1),
  (31, 'Entrada 31', '2025-05-01 08:00:00', 'Feliz',      'Alta',  1),
  (32, 'Entrada 32', '2025-05-02 09:00:00', 'Triste',     'Media', 1),
  (33, 'Entrada 33', '2025-05-03 10:00:00', 'Neutral',    'Baja',  1),
  (34, 'Entrada 34', '2025-05-04 11:00:00', 'Emocionado', 'Alta',  1),
  (35, 'Entrada 35', '2025-05-05 12:00:00', 'Ansioso',    'Media', 1),
  (36, 'Entrada 36', '2025-05-06 13:00:00', 'Feliz',      'Baja',  1),
  (37, 'Entrada 37', '2025-05-07 14:00:00', 'Triste',     'Alta',  1),
  (38, 'Entrada 38', '2025-05-08 15:00:00', 'Neutral',    'Media', 1),
  (39, 'Entrada 39', '2025-05-09 16:00:00', 'Emocionado', 'Baja',  1),
  (40, 'Entrada 40', '2025-05-10 17:00:00', 'Ansioso',    'Alta',  1);

-- Insertar relaciones entry_tags para 1-40
INSERT INTO entry_tags (entry_id, tag_id) VALUES
  (1,1),(1,2),(2,2),(2,3),(3,3),(3,4),(4,4),(4,5),(5,5),(5,6),(6,6),(6,7),(7,7),(7,8),(8,8),(8,1),(9,1),(9,2),(10,2),(10,3),(11,3),(11,4),(12,4),(12,5),(13,5),(13,6),(14,6),(14,7),(15,7),(15,8),(16,8),(16,1),(17,1),(17,2),(18,2),(18,3),(19,3),(19,4),(20,4),(20,5),
  (21,5),(21,6),(22,6),(22,7),(23,7),(23,8),(24,8),(24,1),(25,1),(25,2),(26,2),(26,3),(27,3),(27,4),(28,4),(28,5),(29,5),(29,6),(30,6),(30,7),(31,7),(31,8),(32,8),(32,1),(33,1),(33,2),(34,2),(34,3),(35,3),(35,4),(36,4),(36,5),(37,5),(37,6),(38,6),(38,7),(39,7),(39,8),(40,8),(40,1);

-- Reiniciar secuencias
ALTER TABLE entries   ALTER COLUMN id_entry RESTART WITH 41;
ALTER TABLE tags      ALTER COLUMN id        RESTART WITH 9;
ALTER TABLE users     ALTER COLUMN id_user   RESTART WITH 2;