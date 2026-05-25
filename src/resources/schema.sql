CREATE TABLE IF NOT EXISTS editura (
    id_editura INT PRIMARY KEY AUTO_INCREMENT,
    nume_editura VARCHAR(255) NOT NULL UNIQUE,
    an_infiintare INT NOT NULL,
    nr_carti_publicate INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS editura_genuri (
    id INT PRIMARY KEY AUTO_INCREMENT,
    editura_id INT NOT NULL,
    gen VARCHAR(100) NOT NULL,
    FOREIGN KEY (editura_id) REFERENCES editura(id_editura) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS editura_carti (
    id INT PRIMARY KEY AUTO_INCREMENT,
    editura_id INT NOT NULL,
    titlu_carte VARCHAR(255) NOT NULL,
    FOREIGN KEY (editura_id) REFERENCES editura(id_editura) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS editura_autori (
    id INT PRIMARY KEY AUTO_INCREMENT,
    editura_id INT NOT NULL,
    autor VARCHAR(255) NOT NULL,
    FOREIGN KEY (editura_id) REFERENCES editura(id_editura) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS autori (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nume VARCHAR(100) NOT NULL,
    prenume VARCHAR(100) NOT NULL,
    varsta INT NOT NULL,
    nr_carti_scrise INT NOT NULL CHECK (nr_carti_scrise > 0),
    carti_publicate TEXT
);

CREATE TABLE IF NOT EXISTS cititori (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nume VARCHAR(100) NOT NULL,
    prenume VARCHAR(100) NOT NULL,
    varsta INT NOT NULL,
    carti_citite INT DEFAULT 0 CHECK (carti_citite >= 0),
    carti_de_citit INT DEFAULT 0 CHECK (carti_de_citit >= 0),
    email VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS carti (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titlu VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    an_publicare INT NOT NULL,
    nr_pagini INT NOT NULL,
    nr_exemplare INT NOT NULL CHECK (nr_exemplare > 0),
    nr_imprumuturi INT DEFAULT 0 CHECK (nr_imprumuturi >= 0),
    editura VARCHAR(255),
    gen_literar VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS angajati (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nume VARCHAR(100) NOT NULL,
    prenume VARCHAR(100) NOT NULL,
    varsta INT NOT NULL,
    functie VARCHAR(100) NOT NULL,
    salariu DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS abonament (
    id_abonament INT PRIMARY KEY AUTO_INCREMENT,
    id_cititor INT NOT NULL UNIQUE,
    tip_abonament VARCHAR(50) NOT NULL,
    data_inceput INT NOT NULL,
    data_expirare INT NOT NULL,
    limita_imprumuturi INT NOT NULL,
    FOREIGN KEY (id_cititor) REFERENCES cititori(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS plati_abonamente (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_abonament INT NOT NULL,
    suma DOUBLE NOT NULL,
    data_plata INT NOT NULL,
    metoda VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_abonament) REFERENCES abonament(id_abonament)
);

CREATE TABLE IF NOT EXISTS imprumuturi (
    id INT PRIMARY KEY AUTO_INCREMENT,
    carte_id INT NOT NULL,
    cititor_id INT NOT NULL,
    data_imprumut DATE NOT NULL,
    data_scadenta DATE NOT NULL,
    data_returnare DATE,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (carte_id) REFERENCES carti(id) ON DELETE RESTRICT,
    FOREIGN KEY (cititor_id) REFERENCES cititori(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recenzii (
    id INT PRIMARY KEY AUTO_INCREMENT,
    carte_id INT NOT NULL,
    cititor_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comentariu LONGTEXT,
    contine_spoiler BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (carte_id) REFERENCES carti(id) ON DELETE CASCADE,
    FOREIGN KEY (cititor_id) REFERENCES cititori(id) ON DELETE CASCADE,
    UNIQUE KEY unique_review (carte_id, cititor_id)
);
