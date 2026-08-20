public class Task {
    private String desc;
    private boolean done = false;

    public Task(String desc) {
        this.desc = desc;
    }

    public String mark() {
        this.done = true;
        return String.format("Nice! I've marked this task as done: \n%s", this.toString());
    }

    public String unmark() {
        this.done = false;
        return String.format("I've marked this task as not done yet:\n%s", this.toString());
    }

    @Override
    public String toString() {
        String out = "";
        if (this.done) {
            out += "[X]";
        } else {
            out += "[ ]";
        }
        return String.format("%s %s", out, this.desc);
    }
}
