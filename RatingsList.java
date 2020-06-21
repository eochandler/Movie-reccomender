package recommender; /**
 * recommender.RatingsList.
 * A class that stores movie ratings for a user in a custom singly linked list of
 * recommender.RatingNode objects. Has various methods to manipulate the list. Stores
 * only the head of the list (no tail! no size!). The list should be sorted by
 * rating (from highest to smallest).
 * Fill in code in the methods of this class.
 * Do not modify signatures of methods. Not all methods are needed to compute recommendations,
 * but all methods are required for the assignment.
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.Math;
public class RatingsList implements Iterable<RatingNode> {

    private RatingNode head;

    public int[] moviesIDsToArray(RatingsList list){

        int size = 0;
        RatingNode current = list.head;
        while(current != null){
            size = size + 1;
            current = current.next();
        }
        current = list.head;
        int[] intArray = new int[size];
        int ticker = 0;
        while(current != null){
            intArray[ticker] = current.getMovieId();
            current = current.next();
            ticker = ticker + 1;
        }
        return intArray;
    }

    /**
     * Changes the rating for a given movie to newRating. The position of the
     * node within the list should be changed accordingly, so that the list
     * remains sorted by rating (from largest to smallest).
     *
     * @param movieId id of the movie
     * @param newRating new rating of this movie
     */

    public void setRating(int movieId, double newRating) {
    }

    /**
     * Return the rating for a given movie. If the movie is not in the list,
     * returns -1.
     * @param movieId movie id
     * @return rating of a movie with this movie id
     */
    public double getRating(int movieId) {
        RatingNode current = head;
        while(current!= null){
            if(current.getMovieId() == movieId){
                return current.getMovieRating();
            }
            current = current.next();
        }
        return -1;
    }

    /**
     * Insert a new node (with a given movie id and a given rating) into the list.
     * Insert it in the right place based on the value of the rating. Assume
     * the list is sorted by the value of ratings, from highest to smallest. The
     * list should remain sorted after this insert operation.
     *
     * @param movieId id of the movie
     * @param rating rating of the movie
     */
    public void insertByRating(int movieId, double rating) {

        RatingNode newNode = new RatingNode(movieId, rating);
        RatingNode current = head;
        RatingNode previous = null;
        if(head == null){
            head = newNode;
            head.setNext(null);
        }else{
            if(newNode.getMovieRating() > current.getMovieRating() || newNode.getMovieRating() == current.getMovieRating()) {
                newNode.setNext(current);
                head = newNode;
            }else{
                while(current != null){
                    if(current.getMovieRating() <= newNode.getMovieRating() ){
                        newNode.setNext(current);
                        previous.setNext(newNode);
                        return;
                    }
                    previous = current;
                    current = current.next();
                }
                previous.setNext(newNode);
            }
        }
    }

    /**
     * Computes similarity between two lists of ratings using Pearson correlation.
     * https://en.wikipedia.org/wiki/Pearson_correlation_coefficient
     * Note: You are allowed to use a HashMap for this method.
     *
     * @param otherList another RatingList
     * @return similarity computed using Pearson correlation
     */
    public double computeSimilarity(RatingsList otherList) {

        HashMap<Integer, Double> Xi  = new HashMap<Integer, Double>();
        int n = 0;
        RatingNode current1 = this.head;
        RatingNode current2 = otherList.head;
        while(current1 != null){
            Xi.put(current1.getMovieId(),current1.getMovieRating());
            current1 = current1.next();
        }
        Double sumXiYi = 0.0;
        Double sumXi = 0.0;
        Double sumYi = 0.0;
        Double sumXiSquared = 0.0;
        Double sumYiSquared = 0.0;
        while(current2 !=null){
            if(Xi.get(current2.getMovieId()) != null){
                Double firstRating = Xi.get(current2.getMovieId());
                Double secondRating = current2.getMovieRating();
                sumXiYi = sumXiYi + firstRating * secondRating;
                sumXi = sumXi + firstRating;
                sumYi = sumYi + secondRating;
                sumXiSquared = sumXiSquared + firstRating * firstRating;
                sumYiSquared = sumYiSquared + secondRating* secondRating;
                n = n + 1;
            }
            current2 = current2.next();
        }
        Double firstTerm = n *sumXiYi;
        Double secondTerm = sumXi * sumYi;
        Double thirdTerm = Math.sqrt(n * sumXiSquared-(sumXi*sumXi));
        Double fourthTerm = Math.sqrt(n* sumYiSquared-(sumYi * sumYi));
        Double finalNumber = (firstTerm-secondTerm)/(thirdTerm * fourthTerm);
        return finalNumber;
    }

    /**
     * Returns a sublist of this list where the rating values are in the range
     * from begRating to endRating, inclusive.
     *
     * @param begRating lower bound for ratings in the resulting list
     * @param endRating upper bound for ratings in the resulting list
     * @return sublist of the recommender.RatingsList that contains only nodes with
     * rating in the given interval
     */
    public RatingsList sublist(int begRating, int endRating) {

        RatingsList res = new RatingsList();
        RatingNode current = this.head;
        while(current != null){
           if((current.getMovieRating() <= begRating) & (current.getMovieRating() >= endRating)){
               res.insertByRating(current.getMovieId(),current.getMovieRating());
           }
           current = current.next();
        }
        return res;
    }

    /** Traverses the list and prints the ratings list in the following format:
     *  movieId:rating; movieId:rating; movieId:rating;  */
    public void print() {

        RatingNode current = this.head;
        while(current != null) {
            current = current.next();
        }
    }

    /**
     * Returns the middle node in the list - the one half way into the list.
     * Needs to have the running time O(n), and should be done in one pass
     * using slow & fast pointers (as described in class).
     *
     * @return the middle recommender.RatingNode
     */
    public RatingNode getMiddleNode() {

        RatingNode pointerForward = this.head;
        RatingNode pointerBack = this.head;
        pointerForward = pointerForward.next();
        pointerForward = pointerForward.next();
        pointerBack = pointerBack.next();
        while( pointerForward != null && pointerForward.next() != null ){
            pointerForward = pointerForward.next();
            pointerForward = pointerForward.next();
            pointerBack = pointerBack.next();
        }
        RatingNode middleNode = new RatingNode(pointerBack.getMovieId(),pointerBack.getMovieRating());
        return middleNode;
    }

    /**
     * Returns the median rating (the number that is halfway into the sorted
     * list). To compute it, find the middle node and return it's rating. If the
     * middle node is null, return -1.
     *
     * @return rating stored in the node in the middle of the list
     */
    public double getMedianRating() {

        RatingNode node = getMiddleNode();
        double medianRating = node.getMovieRating();
        int finalNum = (int) medianRating;
        return finalNum;
    }

    /**
     * Returns a recommender.RatingsList that contains n best rated movies. These are
     * essentially first n movies from the beginning of the list. If the list is
     * shorter than size n, it will return the whole list.
     *
     * @param n the maximum number of movies to return
     * @return recommender.RatingsList containing first n movie ratings
     */
    public RatingsList getNBestRankedMovies(int n) {

        RatingsList nBestMovies = new RatingsList();
        RatingNode current = this.head;
        RatingNode temp = new RatingNode(current.getMovieId(), current.getMovieRating());
        nBestMovies.head = temp;
        current = current.next();
        int i = 1;
        while(i < n ) {
            if(current != null) {
                RatingNode newNode = new RatingNode(current.getMovieId(), current.getMovieRating());
                nBestMovies.insertByRating(newNode.getMovieId(), newNode.getMovieRating());
            }
            current = current.next();
            i++;
        }
        return nBestMovies;
    }

    /**
     * * Returns a recommender.RatingsList that contains n worst rated movies for this user.
     * Essentially, these are the last n movies from the end of the list.
     * Note: This method should compute the result in one pass. Do not use the size variable.
     * Note: To find the n-th node from the end of the list, use the technique we discussed in class:
     * use two pointers, where first, you move only one pointer so that pointers are n-nodes apart,
     * and then move both pointers together until the first pointer reaches null; when it happens,
     * the second pointer would be pointing at the correct node.
     * Do NOT use reverse(). Do NOT destroy the list.
     *
     * @param n the maximum number of movies to return
     * @return recommender.RatingsList containing n lowest ranked movies (ranked by this user)
     */
    public RatingsList getNWorstRankedMovies(int n) {

        RatingsList nWorstMovies = new RatingsList();
        RatingNode pointerForward = this.head;
        RatingNode pointerBack = this.head;
        for(int i =1; i<n; i++){
            pointerForward = pointerForward.next();
        }
        while(pointerForward.next() != null){
            pointerForward = pointerForward.next();
            pointerBack = pointerBack.next();
        }
        RatingNode temp = new RatingNode(pointerBack.getMovieId(), pointerBack.getMovieRating());
        nWorstMovies.head = temp;
        pointerBack = pointerBack.next();
        while(pointerBack != null){
            RatingNode newNode = new RatingNode(pointerBack.getMovieId(), pointerBack.getMovieRating());
            nWorstMovies.insertByRating(newNode.getMovieId(), newNode.getMovieRating());
            pointerBack = pointerBack.next();
        }
        return nWorstMovies;
    }

    /**
     * Return a new list that is the reverse of the original list. The returned
     * list is sorted from lowest ranked movies to the highest rated movies.
     * Use only one additional recommender.RatingsList (the one you return) and constant amount
     * of memory. You may NOT use arrays, ArrayList and other built-in Java Collections classes.
     * Read description carefully for requirements regarding implementation of this method.
     *
     * @param head head of the RatingList to reverse
     * @return reversed list
     */
    public RatingsList reverse(RatingNode head) {

        RatingsList r = new RatingsList() ;
        r.head = head;
        RatingNode previousNode = null;
        RatingNode current = head;
        RatingNode nextNode = null;
        while(current != null){
            nextNode = current.next();
            current.setNext(previousNode);
            previousNode = current;
            current = nextNode;
        }
        r.head = previousNode;
        return r;
    }

    /**
     * Returns an iterator for the list
     * @return iterator
     */
    public Iterator<RatingNode> iterator() {

        return new RatingsListIterator(0);
    }

    // ------------------------------------------------------

    /**
     * Inner class, RatingsListIterator
     * The iterator for the ratings list. Allows iterating over the recommender.RatingNode-s of
     * the list.
     */
    private class RatingsListIterator implements Iterator<RatingNode> {

        private RatingNode nextNode;

        /**
         * Creates a new the iterator starting at a given index
         * @param index index
         */
        public RatingsListIterator(int index) {
            nextNode = head;
            for (int i = 0; i < index; i++) {
                nextNode = nextNode.next();
            }
        }

        /**
         * Checks if there is a "next" element of the list
         * @return true, if there is "next" and false otherwise
         */
        public boolean hasNext() {
            return nextNode != null; // don't forget to change
        }

        /**
         * Returns the "next" node and advances the iterator
         * @return next node
         */
        public RatingNode next() {

            if (!hasNext()) {
                System.out.println("No next element");
                throw new NoSuchElementException();
            }
            RatingNode temp = nextNode;
            nextNode = nextNode.next();
            return temp;
        }
        public void remove() {

            throw new UnsupportedOperationException();
        }
    }
}