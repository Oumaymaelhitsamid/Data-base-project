/* Initialisation du fichier */
/* Les exemples de transaction ci-dessous concernent en particulier les propositions d'enchères
Pour plus de lisibilite sur les conditions on a decoupe les transactions en plusieurs cas particuliers :
    - Est-ce que le produit a déjà été attribué ?
    - Est-ce que la dernière offre faite remporte l'enchère ?
    - Est-ce que le prix proposé est correcte ?
    - etc
*/

/* Le squelette de la transaction pour les propositions d'enchères peut se résumer ainsi :
    - On vérifie si le nombre maximal d'enchère n'est pas dépassé : si oui on continue sinon ROLLBACK
    - On vérifie si l'utilisateur n'est pas le dernier à avoir enchéri : S'il n'est pas le dernier on continue, sinon ROLLBACK
    - On vérifie si le prix propose depasse le prix courant : Si oui on continue, sinon ROLLBACK
    - On vérifie si l'offre est la dernière : Si oui on continue, sinon on insert et on commit;
    - On vérifie si l'offre n'a pas été adjugé entretemps : Si oui on adjuge le produit à l'utilisateur qui a fait l'offre, sinon on ROLLBACK
*/  

BEGIN;
    -- Initialisation
    INSERT INTO PRODUITS VALUES ('532', 'XBOX 360', '250', 'console sortie en 2005, vendu plus de 25 millions de fois en Europe', 'https://www.cdiscount.com/pdt2/8/5/9/1/700x700/0882224035859/rw/console-xbox-360-premium.jpg', 'Console');
    INSERT INTO PRODUITS VALUES ('431', 'Peugeot 208', '10400', 'Peugeot occasion, en bonne état, 50 000km', 'https://lh3.googleusercontent.com/proxy/DX4b2G8nfGvU8GEUPs0OCke5iFACCdpci5Og-wF3gdcOkRHuNAECZJjkHgi63mRXYgpQYAgVxt0QNT_iHXm-yGbD5UAO3A4V33wUFVYxUcN4VNSd4WqSaywt', 'Voiture');


    INSERT INTO OFFRES VALUES('431','05/12/2021','15:34:45.087845637','10900','1');
    INSERT INTO OFFRES VALUES('431','05/12/2021','15:34:45.087845638','11000','1');
    INSERT INTO OFFRES VALUES('431','05/12/2021','15:34:45.087845639','11100','1');
    INSERT INTO OFFRES VALUES('532','05/12/2021','15:34:45.087845700','11100','1');
COMMIT;


/* Squelette d'une transaction pour une offre sur un produit finalement non-adjugé */
BEGIN;
    INSERT INTO OFFRES VALUES('431','05/12/2021','15:34:45.087845640','11300','1');
    SELECT COUNT(*) FROM OFFRES WHERE OFFRES.idProduit = '431'; -- valeur stockée dans la variable nb_offres_courant
    -- Traitement en JAVA : on voit que nb_offres_courant = 4 < nb_max_encheres = 5 *ici* donc on valide temporairement l'offre
    
    SAVEPOINT offreAcceptee;

    INSERT INTO ESTREMPORTEPAR VALUES ('431', '05/12/2021', '15:34:45.087845640');
    -- Mais ici nb_offres_courant = 4 != nb_max_encheres = 5 donc le produit n'est pas adjugé
    ROLLBACK TO offreAcceptee;

COMMIT;

/* Squelette d'une transaction pour une offre sur un produit finalement adjugé */

BEGIN;
    INSERT INTO OFFRES VALUES('431','05/12/2021','15:34:45.087845641','11200','1');
    SELECT COUNT(*) FROM OFFRES WHERE OFFRES.idProduit = '431'; -- valeur stockée dans la variable nb_offres_courant
    -- Traitement en JAVA : on voit que nb_offres_courant=5 = nb_max_encheres = 5 *ici* donc on valide temporairement l'offre
    SAVEPOINT offreAcceptee2;

    INSERT INTO ESTREMPORTEPAR VALUES ('431', '05/12/2021', '15:34:45.087845641');
    -- Ici nb_offres_courant = 5 == nb_max_encheres = 5 donc le produit peut etre adjuge
    
    SAVEPOINT adjugePossible;
    --On verifie si le produit n'a pas deja ete remporte :
    SELECT * FROM ESTREMPORTEPAR WHERE idProduit = '431';
    -- On voit que le produit n'apparait pas dans la table ESTREMPORTEPAR, on peut valider l'attribution du produit à l'offre gagnante
COMMIT;



/* Squelette d'une transaction pour une offre sur un produit déjà adjugé */
/* NB : On suppose qu'un deuxième client a fait une offre quand le produit n'etait pas encore attribue*/

BEGIN;
    INSERT INTO OFFRES VALUES('431','05/12/2021','15:34:45.087845642','11200','1');
    SELECT COUNT(*) FROM OFFRES WHERE OFFRES.idProduit = '431'; -- valeur stockée dans la variable nb_offres_courant
    -- Traitement en JAVA : on voit que nb_offres_courant=5 = nb_max_encheres = 5 *ici* donc on valide temporairement l'offre
    SAVEPOINT offreAcceptee2;

    INSERT INTO ESTREMPORTEPAR VALUES ('431', '05/12/2021', '15:34:45.087845642');
    -- Ici nb_offres_courant = 5 == nb_max_encheres = 5 donc le produit peut etre adjuge
    
    SAVEPOINT adjugePossible;
    -- Ah mais le produit a ete attribue entretemps:
    SELECT * FROM ESTREMPORTEPAR WHERE idProduit = '431';
    -- On voit que le produit apparait deja dans la table ESTREMPORTEPAR, on invalide donc malheureusement l'attribution
ROLLBACK;


/* Squelette d'une transaction pour une offre sur un produit déjà adjugé */
BEGIN;
    INSERT INTO OFFRES VALUES('431','05/12/2021','15:34:45.087845642','11200','1');
    SELECT COUNT(*) FROM OFFRES WHERE OFFRES.idProduit = '431'; -- valeur stockée dans la variable nb_offres_courant
    -- Traitement en JAVA : on voit que nb_offres_courant=6 > nb_max_encheres = 5 *ici* donc on ne valide pas l'offre
ROLLBACK;



/* Utilisateur propose une nouvelle offre mais avait déjà renchéri */
BEGIN;
    -- On verifie que l'utilisateur qui propose la nouvelle offre n'est pas le dernier à avoir enchéri 
    INSERT INTO OFFRES VALUES('532','05/12/2021','15:34:45.087845701','11200','1');
    SELECT idUtilisateur FROM OFFRES WHERE idProduit = '1' AND prixPropose = (SELECT MAX(prixPropose) FROM OFFRES WHERE idProduit = '1');
    --On s'aperçoit en JAVA que l'utilisateur a fait la dernière enchère, on annule sa nouvelle offre
ROLLBACK;


/* L'Utilisateur n'est ici pas le dernier à avoir enchéri */
BEGIN;
    -- On verifie que l'utilisateur qui propose la nouvelle offre n'est pas le dernier à avoir enchéri 
    INSERT INTO OFFRES VALUES('532','05/12/2021','15:34:45.087845701','11200','2');
    SELECT idUtilisateur FROM OFFRES WHERE idProduit = '2' AND prixPropose = (SELECT MAX(prixPropose) FROM OFFRES WHERE idProduit = '2');
    -- En JAVA on voit que l'Utilisateur a le droit d'enchérir car il ne renchérit pas sur lui-même
    SAVEPOINT userOK;
    -- On verifie que l'utilisateur propose une enchère supérieure au prix courant sur le produit
    SELECT prixCourant FROM PRODUITS WHERE idProduit = '532';-- valeur stockée dans la variable prixCourant
    -- Traitement en JAVA : on voit que le prix courant est supérieur *ici* donc on valide l'offre
COMMIT;


/* L'Utilisateur propose un prix inferieur */
BEGIN;
    -- On verifie que l'utilisateur qui propose la nouvelle offre n'est pas le dernier à avoir enchéri 
    INSERT INTO OFFRES VALUES('532','05/12/2021','15:34:45.087845701','11100','3');
    SELECT idUtilisateur FROM OFFRES WHERE idProduit = '3' AND prixPropose = (SELECT MAX(prixPropose) FROM OFFRES WHERE idProduit = '3');
    -- En JAVA on voit que l'Utilisateur a le droit d'enchérir car il ne renchérit pas sur lui-même
    SAVEPOINT userOK;
    -- On verifie que l'utilisateur propose une enchère supérieure au prix courant sur le produit
    SELECT prixCourant FROM PRODUITS WHERE idProduit = '532';-- valeur stockée dans la variable prixCourant
    -- Traitement en JAVA : on voit que le prix courant est inférieur *ici* donc on invalide l'offre
ROLLBACK;