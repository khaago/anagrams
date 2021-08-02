# Anagram Finder

### Assumptions

- Anagrams are case-insensitive. If there are two anagrams, e.g. ale -> Lea, lea with the same letters but different
  cases, only the lowercase is returned.
- Only the English alphabet is supported.
- `9,223,372,036,854,775,807` is the largest 64-bit integer. Any word that produces a hash larger than this number is
  not supported.

### Algorithm

    The fundamental theorem of arithmetic states that every positive integer (except the number 1) can be represented
    in exactly one way apart from rearrangement as a product of one or more primes (Hardy and Wright 1979, pp. 2-3).

This property of prime numbers can be used to hash any word of a reasonable length into a long integer. Each character
can be represented as a prime number, and the word as a whole can be represented as the product of these primes. Due to
the uniqueness of the composition of the product, any anagram of the word will also have the same product. For example,
consider the mapping `'a' -> 2, 'b' -> 3, 'c' -> 5`. We can use this hash the word `abc` which is represented
by `2 x 3 x 5` which is `30`. Now the word `bac` can be represented by `3 x 2 x 5`, which is also `30`!

This program creates a hashmap with these prime hashes as the keys, and the sets of words that produce the same hash as
the values to these keys. For the above example, the entry in the dictionary hashmap would look like

```
30 -> ['abc','bac','cab']
```

This set is created during startup and every time the user looks up anagrams for any word, this hashmap is queried for
the hash of the input word.

This algorithm can be further optimized to strategically map the smaller primes to the most frequently occurring letters
in the dictionary. This will help keep the hashes smaller.

### Building and Running

JDK 1.7+ is required. The two solutions are organized into folders called `quick` and `better` respectively. The way to
run them is the same. For quick solution:

```
cd quick
javac QuickSolution.java && java QuickSolution /path/to/dictionary.file
```

For better solution

```
cd better
javac *.java && java AnagramDictionaryClient /path/to/dictionary.file
# for testing:
java -ea AnagramDictionaryServiceTest dictionary.txt
```

To exit, enter `exit` at the prompt and press return.
