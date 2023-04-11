---------------------- MODULE pcd_assignment01_part02 ----------------------

EXTENDS TLC, Integers, Sequences, Naturals

(*--algorithm stoppable_file_exploring
variable
    TaskBag = <<>>;         \* Monitor for tasks
    finalResult = 0;        \* Monitor for summing lines
    lastUpdate = -1;
    noMoreTasks = FALSE;    \* Producer -> Consumers
    stopped = FALSE;        \* Monitor for stopping the system
    Consumers = { "cons1", "cons2", "cons3" };

define
    LineCountsAreOk == IF pc["producer"] = "Done" /\ (\A consumer \in Consumers: pc[consumer] = "Done")
                       THEN finalResult <= 12 /\ finalResult >= 0
                       ELSE TRUE

    ProducerEndsBeforeConsumers == []( (\E consumer \in Consumers: pc[consumer] = "Done") => pc["producer"] = "Done")

    IfStoppedNoMoreUpdateToResult == IF lastUpdate # -1
                                     THEN finalResult = lastUpdate
                                     ELSE TRUE
end define;

process stopper = "stopper"
begin Stopper:
    stopped := TRUE;
    lastUpdate := finalResult
end process;

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
    checkIfStopped:
        if stopped then
            filesToVisit := <<>>;
            TaskBag := <<>>;
        end if;
  end while;

endProducing:
  noMoreTasks := TRUE;

end process;


process consumer \in Consumers
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
        if somethingToDo /\ stopped = FALSE then
            finalResult := finalResult + task;
        end if;
  end while;

end process;

end algorithm;*)
\* BEGIN TRANSLATION (chksum(pcal) = "704fa187" /\ chksum(tla) = "110b5abd")
VARIABLES TaskBag, finalResult, lastUpdate, noMoreTasks, stopped, Consumers, 
          pc

(* define statement *)
LineCountsAreOk == IF pc["producer"] = "Done" /\ (\A consumer \in Consumers: pc[consumer] = "Done")
                   THEN finalResult <= 12 /\ finalResult >= 0
                   ELSE TRUE

ProducerEndsBeforeConsumers == []( (\E consumer \in Consumers: pc[consumer] = "Done") => pc["producer"] = "Done")

IfStoppedNoMoreUpdateToResult == IF lastUpdate # -1
                                 THEN finalResult = lastUpdate
                                 ELSE TRUE

VARIABLES file, filesToVisit, task, somethingToDo

vars == << TaskBag, finalResult, lastUpdate, noMoreTasks, stopped, Consumers, 
           pc, file, filesToVisit, task, somethingToDo >>

ProcSet == {"stopper"} \cup {"producer"} \cup (Consumers)

Init == (* Global variables *)
        /\ TaskBag = <<>>
        /\ finalResult = 0
        /\ lastUpdate = -1
        /\ noMoreTasks = FALSE
        /\ stopped = FALSE
        /\ Consumers = { "cons1", "cons2", "cons3" }
        (* Process producer *)
        /\ file = ""
        /\ filesToVisit = <<1, 2, 3, 3, 2, 1>>
        (* Process consumer *)
        /\ task = [self \in Consumers |-> 0]
        /\ somethingToDo = [self \in Consumers |-> TRUE]
        /\ pc = [self \in ProcSet |-> CASE self = "stopper" -> "Stopper"
                                        [] self = "producer" -> "Produce"
                                        [] self \in Consumers -> "Consume"]

Stopper == /\ pc["stopper"] = "Stopper"
           /\ stopped' = TRUE
           /\ lastUpdate' = finalResult
           /\ pc' = [pc EXCEPT !["stopper"] = "Done"]
           /\ UNCHANGED << TaskBag, finalResult, noMoreTasks, Consumers, file, 
                           filesToVisit, task, somethingToDo >>

stopper == Stopper

Produce == /\ pc["producer"] = "Produce"
           /\ IF Len(filesToVisit) > 0
                 THEN /\ pc' = [pc EXCEPT !["producer"] = "produce"]
                 ELSE /\ pc' = [pc EXCEPT !["producer"] = "endProducing"]
           /\ UNCHANGED << TaskBag, finalResult, lastUpdate, noMoreTasks, 
                           stopped, Consumers, file, filesToVisit, task, 
                           somethingToDo >>

produce == /\ pc["producer"] = "produce"
           /\ file' = Head(filesToVisit)
           /\ filesToVisit' = Tail(filesToVisit)
           /\ pc' = [pc EXCEPT !["producer"] = "put"]
           /\ UNCHANGED << TaskBag, finalResult, lastUpdate, noMoreTasks, 
                           stopped, Consumers, task, somethingToDo >>

put == /\ pc["producer"] = "put"
       /\ TaskBag' = Append(TaskBag, file)
       /\ pc' = [pc EXCEPT !["producer"] = "checkIfStopped"]
       /\ UNCHANGED << finalResult, lastUpdate, noMoreTasks, stopped, 
                       Consumers, file, filesToVisit, task, somethingToDo >>

checkIfStopped == /\ pc["producer"] = "checkIfStopped"
                  /\ IF stopped
                        THEN /\ filesToVisit' = <<>>
                             /\ TaskBag' = <<>>
                        ELSE /\ TRUE
                             /\ UNCHANGED << TaskBag, filesToVisit >>
                  /\ pc' = [pc EXCEPT !["producer"] = "Produce"]
                  /\ UNCHANGED << finalResult, lastUpdate, noMoreTasks, 
                                  stopped, Consumers, file, task, 
                                  somethingToDo >>

endProducing == /\ pc["producer"] = "endProducing"
                /\ noMoreTasks' = TRUE
                /\ pc' = [pc EXCEPT !["producer"] = "Done"]
                /\ UNCHANGED << TaskBag, finalResult, lastUpdate, stopped, 
                                Consumers, file, filesToVisit, task, 
                                somethingToDo >>

producer == Produce \/ produce \/ put \/ checkIfStopped \/ endProducing

Consume(self) == /\ pc[self] = "Consume"
                 /\ IF somethingToDo[self]
                       THEN /\ pc' = [pc EXCEPT ![self] = "take"]
                       ELSE /\ pc' = [pc EXCEPT ![self] = "Done"]
                 /\ UNCHANGED << TaskBag, finalResult, lastUpdate, noMoreTasks, 
                                 stopped, Consumers, file, filesToVisit, task, 
                                 somethingToDo >>

take(self) == /\ pc[self] = "take"
              /\ TaskBag # <<>> \/ noMoreTasks
              /\ IF TaskBag = <<>>
                    THEN /\ somethingToDo' = [somethingToDo EXCEPT ![self] = FALSE]
                         /\ UNCHANGED << TaskBag, task >>
                    ELSE /\ task' = [task EXCEPT ![self] = Head(TaskBag)]
                         /\ TaskBag' = Tail(TaskBag)
                         /\ UNCHANGED somethingToDo
              /\ pc' = [pc EXCEPT ![self] = "consume"]
              /\ UNCHANGED << finalResult, lastUpdate, noMoreTasks, stopped, 
                              Consumers, file, filesToVisit >>

consume(self) == /\ pc[self] = "consume"
                 /\ IF somethingToDo[self] /\ stopped = FALSE
                       THEN /\ finalResult' = finalResult + task[self]
                       ELSE /\ TRUE
                            /\ UNCHANGED finalResult
                 /\ pc' = [pc EXCEPT ![self] = "Consume"]
                 /\ UNCHANGED << TaskBag, lastUpdate, noMoreTasks, stopped, 
                                 Consumers, file, filesToVisit, task, 
                                 somethingToDo >>

consumer(self) == Consume(self) \/ take(self) \/ consume(self)

(* Allow infinite stuttering to prevent deadlock on termination. *)
Terminating == /\ \A self \in ProcSet: pc[self] = "Done"
               /\ UNCHANGED vars

Next == stopper \/ producer
           \/ (\E self \in Consumers: consumer(self))
           \/ Terminating

Spec == Init /\ [][Next]_vars

Termination == <>(\A self \in ProcSet: pc[self] = "Done")

\* END TRANSLATION 
=============================================================================
\* Modification History
\* Last modified Mon Apr 10 19:57:01 CEST 2023 by manuandru
\* Created Mon Apr 10 11:11:23 CEST 2023 by manuandru
