public class Deadline extends Task {
    private String by;

    public Deadline(String desc, String by) {
        super(desc);
        this.by = by;
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

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.by);
    }
}
