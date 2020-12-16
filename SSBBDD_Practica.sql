-- creando tablas
create table authors (author_id int, author_name string) 
comment 'tabla de autores' 
row format delimited
fields terminated by ';'
lines terminated by '\n'
location '/user/hive/warehouse'
tblproperties ("skip.header.line.count"="1");

-- creando tabla
create table datasets (author_id int, bestsellers_rank int, imprint string, publication_date timestamp, rating_avg float, rating_count int) 
comment 'tabla de datasets' 
row format delimited
fields terminated by ';'
lines terminated by '\n'
location '/user/hive/warehouse'
tblproperties ("skip.header.line.count"="1");


-- desde la línea de comandos de la vm
hadoop fs -put /home/cloudera/authors.csv /user/hive/warehouse
hadoop fs -put /home/cloudera/datasets.csv /user/hive/warehouse

-- en hue mediante impala
load data inpath '/user/hive/warehouse/authors.csv' into table authors;
load data inpath '/user/hive/warehouse/datasets.csv' into table datasets;
