public class Task {
    private String description;
    private boolean done = false;

    public Task(String desc) {
        this.description = desc;
    }

    public void mark() {
        this.done = true;
    }
    public void unmark() {
        this.done = false;
    }

    @Override
    public String toString() {
        String out = "";
        if (this.done) {
            out += "[X]";
        } else {
            out += "[ ]";
        }
        return String.format("%s %s", out, this.description);
    }
}
