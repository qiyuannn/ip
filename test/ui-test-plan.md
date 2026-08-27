# UI Test Plan

These test cases check the Kong chatbot through console input and output.

For each test case, the expected output lists the important lines that must appear in order. The banner and separator lines do not need to be repeated unless they are the behavior being tested.

## TC1 Empty todo description

**Aim:** Check that `todo` without a description is rejected.

### Input
```text
todo
bye
```

### Expected Output
```text
Invalid command. A todo command needs to be in the following format: todo <description>
BYEBYE!
```

## TC2 Unknown command

**Aim:** Check that an unrecognized command is rejected.

### Input
```text
blah
bye
```

### Expected Output
```text
Sorry we do not recognise that command yet.
BYEBYE!
```

## TC3 Empty deadline description

**Aim:** Check that a deadline with no description is rejected.

### Input
```text
deadline /by tomorrow
bye
```

### Expected Output
```text
Invalid command. A deadline command needs to be in the following format: deadline <description> /by <date>
BYEBYE!
```

## TC4 Empty event from value

**Aim:** Check that an event with no `/from` value is rejected.

### Input
```text
event party /from /to 5pm
bye
```

### Expected Output
```text
Invalid command. An event command needs to be in the following format: event <description> /from <date> /to <date>
BYEBYE!
```

## TC5 Invalid mark number

**Aim:** Check that marking a task outside the list gives an error.

### Input
```text
mark 1
bye
```

### Expected Output
```text
This task number is invalid. You currently have 0 tasks in your list.
BYEBYE!
```


## TC6 Task changes are saved

**Aim:** Check that successful task list changes are written to `data/duke.txt`.

### Input
```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
mark 1
delete 2
bye
```

### Expected Output
```text
Got it. I've added this task
[T][ ] read book
Got it. I've added this task.
[D][ ] return book (by: June 6th)
Got it. I've added this task.
[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
I've marked this task as done.
[T][X] read book
The following task have been removed.
[D][ ] return book (by: June 6th)
BYEBYE!
```

### Expected Saved File
```text
T | 1 | read book
E | 0 | project meeting | Aug 6th 2pm | 4pm
```


## TC7 Saved tasks are loaded at startup

**Aim:** Check that tasks in `data/duke.txt` are loaded when Kong starts.

### Initial Saved File
```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

### Input
```text
list
bye
```

### Expected Output
```text
Here are the tasks in your list.
1. [T][X] read book
2. [D][ ] return book (by: June 6th)
3. [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
BYEBYE!
```
