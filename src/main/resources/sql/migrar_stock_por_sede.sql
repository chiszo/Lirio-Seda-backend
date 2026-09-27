ALTER TABLE tblentrada
    ADD COLUMN idsede INT NULL;

ALTER TABLE tblentrada
    ADD CONSTRAINT fk_entrada_sede
        FOREIGN KEY (idsede) REFERENCES tblsede (idsede);

ALTER TABLE tblsalida
    ADD COLUMN idsede INT NULL;

ALTER TABLE tblsalida
    ADD CONSTRAINT fk_salida_sede
        FOREIGN KEY (idsede) REFERENCES tblsede (idsede);

-- Assign idsede for existing movements before making these columns NOT NULL.