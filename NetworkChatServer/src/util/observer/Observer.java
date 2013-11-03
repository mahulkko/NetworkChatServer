package util.observer;

/**
 * 
 * @author Martin Hulkkonen
 * @param <K> - Generic value for the update function
 */
public interface Observer<K> {
    
    /**
     * Update the observer
     * @param update - Generic value for the update
     */
    public void update(K update);
}
