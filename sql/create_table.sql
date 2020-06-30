CREATE TABLE contents (
  id SERIAL PRIMARY KEY
, text VARCHAR(140)
, created_on date
, created_at time
);

# テストデータの挿入
INSERT INTO contents (text, created_on, created_at) VALUES
  ('hello',current_date,current_time)
, ('hi',current_date,current_time)
, ('good night',current_date,current_time)
;

SELECT * FROM contents;