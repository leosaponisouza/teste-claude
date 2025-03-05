#!/usr/bin/python3
import psycopg2
import sys
import os
from tqdm import tqdm

def to_string_or_none(value):
    if value == "-":
        return None
    elif "-" in value:
        start, end = value.split("-")
        return f"{start}-{end}"
    elif "=" in value:
        start, end = value.split("=")
        return f"{end}"
    else:
        return value
    
def process_file(filename, src):
    # Verificar se o arquivo existe
    if not os.path.exists(filename):
        print(f"Erro: O arquivo '{filename}' não existe.")
        return

    # Conectar ao banco de dados
    try:
        conn = psycopg2.connect(
            host="localhost",
            database="VEPANN",
            user="exomaadm",
            password="po8AhghaiThi"
        )
    except psycopg2.Error as e:
        print(f"Erro ao conectar ao banco de dados: {e}")
        return

    cur = conn.cursor()

    try:
        with open(filename, "r") as arquivo:
            linhas = arquivo.readlines()
            for linha in tqdm(linhas, desc="Processando linhas", miniters=1000):
                linha = linha.strip()
                # Ignora linhas que começam com '#'
                if linha.startswith('#'):
                    continue
                campos = linha.split(None, 12)
                
                if len(campos) != 13:
                    print(f"Aviso: linha com número incorreto de campos: {linha}")
                    continue

                uploaded_variation, location, allele, gene, feature, feature_type, consequence, cDNA_position, CDS_position, protein_position, amino_acids, codon, extra = campos

                cur.execute("""
                    INSERT INTO annotation (
                        uploaded_variation, location, allele, gene, feature, feature_type,
                        cDNA_position, CDS_position, protein_position, amino_acids, codon, src
                    ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) RETURNING annotation_id
                """, (
                    uploaded_variation, location, allele, gene, feature, feature_type,
                    to_string_or_none(cDNA_position), to_string_or_none(CDS_position), 
                    to_string_or_none(protein_position), amino_acids, codon, src
                ))
                annotation_id = cur.fetchone()[0]

                # Inserir informações na tabela consequence (um por linha)
                for c in consequence.split(','):
                    cur.execute("""
                        INSERT INTO consequence (annotation_id, type)
                        VALUES (%s, %s)
                    """, (annotation_id, c.strip()))

                # Processar e inserir informações na tabela extra (um por linha)
                for item in extra.split(';'):
                    if '=' in item:
                        key, value = item.split('=')
                        key = key.replace('-', '').strip().lower()
                        if key == 'impact':
                            cur.execute("""INSERT INTO extra (annotation_id, key, value) VALUES (%s, %s, %s) """,
                                        (annotation_id, key, value))
                        elif key in ['distance', 'flags', 'minimised', 'strand', 'zyg']:
                            cur.execute("""INSERT INTO extra (annotation_id, key, value) VALUES (%s, %s, %s)""",
                                        (annotation_id, key, value))
                        else:
                            cur.execute("""INSERT INTO extra (annotation_id, key, value) VALUES (%s, %s, %s)""",
                                        (annotation_id, 'other', f"{key}={value}"))
                    else:
                        if item.strip() != '-':
                            cur.execute("""INSERT INTO extra (annotation_id, key, value) VALUES (%s, %s, %s)""",
                                        (annotation_id, 'other', item))

        conn.commit()
        print(f"Processamento do arquivo '{filename}' concluído com sucesso.")

    except psycopg2.Error as e:
        conn.rollback()
        print(f"Erro ao inserir dados no banco: {e}")
    except IOError as e:
        print(f"Erro ao ler o arquivo: {e}")
    finally:
        cur.close()
        conn.close()

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Uso: python script.py <nome_do_arquivo> <src>")
    else:
        filename = sys.argv[1]
        try:
            src = int(sys.argv[2])
            process_file(filename, src)
        except ValueError:
            print("Erro: O valor de 'src' deve ser um número inteiro.")
