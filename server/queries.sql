--Tipos de consequencias severas
select distinct(con.type) from annotation ann natural inner join (consequence con  natural inner join extra ext) where ext.key = 'impact' and ext.value ='HIGH';
/*
                type                 
-------------------------------------
 3_prime_UTR_variant
 5_prime_UTR_variant
 coding_sequence_variant
 frameshift_variant
 intron_variant
 NMD_transcript_variant
 non_coding_transcript_exon_variant
 non_coding_transcript_variant
 splice_acceptor_variant
 splice_donor_5th_base_variant
 splice_donor_region_variant
 splice_donor_variant
 splice_polypyrimidine_tract_variant
 splice_region_variant
 start_lost
 start_retained_variant
 stop_gained
 stop_lost
(18 rows) */

-- Genes que sofreram variações moderadas apenas no participante 1:
(select ann.gene from annotation ann natural inner join gene gen where ann.src=1 group by ann.gene) except (select ann.gene from annotation ann natural inner join gene gen where ann.src<>1 group by ann.gene);

-- Agora features
(select ann.feature from annotation ann natural inner join gene gen where ann.src=1 group by ann.feature) except (select ann.feature from annotation ann natural inner join gene gen where ann.src<>1 group by ann.feature);

-- Contagem do percentual de variant call frente ao tamanho do gene 
select ann.feature, gen.symbol, ann.src, count(1), gen.size, round(100.0*count(1)/gen.size,4) as vcl_percent from annotation ann natural inner join gene gen where ann.src=1 group by ann.gene, ann.feature, ann.src, gen.size, gen.symbol order by ann.src, ann.feature;

-- Contagem do percentual de variant call frente ao tamanho do gene, mas apenas para os genes que sofreram vcl apenas no genoma 1.
select ann.feature, gen.symbol, count(1) total, gen.size, round(100.0*count(1)/gen.size,4) as vcl_percent from annotation ann natural inner join gene gen  where ann.src=1 and ann.feature in ((select ann.feature from annotation ann natural inner join gene gen where ann.src=1 group by ann.feature) except (select ann.feature from annotation ann natural inner join gene gen where  ann.src<>1 group by ann.feature)) group by ann.feature, gen.size, gen.symbol order by vcl_percent desc, gen.symbol;
-- Retorna 854 transcritos

-- Confirmando que os genes listados acima não possuem variant calls nos outros três genomas
-- Teste apenas para os dez transcritos com maior quantidade de vcls
select ann.feature, gen.symbol, count(1) total, gen.size, round(100.0*count(1)/gen.size,4) as vcl_percent from annotation ann natural inner join gene gen  where ann.src<>1 and ann.feature in ('ENST00000546527','ENST00000545299','ENST00000554710','ENST00000320982','ENST00000452264', 'ENST00000467753','ENST00000580904','ENST00000511964','ENST00000442721', 'ENST00000592625') group by ann.feature, gen.size, gen.symbol order by vcl_percent desc, gen.symbol;
--Retorna 0
 
-- Contagem do percentual de variant call frente ao tamanho do gene, mas apenas para os genes que sofreram vcl apenas no genoma 1 e que cujos genes são homozigotos
select ann.feature, gen.symbol, count(1) total, gen.size, round(100.0*count(1)/gen.size,4) as vcl_percent from annotation ann natural inner join (consequence con  natural inner join extra ext) natural inner join gene gen where ext.key='zyg' and ext.value ='1:HOM' and ann.src=1 and ann.feature in ((select ann.feature from annotation ann natural inner join gene gen where ann.src=1 group by ann.feature) except (select ann.feature from annotation ann natural inner join gene gen where  ann.src<>1 group by ann.feature)) group by ann.feature, gen.size, gen.symbol order by vcl_percent desc, gen.symbol; 
 

