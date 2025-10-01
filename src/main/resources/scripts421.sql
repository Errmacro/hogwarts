ALTER TABLE студенты
ADD CONSTRAINT age_minimum CHECK (age >= 16);

ALTER TABLE студенты
ALTER COLUMN age SET DEFAULT 20;

ALTER TABLE студенты
ADD CONSTRAINT unique_name UNIQUE (name);

ALTER TABLE факультеты
ADD CONSTRAINT unique_name_color UNIQUE (name, color);