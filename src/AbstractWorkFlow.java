public abstract class AbstractWorkFlow<T> {
    public T result;

    public final T startWorkFlow(PlayerFileHandler playerFileHandler) {
        process();
        save(playerFileHandler);
        return result;
    }

    public final T startWorkFlow(TeamFileHandler teamFileHandler) {
        process();
        save(teamFileHandler);
        return result;
    }

    protected abstract void process();

    protected abstract void save(PlayerFileHandler playerHandler);

    protected abstract void save(TeamFileHandler teamHandler);
}
