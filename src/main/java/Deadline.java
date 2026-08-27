public class Deadline extends Task {
    private String by;

    public Deadline(String desc, String by) {
        this(desc, false, by, true);
    }

    public Deadline(String desc, boolean done, String by) {
        this(desc, done, by, false);
    }

    private Deadline(String desc, boolean done, String by, boolean shouldPrint) {
        super(desc, done);
        this.by = by;
        if (shouldPrint) {
            System.out.println("Got it. I've added this task.");
            System.out.println(this.toString());
        }
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

    @Override
    public String toFileString() {
        return String.format("D | %s | %s | %s", this.getDoneStatus(), this.getDescription(), this.by);
    }
}
