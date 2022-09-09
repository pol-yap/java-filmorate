-- удалаяем структуру
DROP TABLE IF EXISTS Friendship CASCADE;
DROP TABLE IF EXISTS Likes CASCADE;
DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Film CASCADE;
DROP TABLE IF EXISTS Genre CASCADE;
DROP TABLE IF EXISTS MPAA_rating CASCADE;

-- создаём заново
CREATE TABLE IF NOT EXISTS MPAA_ratings(
  id SERIAL PRIMARY KEY,
  name varchar,

  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS Genres(
  id SERIAL PRIMARY KEY,
  name varchar,

  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS Films(
  id SERIAL PRIMARY KEY,
  name varchar NOT NULL,
  description varchar,
  release_date date,
  duration int,
  rating_id int,

  FOREIGN KEY (rating_id) REFERENCES MPAA_ratings(id)
);

CREATE TABLE IF NOT EXISTS Users(
  id SERIAL PRIMARY KEY,
  email varchar,
  login varchar NOT NULL,
  name varchar,
  birthday date
);

CREATE TABLE IF NOT EXISTS Friendship(
  friending_id int,
  friended_id int,

  PRIMARY KEY (friending_id, friended_id),
  FOREIGN KEY (friending_id) REFERENCES Users(id),
  FOREIGN KEY (friended_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Likes(
  film_id int,
  user_id int,

  PRIMARY KEY (film_id, user_id),
  FOREIGN KEY (film_id) REFERENCES Films(id),
  FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Film_genres(
  film_id int,
  genre_id int,

  PRIMARY KEY (film_id, genre_id),
  FOREIGN KEY (film_id) REFERENCES Films(id),
  FOREIGN KEY (genre_id) REFERENCES Genres(id)
);


