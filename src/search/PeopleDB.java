package search;

import java.util.*;

public class PeopleDB {
    private final List<String> people;
    private Map<String, List<Integer>> invertedIndexes;

    public PeopleDB(List<String> people) {
        this.people = people;
        setInvertedIndexes();
    }

    public List<String> getAll() {
        return new ArrayList<>(people);
    }

    public List<String> findAllByQueryInvertedIndexes (String query) {
        List<Integer> indexes = invertedIndexes.get(query.toLowerCase());
        List<String> result = new ArrayList<>();
        if (indexes == null) return result;
        for (Integer index : indexes) {
            result.add(people.get(index));
        }
        return result;
    }

    private void setInvertedIndexes() {
        invertedIndexes = new HashMap<>();
        for (int i = 0; i < people.size(); i++) {
            for (String word : people.get(i).split("\\s+")) {
                word = word.toLowerCase();
                List<Integer> mapValue;
                if (invertedIndexes.containsKey(word)) {
                    mapValue = invertedIndexes.get(word);
                } else {
                    mapValue = new ArrayList<>();
                }
                mapValue.add(i);
                invertedIndexes.put(word, mapValue);
            }
        }
    }
    public List<String> findAllByAny (List<String> query) {
        List<String> fullResult = new ArrayList<>();
        for (String q : query) {
            List<String> result = findAllByQueryInvertedIndexes(q);
            if (Collections.disjoint(fullResult, result)) {
                fullResult.addAll(result);
            } else {
                for (String r : result) {
                    if (!fullResult.contains(r)) {
                        fullResult.add(r);
                    }
                }
            }
        }
        return fullResult;
    }
    public List<String> findAllByAll (List<String> query) {
        PeopleDB arg = this;
        for (String q : query) {
            arg = new PeopleDB(arg.findAllByQueryInvertedIndexes(q));
        }
        return new ArrayList<>(arg.people);
    }
    public List<String> findAllByNone (List<String> query) {
        List<Integer> fullIndexes = new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (String q : query) {
            List<Integer> ls = invertedIndexes.get(q.toLowerCase());
            if (ls != null) fullIndexes.addAll(ls);
        }
        for (int i = 0; i < people.size(); i++) {
            if (!fullIndexes.contains(i)) {
                result.add(people.get(i));
            }
        }
        return result;
    }
}
