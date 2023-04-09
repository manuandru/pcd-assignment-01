<!--- For math in pdf conversion
<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
<script type="text/x-mathjax-config">
  MathJax.Hub.Config({ tex2jax: {inlineMath: [['$', '$']]}, messageStyle: "none" });
</script>
-->
<!--- For table alignment and bord
<style>
table {
  margin: auto;
  width: auto;
  border: 1px solid black;
}
</style>
-->

# PCD Assignment 01

> Desclaimer: la prima parte di questo report è presente perché la soluzione adottata non scalava in performance con il JDK8.
>
> I test sono stati eseguiti con un processore 6 core, 12 Thread (`Runtime.getRuntime().availableProcessors() == 12`)


## Architettura per la lettura da file multi-threaded

### Produttore Consumatori

![Architettura produttore-consumatore](./img/architettura/prod-cons/prod-cons-schema.jpg)

Il primo approccio per la lettura da file è stata un'architettura produttore (1) e consumatori (n = #PROCESSORI) come componenti attivi: gli agenti condividono un Buffer di file da analizzare (*Monitor*).

- *Produttore*: inizializzato con la cartella (o più di una) da esplorare. Mantiene internamente una lista di cartelle da esplorare (*folders*) e, ad ogni iterazione, ne prende una. Data una cartella, ne controlla il contenuto e, per ogni elemento:
    - se è una *cartella* viene aggiunta a folders
    - se è un *file* viene aggiunto al Monitor

- *Consumatori*: competono sul monitor per ricevere file da analizzare, consumandoli.

A fronte dei test eseguiti, il singolo produttore è più rapido ad esplorare ricorsivamente le cartelle, rispetto al consumo dei file da parte dei consumatori.

[Implementazione produttore-consumatore](../part-01/src/pcd/concurrent_reading/producer_consumer/)

#### Performance

Per poter apprezzare i risultati, il codice è stato eseguito analizzando tutti i file che venivano trovati, partendo da una cartella con un numero sufficiente di elementi.

> I test sono stati ripetuti sempre sulla stessa directory.

##### JDK8 

Il JDK8 (*corretto-1.8.0_362*) non ha permesso di scalare notevolmente tramite la soluzione adottata, 
$$ S \approx 1.4 $$

<center>

| #Consumers | Time (ms) |
|:----------:|:---------:|
|      1     |   55892   |
|      2     |   49762   |
|      3     |   46678   |
|      4     |   48528   |
|     ...    |    ...    |
|     12     |   39397   |
|     13     |   39957   |

</center>

[VisualVM produttore consumatore JDK8](./img/architettura/prod-cons/visualvm-producer-consumer-jdk8.jpg)

##### JDK17

Il JDK17 (*Oracle OpenJDK version 17.0.2*) invece, ha permesso di scalare notevolmente tramite la soluzione adottata.

<center>

| #Consumers | Time (ms) |
|:----------:|:---------:|
|      1     |   58185   |
|      2     |   30982   |
|      3     |   21605   |
|      4     |   17010   |
|      5     |   14541   |
|      6     |   12878   |
|      7     |   11806   |
|     ...    |    ...    |
|     12     |   11328   |
|     13     |   10698   |

</center>

$$ S = \frac{T_1}{T_N} \Rightarrow \frac{58.185}{10.698} \approx 5.5  $$

[Grafico speed up Produttore Consumatori](./img/architettura/prod-cons/prod-cons-grafico.png)

[VisualVM produttore consumatore JDK17](./img/architettura/prod-cons/visualvm-producer-consumer-jdk17.jpg)


### Worker ricorsivi

![Architettura worker](./img/architettura/worker/worker-schema.jpg)

Il secondo approccio ideato per leggere da file mediante più Thread, è stato quello di sfruttare N *Worker*, un'unica componente attiva replicata. Tutti condividono un monitor (*TaskBag*) dove sono presenti i compiti da svolgere. A differenza dell'approccio precedente, ora sono gli worker stessi ad aggiungere nuovi Task, se necessario.

Questa idea nasce dal fatto che, se gli worker che consumano file sono più veloci di chi li produce, rimarrebbero in attesa.
Potendo far operare un worker sia come producer, che come consumer, questo permette di far svolgere i task a chiunque sia in attesa.

Il ciclo di vita del worker inizia richiedendo un task al monitor e, se disponibile, lo elabora:

- Se il Task ricevuto è un file (FileAnalyzerTask): lo analizza
- Se il Task ricevuto è una cartella (FolderAnalyzerTask), allora esplora la cartella ed inserisce tanti FileAnalyzerTask e FolderAnalyzerTask in relazione ai nodi che trova.

[Implementazione worker](../part-01/src/pcd/concurrent_reading/recursive_worker/)

#### Performance

Anche in questo caso il JDK8 si rivela peggiore del JDK17 in termini di scalabilità in lettura, al crescere del numero dei Thread.

##### JDK8 

$$ S \approx 1.1 $$

<center>

|   #Worker  | Time (ms) |
|:----------:|:---------:|
|      1     |   43190   |
|      2     |   45411   |
|      3     |   46572   |
|      4     |    ...    |
|     12     |   40136   |

</center>

[VisualVM worker JDK8](./img/architettura/worker/visualvm-worker-jdk8.jpg)

##### JDK17


$$ S \approx 5.5 $$

<center>

| #Workers | Time (ms) |
|:--------:|:---------:|
|     1    |   59429   |
|     2    |   30435   |
|     3    |   20950   |
|     4    |   15739   |
|     5    |   13182   |
|     6    |   11544   |
|    ...   |    ...    |
|    12    |   11485   |
|    13    |   10866   |


</center>

[Grafico speed up Workers](./img/architettura/worker/worker-grafico.png)

[VisualVM worker JDK17](./img/architettura/worker/visualvm-worker-jdk8.jpg)


## Part 01

A fronte dell'analisi sulla lettura dei file svolta in precedenza, è stata adottata la soluzione **Produttore - Consumatori**, dato che le prestazioni di velocità erano simili.

Il diagramma proposto in precedenza è stato dettagliato ulteriormente, in modo da tenere conto sia il flusso di controllo del singolo *Produttore*, che l'aggiunta di un *Monitor* per raccogliere le statistiche dei files.

> Per la lettura del grafico si può partire da sinistra verso destra:
>
> - La parte a sinistra è il ciclo di vita del **Producer** (Agente) che prende una cartella e la visita:
>   - per ogni cartella che trova, la inserisce nel buffer delle cartelle da visitare.
>   - per ogni file che trova, lo inserisce nel buffer di file da analizzare, ma deve attendere che il monitor sia disponibile.
>   - **Nota:** il buffer delle cartelle è interno al Producer, mentre il buffer dei file no, quindi quest'ultimo deve attendere il monitor per poter aggiungere i files e poter analizzare una nuova cartella.
>
> - La parte centrale è quella relativa alla **TaskBag** (Monitor) ed entrambe le tipologie di Agenti competono su essa:
>   - Il Producer cerca di aggiungere nuovi Task.
>   - I Consumers richiedono nuovi Task da svolgere.
>
> - La parte a destra rappresenta il ciclo di vita dei **Consumers** (Agenti). Ogni consumer sfrutta 2 Monitor:
>   - La TaskBag per ricevere il nuovo Task da svolgere che, una volta ricevuto, viene processato
>   - Il Monitor delle statistiche, dove vengono inserire queste ultime, un volta calcolate.

![Grafico architettura part 01](./img/part-01/part-01-schema.jpg)

### Performance

<center>



</center>