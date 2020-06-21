package recommender;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/** recommender.MovieRecommender. A class that is responsible for:
 - Reading movie and ratings data from the file and loading it in the data structure recommender.UsersList.
 *  - Computing movie recommendations for a given user and printing them to a file.
 *  - Computing movie "anti-recommendations" for a given user and printing them to file.
 *  Fill in code in methods of this class.
 *  Do not modify signatures of methods.
 */
public class MovieRecommender {

    private UsersList usersData; // linked list of users
    private HashMap<Integer, String> movieMap; // maps each movieId to the movie title

    public MovieRecommender() {

        movieMap = new HashMap<>();
        usersData = new UsersList();
    }

    /**
     * Read user ratings from the file and save data for each user in this list.
     * For each user, the ratings list will be sorted by rating (from largest to
     * smallest).
     * @param movieFilename name of the file with movie info
     * @param ratingsFilename name of the file with ratings info
     */

    public void loadData(String movieFilename, String ratingsFilename) {

        loadMovies(movieFilename);
        loadRatings(ratingsFilename);
    }

    /** Load information about movie ids and titles from the given file.
     *  Store information in a hashmap that maps each movie id to a movie title
     *
     * @param movieFilename csv file that contains movie information.
     *
     */
    private void loadMovies(String movieFilename) {

        movieMap = new HashMap<Integer, String>();
        try (BufferedReader br = new BufferedReader(new FileReader(movieFilename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String flag = "a";
                String c = ("\"");
                char quotationMark = c.charAt(0);
                for(char x: line.toCharArray()){
                    if(x == quotationMark){
                        flag = "b";
                    }
                }
                if(flag.equals("b")){
                    String[] separatedLineB = line.split(",");
                    char[] cArray = line.toCharArray();
                    String movieTitle = "";
                    int counter = -1;
                    int startInt = 0;
                    int endInt = 0;
                    boolean flag2 = false;
                    for(char y: cArray){
                        counter = counter + 1;
                        if(y == quotationMark & flag2 != true){
                            startInt = counter+1;
                            flag2 = true;
                        }
                        if(y == quotationMark & flag2 == true){
                            endInt = counter;
                        }
                    }
                    for(int i = startInt; i<endInt; i ++){
                        movieTitle = movieTitle + cArray[i];
                    }
                    movieMap.put(Integer.parseInt(separatedLineB[0]), movieTitle);
                }
                String[] separatedLine = line.split(",");
                if(flag.equals("a")){
                    if(!separatedLine[0].equals("movieId")){
                       movieMap.put(Integer.parseInt(separatedLine[0]), separatedLine[1]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read from the file: " + movieFilename);
        }
    }

    /**
     * Load users' movie ratings from the file into recommender.UsersList
     * @param ratingsFilename name of the file that contains ratings
     */
    private void loadRatings(String ratingsFilename) {

        try (BufferedReader br = new BufferedReader(new FileReader(ratingsFilename))) {
            String line;
            line = br.readLine();
            while ((line = br.readLine()) != null) { // read each line from the fill
                String[] splitLine = line.split(",");
                    int intUserID = Integer.parseInt(splitLine[0]);
                    int movieId = Integer.parseInt(splitLine[1]);
                    Double rating = Double.parseDouble(splitLine[2]);
                usersData.insert(intUserID, movieId, rating);
            }
        } catch (IOException e) {
            System.out.println("Could not read from the file: " + ratingsFilename);
        }
    }

    /**
     * * Computes up to num movie recommendations for the user with the given user
     * id and prints these movie titles to the given file. First calls
     * findMostSimilarUser and then getFavoriteMovies(num) method on the
     * "most similar user" to get up to num recommendations. Prints movies that
     * the user with the given userId has not seen yet.
     * @param userid id of the user
     * @param num max number of recommendations
     * @param filename name of the file where to output recommended movie titles
     *                 Format of the file: one movie title per each line
     */
    public void findRecommendations(int userid, int num, String filename) {

        UserNode User = usersData.get(userid);
        RatingsList ratingsList = User.getMovies();
        UserNode mostSimilarUser = usersData.findMostSimilarUser(userid);
        int[] reccomendedMovies = mostSimilarUser.getFavoriteMovies(num);
        try (PrintWriter pw = new PrintWriter(filename)) {
            for (int i = 0; i < reccomendedMovies.length; i++) {
                Double rating = ratingsList.getRating(reccomendedMovies[i]);
                if (rating == -1) {
                    pw.println(movieMap.get(reccomendedMovies[i]));
                    pw.flush();
                }
            }
        }
        catch (IOException e) {
            System.out.println("Could not read from the file: " + filename);
        }
    }

    /**
     * Computes up to num movie anti-recommendations for the user with the given
     * user id and prints these movie titles to the given file. These are the
     * movies the user should avoid. First calls findMostSimilarUser and then
     * getLeastFavoriteMovies(num) method on the "most similar user" to get up
     * to num movies the most similar user strongly disliked. Prints only
     * those movies to the file that the user with the given userid has not seen yet.
     * Format: one movie title per each line
     * @param userid id of the user
     * @param num max number of anti-recommendations
     * @param filename name of the file where to output anti-recommendations (movie titles)
     */
    public void findAntiRecommendations(int userid, int num, String filename) {
        UserNode user = usersData.get(userid);
        RatingsList ratingsList = user.getMovies();
        UserNode mostSimilarUser = usersData.findMostSimilarUser(userid);
        int[] antiReccomendedMovies = mostSimilarUser.getLeastFavoriteMovies(num);
        try (PrintWriter pw = new PrintWriter(filename)) {
            for (int i = 0; i < antiReccomendedMovies.length; i++) {
                Double rating = ratingsList.getRating(antiReccomendedMovies[i]);
                if (rating == -1) {
                    pw.println(movieMap.get(antiReccomendedMovies[i]));
                    pw.flush();
                }
            }
        }
        catch (IOException e) {
            System.out.println("Could not read from the file: " + filename);
        }
    }
}