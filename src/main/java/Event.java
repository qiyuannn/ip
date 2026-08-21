public class Event extends Task{
    private String from;
    private String to;

    public Event(String desc, String from, String to) {
        super(desc);
        this.from = from;
        this.to = to;

        System.out.println("Got it. I've added this task.");
        System.out.println(this.toString());
    }

    @Override public void mark() {
        super.mark();
        System.out.println("I've marked this task as done.");
        System.out.println(this.toString());
    }

    @Override public void unmark() {
        super.unmark();
        System.out.println("I've marked this task as undone.");
        System.out.println(this.toString());
    }

    @Override public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), this.from, this.to);
    }
}
