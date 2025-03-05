CREATE TABLE annotation (
    annotation_id SERIAL PRIMARY KEY,
    uploaded_variation VARCHAR(270),
    location VARCHAR(40),
    allele VARCHAR(150),
    gene VARCHAR(20),
    feature VARCHAR(20),
    feature_type VARCHAR(20),
    cDNA_position VARCHAR(20),
    CDS_position VARCHAR(20),
    protein_position VARCHAR(20),
    amino_acids VARCHAR(100),
    codon VARCHAR(250),
    src INTEGER
);

CREATE TABLE extra (
    extra_id SERIAL PRIMARY KEY,
    annotation_id INTEGER,
    key VARCHAR(20),
    value VARCHAR(40),
    FOREIGN KEY (annotation_id) REFERENCES annotation(annotation_id) ON DELETE CASCADE
);

CREATE TABLE consequence (
    consequence_id SERIAL PRIMARY KEY,
    annotation_id INTEGER,
    type VARCHAR(50),
    FOREIGN KEY (annotation_id) REFERENCES annotation(annotation_id) ON DELETE CASCADE
);

CREATE INDEX idx_annotation_src ON annotation (src);

CREATE TABLE gene (
    gene VARCHAR(20),
    feature VARCHAR(20),    
    size INTEGER,
    symbol VARCHAR(50),
    description VARCHAR(200),
    PRIMARY KEY (gene, feature)
);

