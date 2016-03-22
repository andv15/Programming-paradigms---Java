Sonea Andreea 323 CB Tema1 PP


	Prentru retinerea pattern-urilor am folosti o lista de seturi. Pentru a retine un 
set am creat o clasa PatternSet ce retine un set dintr-un pattern, avand drept campuri 3 
bindinguri: src, dst, port. Clasa suprascrie metoda equals care intoarce false daca cel 
putin unul din bindinguri nu este egal cu cel primit ca parametru. 
	Metoda isMorePermisive  verifica daca setul 'this' e mai permisiv decat x. Intoarce 
true daca valorile celor 3 headere au aceeasi valoare cu cele din setul primit ca parametru sau
daca contin valoarea 'Any'.
	Metoda intersect realizeaza intersectia fiecarei valori din cele 3 header-uri ale lui
'this' si a setului primit ca parametru. Daca intersectia este egala cu unul dintre seturi,
intoarce acel set, altfel intoarce null.

	Pattern
	Metoda nullPattern intoarce un pattern ce contine [Src:Null, Dst:Null, Port:Null]
	Metoda intersect returneaza intersectia a doua pattern-uri. Se intersecteaza feiecare
set din cele doua pattern-uri si atunci cand intersectia nu intoarce null se adauga in
pattern-ul ce v fi returnat.
	Metoda reunion realizeaza reuniunea a doua pattern-uri. Sunt adaugate seturile din
pattern-ul 'this', se pargurg seturile din pattern-ul primit ca parametru si sunt adaugate
acelea care nu exista deja.
	Metoda Subset verifica daca 'this' e subset al lui q(primit). Este verificat daca 
pentru fiecare set din 'this' exista un set 'mai permisiv' in q, adica unul egal sau mai 
general.
	Metoda Rewrite suprascrie in header-ul h al setului 'this' valoarea v.
	Metoda deepClone creaza un nou pattern identic cu this. Sunt parcurse toate 
binding-urile din fiecare set si sunt create binding-uri noi, ce sunt adaugate intr-o lista
pentru crearea noului pattern.

	Reachability
	La nivelul clasei Reachability sunt retinute device-urile impreuna cu pattern-urile
ce le-au vizitat, in reach, pattern-ul curent si pattern-ul urmator, lista de deviceuri.
	Este adaugat in lista de device-uri ce va fi parcursa primul device care 'accepta'
pattern-ul curent. Pattern-ul curent la acest pas este cel initial.
	Cat timp un pattern nu se repeta sau cat timp pattern-ul curent nu este gol, sunt
parcurse deviceu-rile din lista. Device-urile dau accept la vizitatorul 'this'. Este apelata
metoda accept din Device care apeleaza metoda vizit din Richability.
	Metoda vizit din Richability adauga in 'reach' device-ul vizitat impreuna cu 
pattern-ul ce il viziteaza. Este apelata metoda apply din Device care intoarce un nou
pattern dupa aplicarea regulilor device-ului asupra pattern-ului curent.
	Noul pattern este retinut prin reuniunea tuturor pattern-urilor care ies din fiecare
device de la pasul curent.
	Este returnata lista cu succesorii device-ului primit care nu au mai fost vizitati sau
care nu au mai fost vizitati de pattern-ul curent.
	Metoda accept din Device stabileste pentru 'this' noii succesori returnati de vizit.

	Este actualizata lista de device-uri ce vor da 'accept' la pasul urmator. Aceasta va
fi formata din succesorii tuturor device-urilor de la pasul curent.
	Este actualizat si pattern-ul curent ca fiind noul pattern(reuniunea pattern-urilor
ce au 'iesit' din device-urile de la pasul curent).

	La iesirea din bucla while sunt parcurse toate device-urile cu lista aferenta de
pattern-uri care le-a vizitat. Sunt retinute acele pattern-uri care intersectate cu
pattern-ul destinatie sunt diferite de null.
