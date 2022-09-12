-- удалаяем структуру
DROP TABLE IF EXISTS friendship CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS mpaa CASCADE;

-- создаём заново
CREATE TABLE IF NOT EXISTS mpaa(
  id SERIAL PRIMARY KEY,
  name varchar,

  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS genres(
  id SERIAL PRIMARY KEY,
  name varchar,

  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS films(
  id SERIAL PRIMARY KEY,
  name varchar(200) NOT NULL,
  description varchar,
  release_date date,
  duration int,
  rating_id int,

  FOREIGN KEY (rating_id) REFERENCES mpaa(id)
);

CREATE TABLE IF NOT EXISTS users(
  id SERIAL PRIMARY KEY,
  email varchar,
  login varchar NOT NULL,
  name varchar,
  birthday date
);

CREATE TABLE IF NOT EXISTS friendship(
  friending_id int,
  friended_id int,

  PRIMARY KEY (friending_id, friended_id),
  FOREIGN KEY (friending_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (friended_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes(
  film_id int,
  user_id int,

  PRIMARY KEY (film_id, user_id),
  FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_genres(
  film_id int,
  genre_id int,

  PRIMARY KEY (film_id, genre_id),
  FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
  FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);


