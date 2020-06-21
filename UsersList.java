package recommender;

/**
 * A custom linked list that stores user info. Each node in the list is of type
 * recommender.UserNode.
 * FILL IN CODE. Also, add other methods as needed.
 *
 * @author okarpenko
 *
 */
public class UsersList {
    private UserNode head = null;
    private UserNode tail = null;

    /** Insert the rating for the given userId and given movieId.
     *
     * @param userId  id of the user
     * @param movieId id of the movie
     * @param rating  rating given by this user to this movie
     */

    public void insert(int userId, int movieId, double rating) {

        UserNode current = null;
        current = head;
        if(head == null){
            UserNode headNode = new UserNode(userId);
            headNode.insert(movieId, rating);
            head = headNode;
            tail = headNode;
            return;
        }
        while(current != null){
            if(current.getId() == userId){
                current.insert(movieId, rating);
                return;
            }
            current = current.next();
        }
            UserNode newUser = new UserNode(userId);
            newUser.insert(movieId, rating);
            append(newUser);
    }
    /**
     * Append a new node to the list
     * @param newNode a new node to append to the list
     */

    public void append(UserNode newNode) {

        if(tail != null) {
            tail.setNext(newNode);
            tail = newNode;
        }
    }

    /** Return a recommender.UserNode given userId
     *
     * @param userId id of the user (as defined in ratings.csv)
     * @return recommender.UserNode for a given userId
     */
    public UserNode get(int userId) {

        UserNode current = head;
        while(current != null){
            if(current.getId() == userId){
                return current;
            }
            current = current.next();
        }
        return null;
    }

    /**
     * The method computes the similarity between the user with the given userid
     * and all the other users. Finds the maximum similarity and returns the
     * "most similar user".
     * Calls computeSimilarity method in class MovieRatingsList/
     *
     * @param userid id of the user
     * @return the node that corresponds to the most similar user
     */
    public UserNode findMostSimilarUser(int userid) {

        UserNode mostSimilarUser = null;
        Double mostSimilarScore = 0.0;
        UserNode comparisonNode = get(userid);
        if(comparisonNode == null){
            return null;
        }
        UserNode current = head;
        while(current != null){
            if(current.getId() != userid){
                Double result = comparisonNode.computeSimilarity(current);
                if (result> mostSimilarScore){
                    mostSimilarScore = result;
                    mostSimilarUser = current;
                }
            }
            current = current.next();
        }
        return mostSimilarUser;
    }

    /** Print recommender.UsersList to a file  with the given name in the following format:
     (userid) movieId:rating; movieId:rating; movieId:rating;
     (userid) movieId:rating; movieId:rating;
     (userid) movieId:rating; movieId:rating; movieId:rating; movieId:rating;
     Info for each userid should be printed on a separate line
     * @param filename name of the file where to output recommender.PList info
     */
    public void print(String filename) {

    }
}