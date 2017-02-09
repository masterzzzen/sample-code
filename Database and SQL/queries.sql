/* 
 * CSCI E-66: Problem Set 1, SQL Programming Problems
 *
 * Put your name and email address below:
 *     name:
 *     email:
 */

/**********************************************************
 * REMEMBER: You should include only the SQL commands,
 * *NOT* the results that they produce.
 **********************************************************/

/*
 * Problem 5. Put your SQL command for this problem below.	
 */

 Find names of movies, oscar is Cate, type 
 
 SELECT M.name, O.type, O.year  
 FROM Movie M, Person P, Oscar O, Actor A  
 WHERE A.actor_id = P.id and M.id = A.movie_id and O.movie_id = M.id
	and P.name = 'Cate Blanchett';
	


/*
 * Problem 6. Put your SQL command for this problem below.	
 */

 SELECT name, pob, dob
 FROM Person P 
 WHERE name = 'Matt Damon' OR name = 'Leonardo DiCaprio'


/*
 * Problem 7. Put your SQL command for this problem below.	
 */

 
 SELECT DISTINCT P.name
 FROM Director D, Movie M, Oscar O, Person P  
 WHERE P.id = D.director_id AND D.director_id = O.person_id AND M.id = O.movie_id AND O.type = 'BEST-DIRECTOR' 
		AND P.pob NOT LIKE '%USA%' AND M.id IN 		
		(
			SELECT DISTINCT M.id
			FROM ACTOR A, Movie M, Oscar O, Person P  
			WHERE P.id = A.actor_id AND A.actor_id = O.person_id AND M.id = O.movie_id AND (O.type = 'BEST-ACTOR' OR O.type = 'BEST-ACTRESS') AND P.pob NOT LIKE '%USA%'
		)

 
/*
 * Problem 8. Put your SQL command for this problem below.	
 */
SELECT O.year, M.name 
FROM Movie M, Oscar O
WHERE O.movie_id = M.id AND O.type = 'BEST-PICTURE' AND M.id IN 
				(
				SELECT M.id 
				FROM Movie M, Director D, Oscar O 
				WHERE M.id = D.movie_id AND O.movie_id = D.movie_id AND O.type = 'BEST-DIRECTOR'
				)

/*
 * Problem 9. Put your SQL command for this problem below.	
 */

 SELECT count(*) 
 FROM Movie 
 WHERE runtime > 200

 /*
 * Problem 10. Put your SQL command for this problem below.	
 */
 
 SELECT name, dob  
 FROM Person 
 WHERE name LIKE 'Julia %' 



/*
 * Problem 11. Put your SQL command for this problem below.	
 */

 SELECT name 
 FROM Movie 
 WHERE runtime	< (SELECT min(M.runtime) 
		FROM Movie M, Oscar O 
		WHERE M.id = O.movie_id AND O.type = 'BEST-PICTURE')
 
 
/*
 * Problem 12. Put your SQL command for this problem below.	
 */

 SELECT P.name
 FROM Movie M, Director D, Person P
 WHERE M.id = D.movie_id AND D.director_id = P.id AND M.earnings_rank IS NOT NULL
 GROUP BY P.name 
 HAVING count(P.name) > 2



/*
 * Problem 13. Put your SQL command for this problem below.	
 */

SELECT M.earnings_rank, M.name, O.type 
FROM Movie M LEFT OUTER JOIN Oscar O on M.id = O.movie_id
WHERE M.earnings_rank < 26
ORDER BY M.earnings_rank;

/*
 * Problem 14. Put your SQL command for this problem below.	
 */

 SELECT count(P.name)
 FROM Person P 
 WHERE P.name NOT IN (SELECT P.name
 FROM Person P, Movie M
 WHERE P.id = M.id AND M.earnings_rank IS NOT NULL)


/*
 * Problem 15. Put your SQL command for this problem below.	
 */
	
 SELECT M.name, "actor" function
 FROM Movie M, ACTOR A, Person P  
 WHERE M.id = A.movie_id AND A.actor_id = P.id AND P.name = 'Clint Eastwood'
 UNION
 SELECT M.name, "director" function
 FROM Movie M, DIRECTOR D, Person P  
 WHERE M.id = D.movie_id AND D.director_id = P.id AND P.name = 'Clint Eastwood'
 ORDER BY M.name, function 
 
/*
 * Problem 16. Put your SQL command for this problem below.	
 */
 SELECT M.name, O.year, rating, O.type
 FROM Movie M, Oscar O
 WHERE M.id = O.movie_id AND M.rating = 'G' AND O.type = 'BEST-PICTURE' AND 
				O.year = (SELECT max(O.year)
				FROM Movie M, Oscar O
				WHERE M.id = O.movie_id AND M.rating = 'G' AND O.type = 'BEST-PICTURE');



/*
 * Problem 17. Put your SQL command for this problem below.	
 */

 UPDATE Person
 SET pob = 'Los Angeles, California, USA', dob = '1982-7-24'
 WHERE Person.name = 'Elisabeth Moss';

/*
 * Problem 18 (required for grad-credit students; optional for others). 
 * Put your SQL command for this problem below.	
 */
SELECT avg(count), type 
FROM

(SELECT count(DISTINCT M.name) AS count, 'BEST-ACTOR' AS type, P.name  
FROM Person P, Movie M, ACTOR A 
WHERE P.id = A.actor_id AND M.id = A.movie_id 
		AND P.name IN 
		(
		SELECT P.name 
		FROM Person P, Oscar O 
		WHERE P.id = O.person_id AND O.type = 'BEST-ACTOR'
		)
GROUP BY P.name

UNION 

SELECT count(DISTINCT M.name) AS count, 'BEST-ACTRESS' AS type, P.name  
FROM Person P, Movie M, ACTOR A 
WHERE P.id = A.actor_id AND M.id = A.movie_id 
		AND P.name IN 
		(
		SELECT P.name 
		FROM Person P, Oscar O 
		WHERE P.id = O.person_id AND O.type = 'BEST-ACTRESS'
		)
GROUP BY P.name

UNION 

SELECT count(DISTINCT M.name) AS count, 'BEST-SUPPORTING-ACTOR' AS type, P.name  
FROM Person P, Movie M, ACTOR A 
WHERE P.id = A.actor_id AND M.id = A.movie_id 
		AND P.name IN 
		(
		SELECT P.name 
		FROM Person P, Oscar O 
		WHERE P.id = O.person_id AND O.type = 'BEST-ACTRESS'
		)
GROUP BY P.name

UNION 

SELECT count(DISTINCT M.name) AS count, 'BEST-SUPPORTING-ACTRESS' AS type, P.name  
FROM Person P, Movie M, ACTOR A 
WHERE P.id = A.actor_id AND M.id = A.movie_id 
		AND P.name IN 
		(
		SELECT P.name 
		FROM Person P, Oscar O 
		WHERE P.id = O.person_id AND O.type = 'BEST-SUPPORTING-ACTRESS'
		)
GROUP BY P.name)

GROUP BY type

/*
 * Problem 19 (required for grad-credit students; optional for others). 
 * Put your SQL command(s) for this problem below.	
 */
 
 CREATE TEMPORARY TABLE bacon_1 AS 
	SELECT P.name 
	FROM Person P, ACTOR A, Movie M 
	WHERE P.id = A.actor_id AND M.id = A.movie_id
		AND M.name IN 
		(
			SELECT M.name 
			FROM Person P, ACTOR A, Movie M 
			WHERE P.id = A.actor_id AND M.id = A.movie_id AND P.name = 'Kevin Bacon'			
		);
 	
	SELECT P.name 
	FROM Person P, ACTOR A, Movie M 
	WHERE P.id = A.actor_id AND M.id = A.movie_id
		AND M.name IN 
		(
			SELECT M.name 
			FROM Person P, ACTOR A, Movie M 
			WHERE P.id = A.actor_id AND M.id = A.movie_id AND P.name IN bacon_1		
		)

 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 