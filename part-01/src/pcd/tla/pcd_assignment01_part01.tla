---------------------- MODULE pcd_assignment01_part01 ----------------------

EXTENDS TLC, Integers, Sequences

(*--algorithm file_exploring
variable
    TaskBag = <<>>; \* Monitor for tasks
    lineCount = 0;        \* Monitor for summing lines
    noMoreTasks = FALSE;

define
    lineCountsAreOk == IF pc["producer"] = "Done" /\ pc["cons1"] = "Done" /\ pc["cons2"] = "Done" /\ pc["cons3"] = "Done"
                       THEN lineCount = 12
                       ELSE TRUE

    ProducerEndsBeforeConsumers == []((
                                    pc["cons1"] = "Done"
                                    \/ pc["cons2"] = "Done"
                                    \/ pc["cons3"] = "Done"
                                    ) => pc["producer"] = "Done")
end define;

process producer = "producer"
variable
    file = "";
    filesToVisit = <<1, 2, 3, 3, 2, 1>>;
begin Produce:

  while Len(filesToVisit) > 0 do
    produce:
        file := Head(filesToVisit);
        filesToVisit := Tail(filesToVisit);
    put:
        TaskBag := Append(TaskBag, file);
  end while;

endProducing:
  noMoreTasks := TRUE;

end process;


process consumer \in { "cons1", "cons2", "cons3" }
variable
    task = 0;
    somethingToDo = TRUE;
begin Consume:

  while somethingToDo do
    take:
        await TaskBag # <<>> \/ noMoreTasks;
        if TaskBag = <<>> then
            somethingToDo := FALSE;
        else
            task := Head(TaskBag);
            TaskBag := Tail(TaskBag);
        end if;
    consume:
        if somethingToDo then
            lineCount := lineCount + task;
        end if;
  end while;

end process;

end algorithm;*)
\* BEGIN TRANSLATION (chksum(pcal) = "883da549" /\ chksum(tla) = "4238a679")
VARIABLES TaskBag, lineCount, noMoreTasks, pc

(* define statement *)
lineCountsAreOk == IF pc["producer"] = "Done" /\ pc["cons1"] = "Done" /\ pc["cons2"] = "Done" /\ pc["cons3"] = "Done"
                   THEN lineCount = 12
                   ELSE TRUE

ProducerEndsBeforeConsumers == []((
                                pc["cons1"] = "Done"
                                \/ pc["cons2"] = "Done"
                                \/ pc["cons3"] = "Done"
                                ) => pc["producer"] = "Done")

VARIABLES file, filesToVisit, task, somethingToDo

vars == << TaskBag, lineCount, noMoreTasks, pc, file, filesToVisit, task, 
           somethingToDo >>

ProcSet == {"producer"} \cup ({ "cons1", "cons2", "cons3" })

Init == (* Global variables *)
        /\ TaskBag = <<>>
        /\ lineCount = 0
        /\ noMoreTasks = FALSE
        (* Process producer *)
        /\ file = ""
        /\ filesToVisit = <<1, 2, 3, 3, 2, 1>>
        (* Process consumer *)
        /\ task = [self \in { "cons1", "cons2", "cons3" } |-> 0]
        /\ somethingToDo = [self \in { "cons1", "cons2", "cons3" } |-> TRUE]
        /\ pc = [self \in ProcSet |-> CASE self = "producer" -> "Produce"
                                        [] self \in { "cons1", "cons2", "cons3" } -> "Consume"]

Produce == /\ pc["producer"] = "Produce"
           /\ IF Len(filesToVisit) > 0
                 THEN /\ pc' = [pc EXCEPT !["producer"] = "produce"]
                 ELSE /\ pc' = [pc EXCEPT !["producer"] = "endProducing"]
           /\ UNCHANGED << TaskBag, lineCount, noMoreTasks, file, filesToVisit, 
                           task, somethingToDo >>

produce == /\ pc["producer"] = "produce"
           /\ file' = Head(filesToVisit)
           /\ filesToVisit' = Tail(filesToVisit)
           /\ pc' = [pc EXCEPT !["producer"] = "put"]
           /\ UNCHANGED << TaskBag, lineCount, noMoreTasks, task, 
                           somethingToDo >>

put == /\ pc["producer"] = "put"
       /\ TaskBag' = Append(TaskBag, file)
       /\ pc' = [pc EXCEPT !["producer"] = "Produce"]
       /\ UNCHANGED << lineCount, noMoreTasks, file, filesToVisit, task, 
                       somethingToDo >>

endProducing == /\ pc["producer"] = "endProducing"
                /\ noMoreTasks' = TRUE
                /\ pc' = [pc EXCEPT !["producer"] = "Done"]
                /\ UNCHANGED << TaskBag, lineCount, file, filesToVisit, task, 
                                somethingToDo >>

producer == Produce \/ produce \/ put \/ endProducing

Consume(self) == /\ pc[self] = "Consume"
                 /\ IF somethingToDo[self]
                       THEN /\ pc' = [pc EXCEPT ![self] = "take"]
                       ELSE /\ pc' = [pc EXCEPT ![self] = "Done"]
                 /\ UNCHANGED << TaskBag, lineCount, noMoreTasks, file, 
                                 filesToVisit, task, somethingToDo >>

take(self) == /\ pc[self] = "take"
              /\ TaskBag # <<>> \/ noMoreTasks
              /\ IF TaskBag = <<>>
                    THEN /\ somethingToDo' = [somethingToDo EXCEPT ![self] = FALSE]
                         /\ UNCHANGED << TaskBag, task >>
                    ELSE /\ task' = [task EXCEPT ![self] = Head(TaskBag)]
                         /\ TaskBag' = Tail(TaskBag)
                         /\ UNCHANGED somethingToDo
              /\ pc' = [pc EXCEPT ![self] = "consume"]
              /\ UNCHANGED << lineCount, noMoreTasks, file, filesToVisit >>

consume(self) == /\ pc[self] = "consume"
                 /\ IF somethingToDo[self]
                       THEN /\ lineCount' = lineCount + task[self]
                       ELSE /\ TRUE
                            /\ UNCHANGED lineCount
                 /\ pc' = [pc EXCEPT ![self] = "Consume"]
                 /\ UNCHANGED << TaskBag, noMoreTasks, file, filesToVisit, 
                                 task, somethingToDo >>

consumer(self) == Consume(self) \/ take(self) \/ consume(self)

(* Allow infinite stuttering to prevent deadlock on termination. *)
Terminating == /\ \A self \in ProcSet: pc[self] = "Done"
               /\ UNCHANGED vars

Next == producer
           \/ (\E self \in { "cons1", "cons2", "cons3" }: consumer(self))
           \/ Terminating

Spec == Init /\ [][Next]_vars

Termination == <>(\A self \in ProcSet: pc[self] = "Done")

\* END TRANSLATION 
=============================================================================
\* Modification History
\* Last modified Mon Apr 10 19:57:01 CEST 2023 by manuandru
\* Created Mon Apr 10 11:11:23 CEST 2023 by manuandru
