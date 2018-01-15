// Classe madre di tutti i nemici, raccoglie tutte le caratteristiche che hanno in comune.
class Nemico{
  int lun, alt, coorX, coorY, lunIm, altIm; // Variabili di identico scopo a quelle in Hero con lo stesso nome
  int coll, count;  // gestione della collisione e del reset.
  PImage I[]; // Array di immagini del nemico. A volte ne basta una, altre più di una perché c'è un'animazione.
  
  Nemico(int l, int a, int y, int li, int ai, int ni){  // Al metodo costruttore della superclasse serve sapere: la grandezza dell'area di collisione e dell'immagine, l'ordinata in cui disegnare il nemico, il numero delle immagini da caricare.
    lun = l;
    alt = a;
    coorY = y; 
    lunIm = li;
    altIm = ai;
    coorX = -lunIm; 
    coll = 0;
    count = int(random(100,1000));
    I = new PImage[ni];
  }
  
  void disegna(){}  // Metodo virtuale che viene sovrascritto diverso in ogni classe figlia a seconda delle loro funzioni
  
  // Il ragionamento è questo: tutti i nemici di un livello sono inizializzati dietro il protagonista, dove non possono essere visti. La variabile count comincia a decrementarsi.
  // Quando la loro variabile count arriva a 0, allora viene trasportato dal metodo reset ad una certa distanza del margine destro della finestra, da cui comincerà ad avanzare verso il protagonista.
  // L'impirgo di due distanze random, impedisce tranne rari casi che i nemici siano così vicini da non poter essere schivati, e se questo succede si fa in modo che almeno uno di essi possa essere
  // eliminato con un calcio volante. Nelle classi nemico più semplici, come Poppo, Spine o Missile queste zone di codice sono evidenti.
  
  void reset(){
    coorX = width + int(random(0,1000));
    coll = 0;
    count = int(random(0,250));
  }
  
  void collisione(Hero h){  // Il metodo collisione tranne rare eccezioni è uguale per tutti i nemici. Controlla se il rettangolo che forma l'area di collisione del nemico e quello del giocatore si sono incrociati e in caso affermativo ferisce il nemico.
    float abbcoef;
    if(h.posizione == 6) abbcoef = 0;
    else                 abbcoef = h.alt;
    if(coorX-lun <= h.coorX+h.lun && coorX+lun >= h.coorX-h.lun
        && coorY-alt <= h.coorY+abbcoef && coorY+alt >= h.coorY-h.alt){
      if(h.posizione != 4 && coll == 0){
        h.ferisci();
        coll = 1;  // Ogni nemico può ferire il protagonista una sola volta. Pertanto se l'ho colpito coll assume valore 1 e non riesco più a colpirlo.
      }
      else if(coll == 0){  // Se però il giocatore stava sferrando un calcio volante il nemico viene sconfitto e non può infliggere più danni.
        h.posizione = 2;
        h.altsalto += height-coorY;   // il personaggio spicca un salto sconfiggendo il nemico
        coll = 2;  // coll assume valore 2 per permettere di inserire immagini differenti se il nemico viene colpito.
      }
    }
  }
  // Il metodo collisione deve essere chiamato al termine di ogni metodo disegna nelle classi figlie.
}
