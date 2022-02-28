package find;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

class FindTest {
	
	/**
     * Finds the given number 'needle' in the given array 'haystack' and returns its index, or -1 
	 * if the number is not in the array.
	 * 
	 * If 'null' is passed as 'haystack', returns -1.
	 * 
	 * @param haystack The array in which this method will look for 'needle'
	 * @param needle The number for which this method will look in 'haystack'
	 * @return The index at which 'needle' was found, or -1 if it was not found
	 * 
	 * opm: pre = voorwaarden die moeten gelden voor de uitvoer van de methode, post = voorwaarden die moeten gelden na uitvoer van de methode
	 * 
	 * // @pre | haystack != null
	 * 
	 * opm: informeel in engelse tekst, formele postconditie na '|'
	 * @post If 'needle' does not appear in 'haystack', the result equals -1.
	 * 		(for all index i, haystack[i] != needle) ==> result == -1
	 * 		!(for all index i, haystack[i] != needle) || result == -1
	 * 		!IntStream.range(0,haystack.length).allMatch(i -> haystack[i] != needle) || result == -1
	 * 		|haystack != null && IntStream.range(0,haystack.length).anyMatch(i -> haystack[i] == needle) || result == -1
	 * 		(opm aan hierboven: ofwel zit needle in de haystack ofwel is het resultaat -1
	 * 		(opm: anyMatch = manier om een existentiele kwantificatie te schrijven in java = er bestaat
	 * 			  allMatch = manier om een universele kwantificatie te schrijven in java = voor alle)
	 * 		(er bestaat een int tussen 0 inclusief en haystack.length exclusief zodat haystack[i] == needle) of result == -1
	 * @post If the result is not -1, the result is a valid index for 'haystack' and the element in 'haystack' at index 'result' equals 'needle'.
	 * 		|result == -1||haystack != null && 0<=result && result<haystack.length && haystack[result] == needle
	 */
	
	/** netjes nu:
	 *@post 
	 * 		|haystack != null && IntStream.range(0,haystack.length).anyMatch(i -> haystack[i] == needle) || result == -1
	 * 
	 *@post
	 * 		|Arrays.stream(haystack).anyMatch(e -> e == needle) // er bestaat een element e in haystack zodat e == needle
	 * 															// dit is hetzelfde als: IntStream.range(0,haystack.length).anyMatch(i -> haystack[i] == needle)
	 * 															// maar korter geschreven
	 * 		| || result == -1
	 * 
	 *@post 
	 * 		|result == -1||haystack != null && 0<=result && result<haystack.length && haystack[result] == needle
	 */
	
	/** (Dit is wat er bij de prof stond)
	 * @pre |haystack != null
	 * 
	 * IntStream.range(a,b).anyMatch(i->P(i))
	 * Er bestaat een i tss a (inclusief) en b(exclusief) zodat p(i) waar is
	 * 
	 * Arrays.stream(haystack).anyMatch(e->e == needle)
	 * Er bestaat een element e in haystack zodat e == needle
	 * 
	 * Arrays.stream(myArray).anyMatch(e->P(e))
	 * Er bestaat een e in myArray zodat P(e) waar is
	 * 
	 * @post als 'neelde'niet in 'haystack' voorkomt, is het resultaat -1
	 * 		|IntStream.range(0,haystack.length).anyMatch(i->haystack[i] == needle)
	 * 		| || result == -1
	 * 
	 * @post
	 * 		|Arrays.stream(haystack).anyMatch(e -> e == needle)
	 * 		| || result == -1
	 * 
	 * @post
	 *		| result == -1 || 
	 *		| 0<= result && result < haystack.length && haystack[result] == needle
	 */	
    
	//deze specificatie van 'find', specificeert die precies het gedrag van de methode? (dus is het ondubbelzinnig of is er nog interpretatie mogelijk?)
	
	
	int find(int[] haystack, int needle) {
		if(haystack == null) return -1;
		int index = 0;
		for (;;) {
			if (index == haystack.length)
				return -1;
			if (haystack[index] == needle)
				return index;
			index++;
		}
	}
	
	@Test
	void testFindMultiple() {
		int[] myArray = {10,20,30,20,40};
		int result = find(myArray,20);
//		assertEquals(1, find(myArray,20));
		assertTrue(result == 1 || result == 3); //index kan 1 OF 3 zijn, alleen 1 is fout (procudurale abstracties)
	}
	
	
	/**
     * Finds the given number 'needle' in the given array 'haystack' and returns its index, or -1 if the given number is not in the given array.
	 * The given array must be sorted in ascending order.
	 * 
//	 * @pre | haystack != null 
//	 * 
//	 * Nu uitdrukken dat haystack in oplopende volgorde staat:
//	 * @pre The given array's elements are in ascending order
//	 *		|IntStream.range(0, haystack.length-1).allMatch(n-> haystack[n] <= haystack[n+1])
//	 * 
	 * 
	 * we hebben de precondities verwijderd en die vervangen door defensieve checks
	 * we moeten nu beloven aan de klant dat we dat doen:
	 * 
	 * @throws IllegalArgumentException if the given array is null
	 * 		| haystack == null
	 * @throws IllegalArgumentException if the given array's elements are not in ascending order
	 * 		| !IntStream.range(0, haystack.length-1).allMatch(n-> haystack[n] <= haystack[n+1])
	 * 
	 * @post
	 * 		| Arrays.stream(haystack).anyMatch(e -> e == needle)
	 * 		| || result == -1
	 * @post
	 * 		| result == -1 ||
	 * 		| 0 <= result && result < haystack.length && haystack[result] == needle
	 * 
	 */
	
// opm: beter om defensief te programmeren dan contractueel, we moeten checken
// dat de argumenten correct zijn in de code en er niet vanuit gaan dat 
// de persoon die het programma gebruikt zich aan de precondities gaat houden
	int binarySearch(int[] haystack, int needle){
		//precondities checken
		if (haystack == null)
			throw new IllegalArgumentException("`haystack`' is null");
		if (!IntStream.range(0, haystack.length-1).allMatch(n-> haystack[n] <= haystack[n+1]))
			throw new IllegalArgumentException("`haystack`'s elements are not in ascending order");
		int start = 0;
		int end = haystack.length;
		while (start<end) {
			//int middle = (start+end)/2; //je kunt overflow krijgen als je het zo doet
			// start en end grootte orde 2miljard
			// maximale waarde voor int is 2^31 (iets groter dan 2miljard), dus als je start en and optelt is het overflow
			
			int middle = start + (end-start)/2;
			if (needle < haystack[middle]) {
				end = middle;
				}else if (needle == haystack[middle])
					return middle;
				else
					start = middle+1;			
		}
		return -1;	
	}
	
	@Test
	void testBinarySearch() {
		int[] myArray = {10,20,30,40};
		assertEquals(0,binarySearch(myArray,10));
		assertEquals(1,binarySearch(myArray,20));
		assertEquals(2,binarySearch(myArray,30));
		assertEquals(3,binarySearch(myArray,40));
		assertEquals(-1,binarySearch(myArray,50));
		
	}
	
	
	
	
	//bedoeling dat hij zijn argument sorteert, we nemen bubblesort:
	
	/**
	 * Sorts the given array
	 * 
	 * @pre | array != null
	 * 
	 * [verwijzing]:
	 * @post the array's content is a permutation of its old content
	 * 		|Arrays.stream(array).allMatch(e->     
	 * 		|	Arrays.stream(array).filter(e1 -> e1 == e).count() ==
	 * 		|	Arrays.stream(old(array.clone())).filter(e1 -> e1 == e).count()
	 * 		|)
	 * (voor elk element van de array in de nieuwe toestand)
	 * (het aantal keren dat e voorkomt in array, filter op de stream van elementen van array en de resulteren de elementen tellen 
	 * dan heb je het aantal keren dat e voorkomt in array) ==
	 * (aantal keren dat dat element voorkomt in de oude toestand van array)
	 * opm: old(array) is niet juist, het geeft de oude waarde van de parameter array en dat blijft ongewijzigd, het blijft wijzen naar hetzelfde object, maar
	 * 		we kijken pas naar de array na afloop van de methode (in de post-state), dus we krijgen altijd de nieuwe inhoud van de array, als we de oude inhoud willen hebben dan moeten
	 * 		we een clone nemen in de pre-state (in de oude toestand): old(array.clone())
	 *  
	 * 
	 * 
	 * 
	 * @post The array's elements are in ascending order
	 * 		|IntStream.range(0, array.length-1).allMatch(n-> array[n] <= array[n+1])
	 * 	opm: for (int i = 0; i<array.length; i++)
				array[i] = 0;
			volgens bovenstaande postconditie is dit ook een gesorteerde array (array met alleen nullen)
			we hebben dus nog een voorwaarde nodig: 
			de nieuwe inhoud van de array moet een permutatie zijn van de originele inhoud van de array (zie [verwijzing])
	 * 
	 * 
	 * 		
	 * 
	 */
	
	
	
	void sort(int[] array) { 
		for (int n = array.length; n>=2 ; n--) {
			for (int i = 0; i < n-1; i++) {
				if (array[i + 1] < array[i]){
					int tmp = array[i+1];
					array[i+1] = array[i];
					array[i] = tmp;				
				}
			}
		}
	}
//		for (int i = 0; i<array.length; i++)
//			array[i] = 0;}
	
	@Test
	void testSort() {
		int[] myArray = {30, 20, 40, 10, 25};
		sort(myArray);
		assertArrayEquals(new int[] {10, 20, 25, 30, 40}, myArray);	//Let op! andere assert statement 	
	}
	
	
	
	
	
	@Test
	void testFound() {
		int[] myhaystack= {10,20,30};
		int positie = find(myhaystack,20);
		assertEquals(1,positie);
	}
	
	@Test
	void testNotFound() {
		int[] myhaystack= {10,20,30};
		int positie = find(myhaystack,40);
		assertEquals(-1,positie);		
	}
	
	//@Test
	//void testNull() {
	//	int positie = find(null,20);
	//	assertEquals(-1,positie);
	//}
		
}

