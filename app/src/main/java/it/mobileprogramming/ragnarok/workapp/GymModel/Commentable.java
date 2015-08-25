package it.mobileprogramming.ragnarok.workapp.GymModel;

/**
 * Created by paride on 24/08/15.
 * Commentable interface, needs to be implemented if a class supports a feedback
 */

public interface Commentable {
    /**
     * set a comment for the item
     * @param comment string to comment
     */
    public void setComment(String comment);

    /**
     * set comment and rating for the item
     * @param comment string comment
     * @param rating int rating
     */
    public void setComment(String comment, int rating);

    /**
     * set a rating for the item
     * @param rating int rating
     */
    public void setRating(int rating);

    /**
     * returns the comment of the item
     * @return string comment
     */
    public String getComment();

    /**
     * returns the rating of the item
     * @return int rating
     */
    public int getRating();

    /**
     * very dirty way, simplest mode to make few code and avoid problems with parcelable items
     * @param serializer
     */
    public void setSerializer(Object serializer);//needed to resume without involve the whole tree of classes

}
