public class ToDo extends Task{
    public ToDo(String desc) {
        super(desc);
        System.out.println("Got it. I've added this task");
        System.out.println(this.toString());
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
        return String.format("[T] %s", super.toString());
    }
}
