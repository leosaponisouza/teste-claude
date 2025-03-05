select count(distinct gene) from gene;

--Monstrando com discriminação de todos os participantes
(select ann.gene from annotation ann natural inner join 
	(consequence con  natural inner join extra ext) natural inner join 
	gene gen where ext.value ='MODERATE' and ann.src=1 group by ann.gene) 
	except (
		(select ann.gene from annotation ann natural inner join 
		(consequence con  natural inner join extra ext) natural inner join 
		gene gen where  ann.src=3 group by ann.gene) 
		union 
		(select ann.gene from annotation ann natural inner join 
		(consequence con  natural inner join extra ext) natural inner join 
		gene gen where ext.value ='MODERATE' and ann.src=2 group by ann.gene) 
		union 
		(select ann.gene from annotation ann natural inner join 
		(consequence con  natural inner join extra ext) natural inner join 
		gene gen where ext.value ='MODERATE' and ann.src=0 group by ann.gene)
	);

--Mostrando mais simples
(select ann.gene from annotation ann natural inner join 
	(consequence con  natural inner join extra ext) natural inner join 
	gene gen where ext.value ='MODERATE' and ann.src=1 group by ann.gene) 
	except (
		(select ann.gene from annotation ann natural inner join 
		(consequence con  natural inner join extra ext) natural inner join 
		gene gen where ext.value ='MODERATE' and ann.src<>1 group by ann.gene) 
	);

--Contando com restrição
select count(1) from ( 
	(select ann.gene from annotation ann natural inner join 
		(consequence con  natural inner join extra ext) natural inner join 
		gene gen where ext.value ='MODERATE' and ann.src=1 group by ann.gene) 
		except (
			(select ann.gene from annotation ann natural inner join 
			(consequence con  natural inner join extra ext) natural inner join 
			gene gen where ext.value ='MODERATE' and ann.src<>1 group by ann.gene) 
		)
	) as report;

--Contando com restrição
select count(1) from ( (select ann.gene from annotation ann natural inner join (consequence con natural inner join extra ext) natural inner join gene gen where ext.value ='MODERATE' and ann.src=1 group by ann.gene) except ( (select ann.gene from annotation ann natural inner join (consequence con natural inner join extra ext) natural inner join gene gen where ext.value ='MODERATE' and ann.src<>1 group by ann.gene) ) ) as report;

--Contando sem restrição
select count(1) from ( (select ann.gene from annotation ann natural inner join (consequence con natural inner join extra ext) natural inner join gene gen where  ann.src=1 group by ann.gene) except ( (select ann.gene from annotation ann natural inner join (consequence con natural inner join extra ext) natural inner join gene gen where  ann.src<>1 group by ann.gene) ) ) as report;

--Mostrando sem restrição
\timing
select distinct ann.gene, gen.symbol, gen.description from annotation ann natural inner join gene gen where ann.gene in ( (select ann.gene from annotation ann natural inner join (consequence con natural inner join extra ext) natural inner join gene gen where  ann.src=1 group by ann.gene) except (select ann.gene from annotation ann natural inner join (consequence con natural inner join extra ext) natural inner join gene gen where  ann.src<>1 group by ann.gene) ) order by gen.symbol;

SELECT ann.gene, gen.symbol, gen.description FROM annotation ann NATURAL INNER JOIN gene gen WHERE EXISTS ( SELECT 1 FROM consequence con NATURAL INNER JOIN extra ext WHERE con.annotation_id = ann.annotation_id AND ann.src = 1 ) AND NOT EXISTS ( SELECT 1 FROM consequence con NATURAL INNER JOIN extra ext WHERE con.annotation_id = ann.annotation_id AND ann.src <> 1 );

