awk -F ':' '
  /^>/ {
    match($1, />([^ ])+/, transcript)
    start = $4
    end = $5
    match($6, /([^ ])+/, strand)
    if (strand[1] == "1") {
      size = end - start
    } else {
      size = start - end
    }
    match($7, /([^ ]+)/, gene)
    if (NF>10){
    	    if (NF>14){
	    	match($11, /([^ ]+)/, sym)
    		symbol=sym[1]
    		match($12, /([^\[]+)/, desc)
    	}else{
	    	match($10, /([^ ]+)/, sym)
    		symbol=sym[1]
    		match($11, /([^\[]+)/, desc)
    	}
    }else{
    	match($10, /([^\[]+)/, desc)
    	symbol="none"
    }
    split(gene[1], gene_name, ".")
    split(transcript[0], feature, ".")
    
    print gene_name[1]  "\t" substr(feature[1],2) "\t" size "\t" symbol "\t" desc[1]
  }
' cabecalho.txt > gene.dump


countfasta Homo_sapiens.GRCh38.cds.all.fa > update.size
awk '{ split($1, feature_name, "."); feature="'\''" feature_name[1] "'\''"; printf "update gene set size=%s where feature=%s;\n", $2, feature}' update.size > update.size.sql
