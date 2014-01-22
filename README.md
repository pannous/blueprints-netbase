Welcome to [**Netbase**](https://github.com/pannous/netbase/) 
 a high-performance semantic graph database, optimized for natural language queries on static freebase data.

[**blueprints**](https://github.com/tinkerpop/blueprints) is a graph database standard allowing for toolchaining, making databases available to all different tools.

Libraries and binaries are included for Mac and Linux.

You can feed it with any CSV XML or N3 data.
Optimized for the quick import of Wordnet, DBPEDIA and Freebase (and 
[private data](https://github.com/pannous/lang) )

Thanks to shared memory and binary dumps you can load and process billions of nodes+statements(edges) and hundreds of gigabytes in no time.

Requirements: 
* Lots of RAM favorable
* For java-binding / blueprints : no requirements
* For server : data favorable
* For command line access: 'readline' lib :

	sudo port install readline
	sudo yum install readline
	sudo apt-get install readline

Playing with it:

`./bin/netbase`

	USING SHARED MEMORY
	Context#0 name:ROOT CONTEXT nodes:430338000, statements:355550000
	Warnings: 0
	Node limit: 1048576000
	Statements: 1048576000
	
	Netbase C++ Version z.a
	netbase> ?
	available commands
	help :h or ?
	load :l [force]	fetch the graph from disk or mem
	import :i [<file>]
	export :e (all to csv)
	delete :d <node|statement|id|name>
	save :s or :w
	server
	quit :q
	clear :cl
	limit <n>
	Type words, ids, or queries:
	all animals with feathers
	select * from dogs where terrier
	all city with countrycode ZW
	Population of Gehren
	opposite of bad
	netbase> import

Live example queries:
* [Episodes of South Park – Season 4](http://netbase.pannous.com/html/South%20Park%20-%20Season%204.Episodes)
* [Places opened in 1974](http://netbase.pannous.com/html/Opened:1974%20Year)
* [Chemical structures](http://netbase.pannous.com/xml/verbose/42636157)
* All african birds with orange beaks.

Have fun! See [here for more and hosted samples](http://www.pannous.info/products/netbase/).