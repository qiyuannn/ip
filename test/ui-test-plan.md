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
deadline return book /by 2019-10-15
event project meeting /from 2019-10-15 /to 2019-10-16
mark 1
delete 2
bye
```

### Expected Output
```text
Got it. I've added this task
[T][ ] read book
Got it. I've added this task.
[D][ ] return book (by: Oct 15 2019)
Got it. I've added this task.
[E][ ] project meeting (from: Oct 15 2019 to: Oct 16 2019)
I've marked this task as done.
[T][X] read book
The following task have been removed.
[D][ ] return book (by: Oct 15 2019)
BYEBYE!
```

### Expected Saved File
```text
T | 1 | read book
E | 0 | project meeting | 2019-10-15 | 2019-10-16
```


## TC7 Saved tasks are loaded at startup

**Aim:** Check that tasks in `data/duke.txt` are loaded when Kong starts.

### Initial Saved File
```text
T | 1 | read book
D | 0 | return book | 2019-10-15
E | 0 | project meeting | 2019-10-15 | 2019-10-16
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
2. [D][ ] return book (by: Oct 15 2019)
3. [E][ ] project meeting (from: Oct 15 2019 to: Oct 16 2019)
BYEBYE!
```


## TC8 Missing data folder starts empty

**Aim:** Check that Kong starts with an empty list when `data/duke.txt` and the `data` folder do not exist.

### Input
```text
list
bye
```

### Expected Output
```text
There are currently no tasks in your list.
BYEBYE!
```


## TC9 Invalid saved lines are skipped

**Aim:** Check that malformed saved lines are ignored while valid saved tasks are still loaded.

### Initial Saved File
```text

T | 1 | read book
X | 0 | mystery task
D | maybe | return book | 2019-10-15
D | 0 |
E | 0 | project meeting | 2019-10-15 | 2019-10-16
E | 0 | missing end | 2019-10-15
T | 0 | too | many
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
2. [E][ ] project meeting (from: Oct 15 2019 to: Oct 16 2019)
BYEBYE!
```


## TC10 Invalid deadline date

**Aim:** Check that deadline dates must use `yyyy-MM-dd`.

### Input
```text
deadline return book /by tomorrow
bye
```

### Expected Output
```text
Invalid date. Please use the format yyyy-MM-dd, for example 2019-10-15.
BYEBYE!
```

## TC11 Invalid event date

**Aim:** Check that event dates must use `yyyy-MM-dd`.

### Input
```text
event project meeting /from 2019-10-15 /to tomorrow
bye
```

### Expected Output
```text
Invalid date. Please use the format yyyy-MM-dd, for example 2019-10-15.
BYEBYE!
```


## TC12 Tasks on date

**Aim:** Check that `on <date>` lists deadlines due on that date and events occurring on that date.

### Input
```text
todo read book
deadline return book /by 2019-10-15
event conference /from 2019-10-14 /to 2019-10-16
event holiday /from 2019-10-20 /to 2019-10-21
on 2019-10-15
bye
```

### Expected Output
```text
Here are the deadlines and events on this date.
1. [D][ ] return book (by: Oct 15 2019)
2. [E][ ] conference (from: Oct 14 2019 to: Oct 16 2019)
BYEBYE!
```

## TC13 No tasks on date

**Aim:** Check that `on <date>` gives a clear message when no deadlines or events occur on that date.

### Input
```text
deadline return book /by 2019-10-15
on 2019-10-16
bye
```

### Expected Output
```text
There are no deadlines or events on this date.
BYEBYE!
```

## TC14 Invalid on date

**Aim:** Check that `on <date>` requires the `yyyy-MM-dd` date format.

### Input
```text
on tomorrow
bye
```

### Expected Output
```text
Invalid date. Please use the format yyyy-MM-dd, for example 2019-10-15.
BYEBYE!
```

## TC15 Empty on date

**Aim:** Check that `on` without a date is rejected.

### Input
```text
on
bye
```

### Expected Output
```text
Invalid command. An on command needs to be in the following format: on <date>
BYEBYE!
```

## TC16 Divider after exit

**Aim:** Check that Kong prints the divider line after the exit command finishes.

### Input
```text
bye
```

### Expected Output
```text
BYEBYE!
________________________________________
```

## TC17 Find matching tasks

**Aim:** Check that `find <keyword>` lists matching task descriptions in list order, regardless of case.

### Input
```text
todo read book
deadline return book /by 2019-10-15
event project meeting /from 2019-10-15 /to 2019-10-16
find BOOK
bye
```

### Expected Output
```text
Here are the matching tasks in your list:
1. [T][ ] read book
2. [D][ ] return book (by: Oct 15 2019)
BYEBYE!
```

## TC18 No matching tasks

**Aim:** Check that `find <keyword>` gives a clear message when no task descriptions match.

### Input
```text
todo read book
find movie
bye
```

### Expected Output
```text
There are no matching tasks in your list.
BYEBYE!
```

## TC19 Empty find keyword

**Aim:** Check that `find` without a keyword is rejected.

### Input
```text
find
bye
```

### Expected Output
```text
Invalid command. A find command needs to be in the following format: find <keyword>
BYEBYE!
```
