public class ToDo extends Task{
    public ToDo(String desc) {
        this(desc, false, true);
    }

    public ToDo(String desc, boolean done) {
        this(desc, done, false);
    }

    private ToDo(String desc, boolean done, boolean shouldPrint) {
        super(desc, done);
        if (shouldPrint) {
            System.out.println("Got it. I've added this task");
            System.out.println(this.toString());
        }
    }

    @Override
    public void mark() {
        super.mark();
        System.out.println("I've marked this task as done.");
        System.out.println(this.toString());
    }

    @Override
    public void unmark() {
        super.unmark();
        System.out.println("I've marked this task as undone.");
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }

    @Override
    public String toFileString() {
        return String.format("T | %s | %s", this.getDoneStatus(), this.getDescription());
    }
}
