DROP TABLE IF EXISTS GPS;

CREATE TABLE GPS (
  id 			INT AUTO_INCREMENT  PRIMARY KEY,
  name 			VARCHAR(300) DEFAULT NULL,
  description 	TEXT DEFAULT NULL,
  author 		VARCHAR(300) DEFAULT NULL,
  time 			TIMESTAMP DEFAULT NULL,
  file_name     VARCHAR(300) NOT NULL,
  file_size		INT NOT NULL,
  created_at 	TIMESTAMP NOT NULL,
  updated_at 	TIMESTAMP NOT NULL
);

INSERT INTO GPS (name, description, author,created_at,updated_at,file_name,file_size) VALUES
  ('name3', 'description3','author3', '2020-01-11 13:47:23.28', '2020-01-11 13:47:23.28','xxx3.gpx',123),
  ('name1', 'description1','author1', '2020-01-13 13:47:23.28', '2020-01-13 13:47:23.28','xxx1.gpx',456),
  ('name2', 'description2','author2', '2020-01-12 13:47:23.28', '2020-01-12 13:47:23.28','xxx2.gpx',789);

