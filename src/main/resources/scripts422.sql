CREATE TABLE Автомобили (
    id SERIAL PRIMARY KEY,
    марка VARCHAR(50) NOT NULL,
    модель VARCHAR(50) NOT NULL,
    цена DECIMAL(10,2) NOT NULL CHECK (цена > 0)
);

CREATE TABLE Люди (
    id SERIAL PRIMARY KEY,
    имя VARCHAR(100) NOT NULL,
    возраст INTEGER NOT NULL CHECK (возраст >= 0),
    есть_права BOOLEAN NOT NULL DEFAULT FALSE,
    машина_id INTEGER REFERENCES Автомобили(id)
);