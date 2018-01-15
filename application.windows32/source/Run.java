import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.spi.*; 
import ddf.minim.signals.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.ugens.*; 
import ddf.minim.effects.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Run extends PApplet {

// un gioco del 2015 di Pietro Martello
// Libreria per mettere la musica







int sizealt = 300, sizelun = 700;  // Variabili per la misura della finestra, possono essere cambiate da qui.
Motore m;  // L'oggetto motore contiene tutte le altre classi e regola le varie fasi del gioco.
Minim minim;  // Oggetto di default della libreria sonora minim.
boolean pause;  // Boolean che permette di mettere in pausa il gioco in qualsiasi momento.


public void setup(){
  size(sizelun,sizealt);
  minim = new Minim(this);  // Inizializzazione di Minim, vuole il path del programma.
  m = new Motore();  // Inizializzazione del motore, richiama a catena l'inizializzazione di tutti gli altri oggetti.
  pause = false;    // Il gioco non \u00e8 in pausa all'inizio. 
}

public void draw(){
  if(!pause) m.play();  // se il gioco non \u00e8 in pausa, l'oggetto motore si occupa di tutto. Tranquillo.
}

public void keyReleased(){  //Viene effettuato un controllo periodico sulla tastiera per decidere quando mettere in pausa.
  if(key == 'p'){
    pause = !pause;
    textSize(50);
    fill(255,0,0);
    text("PAUSA",width/2-100,height/2-25,200,50);
    textSize(25);
    text("Premi 'p' per ricominciare",width/2-160,height/2+25,320,25);
  }
  // Il blocco di codice mette o toglie la pausa, e inserisce la scritta che dice che il programma \u00e8 in pausa.
  // Tale scritta viene inserita anche quando viene tolta la pausa, ma verr\u00e0 immediatamente cancellata alla prossima iterazione di draw, ovvero appena il programma riprende.
}
class Avvoltoio extends Nemico{  // Nemico volante avanzato
  int cont, ind; //Controllori per l'animazione
  int tetto, posizione, base;  //Questo nemico svolazza, ovvero va su e gi\u00f9 mentre si avvicina, queste variabili servono per gestire lo svolazzo
  // posizione = 1 -> sali
  // posizione = 2 -> scendi
  // tetto -> massima altezza raggiungibile
  // base -> minima altezza raggiungibile
  Avvoltoio(){
    super(34,15,PApplet.parseInt(random(height-200,height-55)),40,40,2);
    for(int i=0; i<2; i++)
      I[i] = loadImage("Avvoltoio "+(i+1)+".png");
    tetto = constrain(coorY-PApplet.parseInt(random(0,60)),50,220);
    posizione = PApplet.parseInt(random(1,3));
    base = constrain(coorY+PApplet.parseInt(random(0,60)),100,220);
    ind = cont = 0;
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed*2+1;  // L'avvoltoio vola e va veloce.
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(posizione == 1){  // Si alza finch\u00e9 non arriva al tetto.
        coorY-=2;
        if(coorY <= tetto)  posizione = 2;
      }
      else if(posizione == 2){  // Si abbassa finch\u00e9 non arriva alla base
        coorY+=2;
        if(coorY >= base)  posizione = 1;
      }
      if(coll != 2){
        image(I[ind],coorX-lunIm,coorY-altIm-20,lunIm*2,altIm*2);
        cont++;
        if(cont == 6){
          ind = (++ind)%(I.length);
          cont = 0;
        }
      }
      else{
        coorY = height-40;
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();  // Le aggiunte a Reset servono a resettare parametri posseduti solo dal nemico in questione.
        coorY = PApplet.parseInt(random(height-200,height-55));
        tetto = constrain(coorY-PApplet.parseInt(random(10,50)),50,220);
        base = constrain(coorY+PApplet.parseInt(random(10,50)),100,220);
      }
    }
    collisione(m.h);
  }
}
class Disco extends Nemico{
  int vel, tricky;  // Questo nemico accelera in modo randomico
  Disco(int speed){
    super(33,15,PApplet.parseInt(random(height-200,height-55)),37,19,1);
    I[0] = loadImage("Disco.png");
    vel = speed;
    tricky = 1;  // L'accelerazione o la decelerazione viene effettuata quando questa variabile arriva a 0.
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= vel;
      tricky--;
      if(tricky == 0){
        if(m.diff == 2)  vel = PApplet.parseInt(random(m.speed-1,m.speed*3));
        else           vel = PApplet.parseInt(random(m.speed,m.speed*3));
        tricky = PApplet.parseInt(random(1,100));
      }
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coll != 2)  image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      else{
        coorY = height-40;
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();
        coorY=PApplet.parseInt(random(height-200,height-55));
      }
    }
    collisione(m.h);
  }
}
// Questa classe gestisce l'avatar del personaggio.
class Hero{
  float alt, lun, coorX, coorY, salto, altIm, lunIm, altsalto;  // coorX e coorY sono la posizione del personaggio, alt e lun sono i lati del rettangolo dell'area di collisione del protagonista, altIm e lunIm servono a dare le coordinate all'immagine
  // salto e altsalto servono a stabilire rispettivamente quando il personaggio tocca terra e qual'\u00e8 la massima altezza che pu\u00f2 raggiungere.
  PImage ar[];  // Tutte le immagini di cui ho bisogno.
  int posizione;  //Indica l'azione che sta compiendo
//  1 -> camminata
//  2 -> salto
//  3 -> atterraggio
//  4 -> calcio
//  5,6 -> scivolata 
  int colpito;  //Indica se \u00e8 stato appena subito un danno e per quanto tempo se ne sar\u00e0 immuni
  int cont, ind, scivol;  //Indicatori d'animazione
  float vita, stamina;  //Vita ed energia per compiere azioni.
  
  
  Hero(){
    alt = 60;
    lun = 22;
    altIm = 60;
    lunIm = 40;
    coorX = 70;
    salto = coorY = height - 76;
    altsalto = 150;
    posizione = 1;
    scivol = 1;
    colpito = 1;
    ar = new PImage[10]; // Caricamento delle immagini
    for(int i=0; i<10; i++){
      ar[i] = loadImage("Ninja "+(i+1)+".png");
    }
    cont = 0;
    ind = 0;
    vita = 5;
    stamina = 50;
  }
  
  
  public void disegna(){ // Metodo richiamato ad ogni iterazione di draw. Disegna il personaggio e ne stabilisce l'azione a seconda dell'input di tastiera.
    
    // Interazioni con la testiera. Credo siano abbastanza esplicative.
    if(keyPressed){
      if(key == CODED){
        if(keyCode == UP){
          salta();
        }
      }
    }
    if(keyPressed){
      if(key == CODED){
        if(keyCode == DOWN){
          vaigiu();
        }
      }
    }
    if(posizione == 2){  // Vuol dire che sto saltando, quindi decremento coorY finch\u00e9 non arrivo al valore indicato da altsalto, poi cambio la posizione.
      coorY-=3;
      if(coorY <= salto-altsalto)  posizione = 3;
    }
    if(posizione == 3 || posizione == 4){  // Vuol dire che ho terminato il salto, quindi incremento coorY finch\u00e9 non torno a terra, poi ricomincio a correre (posizione = 1)
      coorY+=3;
      altsalto = 150;
      if(posizione == 4) coorY+=3;  // Se sto facendo un calcio volante cado pi\u00f9 velocemente. Sono tecniche ninja avanzatissime.
      if(coorY >= salto){
        coorY = salto;
        posizione = 1;
      }
    }
    if(posizione == 5){  // Mi sto abbassando.
      abbassati();
    }
    if(posizione == 6 && m.stato != 0){  // Mi sto alzando, ma solo se non tengo la posizione continuando a premere il pulsante che mi sta facendo abbassare.
      if(!keyPressed){
        rialzati();
      }
    }
    // Questa parte commentata permette di visualizzare l'area di collisione dell'avatar o dei nemici. Solo nel caso di Hero \u00e8 formata dall'intersezione di due rettangoli, non uno.
//    stroke(0);
//    fill(255,0);
//    rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
//      fill(0,0,255,128);
//      rect(coorX,coorY+alt,lun,alt);
    if(!(colpito%3==0)){  // Questo if permette al personaggio di lampeggiare per un po' quando viene colpito, finch\u00e9 non ritorna vulnerabile.
      if(posizione == 2 || posizione == 3){
        image(ar[7],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else if(posizione == 4){
        image(ar[6],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else if(posizione == 5 || posizione == 6){
        image(ar[8],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else{
        image(ar[ind],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
    }
    if(colpito > 1)  colpito--;  // Decrementa il periodo in cui l'eroe \u00e8 invulnerabile subito dopo aver subito danni.
    if(posizione == 1) cont++;
    if(cont == 5){
      ind = (++ind)%(ar.length - 4);
      cont = 0;
    } // Questi ultimi due if regolano l'animazione. Una struttura simile si trova in ogni oggetto che prevede un'animazione. Scorre un'array di immagini o parte di esso visualizzandone una per poco tempo e poi passando alla successiva.
    
    
    displayVita();
    
  }
  
  
  public void muori(){  // Viene richiamato se esaurisco la vita, mostra l'immagine del protagonista svenuto che scivola lentamente via.
    image(ar[9],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
    if(coorX >= -lunIm){
      cont++;
      if(cont%5 == 0){
        coorX -= scivol;
        if(cont%25 == 24) scivol = scivol*2; 
      }
    }
  }
  
  public void reset(){   // Una copia del costruttore per resettare il gioco o le caratteristiche del personaggio
    alt = 60;
    lun = 22;
    altIm = 60;
    lunIm = 40;
    coorX = 70;
    salto = coorY = height - 76;
    altsalto = 150;
    posizione = 1;
    scivol = 1;
    colpito = 1;
    cont = 0;
    ind = 0;
    vita = 5;
    stamina = 50;
  }
  
  
  public void displayVita(){  // Mostra la vita nell'angolo in alto a destra della finestra, oltre a ripristinare la stamina ad ogni chiamata.
    rectMode(CORNER);
    stroke(0,0);
    fill(255,0,0);
    rect(width-160,5,vita*30,15);
    fill(0xff11FF11);
    rect(width-160,20,stamina*2,15);
    if(stamina < 50 && posizione == 1 && cont == 0){
      stamina+=2;
    }
  }
  
  public void salta(){  // Cambia la posizione del protagonista iniziando il salto.
    if(stamina >= 5){
      if(posizione == 1){
        posizione = 2;
        stamina-=5;
      }
      if(posizione == 5 || posizione == 6){
        rialzati();
        posizione = 2;
      }
    }
  }
  
  public void abbassati(){  // Cambia l'area in cui il personaggio \u00e8 vulnerabile e il punto dove si mettono le immagini quando sto scivolando.
    coorY = height - 38;
    float swap = alt;
    alt = lun;
    lun = swap;
    swap = altIm;
    altIm = alt+9;
    lunIm = lun+8;
    posizione = 6;
    stamina-=5;
  }
  
  public void rialzati(){  // Ripristina l'area di collisione e le immagini quando finisco di scivolare.
    posizione = 1;
    coorY = height - 76;
    float swap = alt;
    alt = lun;
    lun = swap;
    altIm = alt;
    lunIm = lun+18;
  }
  
  public void vaigiu(){  // In base alla situazione, permette di abbassarsi oppure di eseguire un calcio volante.
    if(posizione == 1 && stamina >= 5) posizione = 5;
    else if((posizione == 3 || posizione == 2) && stamina >= 5){
      posizione = 4;
      stamina-=5;
    }
  }
  
  public void ferisci(){  // Viene richiamato da un oggetto derivato da nemico se colpisce il personaggio. Decrementa vita e decide cosa fare in base alla difficolt\u00e0.
    if(colpito == 1){  // Non viene attivato se il personaggio \u00e8 stato colpito da poco da qualche altro nemico. Colpito viene inizializzato da ferisci e viene decrementato dal metodo disegna.
      colpito = 73;
      vita--;
      if(m.diff == 0)  m.colpito++;
      if(vita == 0){
        if(m.diff == 0) vita = 5;
        else{
          abbassati();
          m.stato = 0;
          m.pano.setGameOver();
        }
      }
    }
  }
  
  public void cura(){  // Metodo esclusivo che viene richiamato solo da Pozione.
    if(vita < 5) vita++;
  }
}

class Johnny extends Nemico{  // Nemico spinoso piuttosto altino.
  Johnny(){
    super(23,20,height-50,39,35,1);
    I[0] = loadImage("Johnny.png");
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);;
    }
    else{
      count--;
      if(count <= 0){
        reset();
      }
    }
    collisione(m.h);
  }
  public void collisione(Hero h){  // Questo nemico fa l'overwriting di collisione perch\u00e9 non pu\u00f2 essere sconfitto, ma danneggia sempre il personaggio.
    float abbcoef;
    if(h.posizione == 6) abbcoef = 0;
    else                 abbcoef = h.alt;
    if(coorX-lun <= h.coorX+h.lun && coorX+lun >= h.coorX-h.lun
        && coorY-alt <= h.coorY+abbcoef && coorY+alt >= h.coorY-h.alt){
      h.ferisci();
      coll = 1;
    }
  }
}
class Missile extends Nemico{   // Nemico volante di base
  int cont, ind; //Indicatori d'animazione
  Missile(){
    super(33,14,PApplet.parseInt(random(height-200,height-55)),37,17,4);
    for(int i=0; i<3; i++){
      I[i] = loadImage("Missile "+(i+1)+".png");
    }
    I[3] = loadImage("Esplosione.png");
    ind = cont = 0;
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed*2;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coll != 2) image(I[ind],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      else{
        coorX += m.speed;
        image(I[3],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();
        coorY=PApplet.parseInt(random(height-200,height-55));
      }
    }
    cont++;
    if(cont == 5){
      ind = (++ind)%(I.length-1);
      cont = 0;
    }
    collisione(m.h);
  }
}
// La classe motore contiene tutte le classi e i metodi creati da me per questo gioco.
class Motore{
  int speed;  // Velocit\u00e0 con cui scorre lo sfondo e i nemici vanno addosso al giocatore. Conservandola qua pu\u00f2 essere cambiata durante il gioco, anche se non l'ho mai fatto avvenire.
  int stato, narrator, diff, punteggio, credits, creditime, colpito;  // Lo stato indica in quale momento del gioco ci si trova. All'interno del metodo play si trova una chiara suddivisione.
  // narrator viene utilizzato nel men\u00f9 principale per permettere la navigazione.
  // Il punteggio viene incrementato automaticamente ogni iterazione di draw e raggiunta una certa soglia permette di passare di livello
  // diff indica difficolt\u00e0. Viene scelta nel men\u00f9 principale e ha varie implicazioni:
  // n00b -> diff = 0. God Mode. Il personaggio non pu\u00f2 essere ucciso. Questo permette di fare pratica e di visualizzare tutto il gioco in una volta. Non \u00e8 molto bello dal punto di vista della giocabilit\u00e0, ma l'ho inserito per lei, prof. Questo livello non permette di accedere al vero finale
  // ninja -> diff = 1. Difficolt\u00e0 normale. Altro -> diff = 2. Difficolt\u00e0 elevata. Nemici aggiuntivi e pi\u00f9 forti.
  // credits e creditime sono due variabili per gestire i crediti finali.
  // Diversa dalla variabile con lo stesso nome in Hero, viene incrementata ogni volta che il personaggio viene colpito durante la God Mode.
  PFont font, gameo;  // Due font molto belli.
  PImage titolo;   // Non ho trovato un font abbastanza grande per il titolo, quindi l'ho disegnato io.
  
  Hero h;  // Oggetto contenente tutte le informazioni riguardo all'avatar del giocatore.
  Nemico nemici[];  // Collezione di oggetti derivati dalla classe nemici.
  Panorama pano;   // Oggetto contenente tutte le informazioni riguardanti sfondo e musica.
  
  Motore(){
    pano = new Panorama();  // Richiamo il metodo costruttore di Panorama.
    h = new Hero();  // Richiamo il costruttore di Hero. Tutti questi costruttori devono essere richiamati nel setup a cascata perch\u00e9 caricano delle immagini.
    speed = 3;
    nemici = new Nemico[15];  // Dopo aver deciso quanti nemici voglio, vengono creati. I costruttori a cui passo "speed" necessitavano tale informazione nel costruttore.
    nemici[0] = new Spine();
    nemici[1] = new Poppo();
    nemici[2] = new Missile();
    nemici[3] = new Ragnetto();  // Per quante volte possano comparire, c'\u00e8 sempre solo un oggetto che rappresenta quel nemico, a meno che non ne possano comparire pi\u00f9 d'uno nello schermo allo stesso momento.
    nemici[4] = new Ragnetto();
    nemici[5] = new Robi();
    nemici[6] = new Ragno();
    nemici[7] = new Ragnaccio();
    nemici[8] = new Avvoltoio();
    nemici[9] = new Palla();
    nemici[10] = new Mummia();
    nemici[11] = new Disco(speed);
    nemici[12] = new Johnny();
    nemici[13] = new Popponaso();
    nemici[14] = new Pozione();
    stato = 1;
    narrator = 0;
    diff = 1;
    punteggio = 0;
    colpito = 0;
    credits = height;
    creditime = 0;
    font = loadFont("KristenITC-Regular-48.vlw");
    gameo = loadFont("Chiller-Regular-72.vlw");
    titolo = loadImage("Titolo.png");
  }
  
  public void play(){  // Il metodo play decide cosa deve apparire nello schermo in un certo momento e cosa no.
    if(stato == 2){
      // equivale al gioco in s\u00e9
      pano.scorri();  // Fa muovere il panorama.
      if(diff == 2 || pano.whereAreWeNow() != 4) nemici[0].disegna();  // Questo nemico \u00e8 disegnato prima di Hero perch\u00e9 \u00e8 l'unico che, durante il contatto, non viene disegnato sull'avatar.
      h.disegna();  // Fa compiere un movimento all'avatar.
      if(diff != 0)  nemici[14].disegna();  // Questo nemico \u00e8 disegnato a parte perch\u00e9 si tratta della pozione che riempie la salute del protagonista. Era talmente simile ad un nemico da non aver senso gestirla come classe a parte.
      commandEnemies();  // Questo metodo fa copiere dei movimenti ai nemici.
      if(!pano.getCambio()) punteggio++;  // Aumento il punteggio quando sto giocando e il livello non sta cambiando
      if(punteggio == 5000 || punteggio == 10000 || punteggio == 15000){  
        pano.cambiaLivello();  // Passa al livello successivo ai punteggi sopracitati. 
      }
      if(punteggio == 20000){
        stato = 3;
        pano.cambiaLivello();  // Il quarto \u00e8 l'ultimo livello, pertanto c'\u00e8 anche bisogno di uscire dallo stato 2.
      }
    }
    else if(stato == 0){
      // equivale al Game Over
      pano.scorri();        // il panorama continua a muoversi, per un uscita drammatica.
      nemici[0].disegna();  // Anche i nemici funzionano come al solito.
      h.muori();            // Solo il personaggio ha un'animazione diversa.
      commandEnemies();
      displayGameOver();    // Mostra la scritta Game Over e permette di tornare al men\u00f9 principale.
    }
    else if(stato == 1){
      // Men\u00f9 Principale
      pano.scorri();       // il panorama si muove
      h.disegna();         // anche il personaggio, e pu\u00f2 fare qualsiasi mossa presente nel gioco.
      if(narrator == 0)  displayMenu();  // Se Narrator = 0 sono nel men\u00f9 principale. Da qui cambio difficolt\u00e0 o inizio a giocare.
      else if(narrator == 1)  displayInstruction();  // Altrimenti mostro le istruzioni e la storia dietro al gioco.
      // narrator poteva essere un boolean, ma ci potrebbe essere la necessit\u00e0 di aggiungere altre opzioni disponibili.
    }
    else if(stato == 3){
      //VITTORIA!
      pano.scorri();
      h.disegna();
      displayCredits();  // Titoli di coda.
    }
  }
  
  public void displayMenu(){
    int c1, c2, c3, d1, d2, d3;  // Colori che indicano come colorare il testo. Cambiano a seconda della posizione del mouse.
    int startX = 220, startY = 55, ingombroX = 195, ingombroY = 55;  // Queste variabili indicano dove posizionare il testo.
    c1 = c2 = c3 = d1 = d2 = d3 = color(0,0,0);
    if(diff == 0)       d1 = color(255,0,0);
    else if(diff == 1)  d2 = color(255,0,0);
    else                d3 = color(255,0,0);
//     GIOCA --> va al gioco
    if(mouseX > startX && mouseX < startX+ingombroX && mouseY > startY && mouseY < startY+ingombroY){  // Ognuno di questi if dice cosa fare quando il mouse passa sopra una scritta.
      c1 = color(0,255,0);
      if(mousePressed){  // Questi if interni dicono cosa fare quando si clicca sulla scritta corrispondente.
        stato = 2;
        pano.cambiaLivello();
      }
    }
//      ISTRUZIONI --> va al metodo successivo
    else if(mouseX > startX+10 && mouseX < startX+ingombroX+5 && mouseY > startY+ingombroY && mouseY < startY+ingombroY*2-15){
      c2 = color(0,0,255);
      if(mousePressed)  narrator = 1;
    }
//     Bottoni della difficolt\u00e0
    else if(mouseX > startX+ingombroX/1.5f+15 && mouseX < startX+ingombroX/1.5f+15+ingombroX/3 && mouseY > startY+ingombroY*2 && mouseY < startY+ingombroY*2+26){
      if(mousePressed){
        diff = 0;
      }
    }
    else if(mouseX > startX+ingombroX+30 && mouseX < startX+ingombroX+30+ingombroX/3 && mouseY > startY+ingombroY*2 && mouseY < startY+ingombroY*2+26){
      if(mousePressed){
        diff = 1;
      }
    }
    else if(mouseX > startX+ingombroX && mouseX < startX+ingombroX+ingombroX*1.3f && mouseY > startY+ingombroY*2+30 && mouseY < startY+ingombroY*2+65){
      if(mousePressed){
        diff = 2;
      }
    }
    // Tutto il testo che viene visualizzato, con le dimensioni e il colore di ciascuno.
    textFont(font);
    image(titolo,startX+ingombroX+20,startY-20,230,120);  // Logo del gioco.
    textSize(50);
    fill(c1);
    text("GIOCA",startX,startY,ingombroX,ingombroY);
    textSize(36);
    fill(c2);
    text("Istruzioni",startX+10,startY+ingombroY+5,ingombroX-5,ingombroY-15);
    textSize(26);
    fill(255,0,0);
    text("difficolt\u00e0:",startX+10,startY+ingombroY*2,ingombroX/1.5f,26);
    fill(d1);
    text("n00b",startX+ingombroX/1.5f+15,startY+ingombroY*2,ingombroX/3,26);
    fill(d2);
    text("ninja",startX+ingombroX+30,startY+ingombroY*2,ingombroX/3,26);
    textSize(15);
    fill(d3);
    text("come in quel sogno in cui sei nudo al liceo, interrogato di latino",startX+ingombroX,startY+ingombroY*2+30,ingombroX*1.3f,35);
  }
  
  public void displayInstruction(){
    // Questo metodo funziona esattamente come quello precedente.
     int startX = 200, startY = 60, ingombroX = 500, ingombroY = 200;
     textSize(30);
     fill(0);
     text("Istruzioni",startX,startY-33,ingombroX,30);
     String s = "Era un giorno bello e assolato. "+
                "Ninjpg decise di uscire per una corsetta."+
                "Sfortunatamente, il nostro eroe sapeva che dovunque poteva celarsi un pericolo. "+
                "Ninjpg doveva guardarsi le spalle.\n"+
                "- Premi la freccetta in alto per saltare.\n"+
                "- Tieni premuta la freccetta in basso per abbassarti.\n"+
                "- Premi la freccetta in basso mentre salti per dare un calcio volante.\n"+
                "  Se atterri su un nemico non appuntito con un calcio volante salterai ancora!\n"+
                "- Ogni azione consuma la barretta verde, se finisce dovrai aspettare che si\n"+
                "  ricarichi\n"+
                "- Se vieni colpito perderai vita (la barretta rossa), se la perdi tutta\n"+
                "  perderai (ma che caspita te lo dico a fare?)."+
                "<-- Puoi farlo ora! Guarda!";
     textSize(14);
     fill(255,30,30);
     text(s,startX,startY,ingombroX,ingombroY);
     int c = color(0,0,255);
     // Bottone per tornare indietro.
     if(mouseX > startX && mouseX < startX+75 && mouseY > startY+ingombroY+5 && mouseY < startY+ingombroY+25){
       c = color(255);
       if(mousePressed)  narrator = 0;
     }
     textSize(18);
     fill(c);
     text("Indietro",startX,startY+ingombroY+5,75,20);
  }
  public void displayCredits(){  // Questo metodo viene richiamato alla fine per mostrare i titoli di coda, come nel precedente, i titoli di coda sono scritti ordinati come somma di stringhe.
    int c = color(0xff8E7C5E);
    int startX = 10, startY = height-50,ingombroX = 140;
    //  Bottone che permette di tornare al men\u00f9 principale.
    if(mouseX > startX && mouseX < startX+ingombroX && mouseY > startY && mouseY < startY+17){
       c = color(0,255,0);
       if(mousePressed){
         stato = 1;
         punteggio = 0;
         pano.setNewGame();
         h.reset();
         resetEnemies();
         credits = height;
       }
     }
    String s = "Ed \u00e8 cos\u00ec che and\u00f2.\n\n"+
               "Ninjpg corse a lungo, cos\u00ec tanto da superare i limiti della realt\u00e0 stessa.\n\n"+
               "Da quel giorno, nessuno ebbe mai pi\u00f9 notizie di lui.\n\n\n"+
               "Ma c'\u00e8 chi dice\n\n"+
               "che Ninjpg stia ancora correndo, portando il suo aiuto dovunque sia necessario.\n\n\n"+
               "E cos\u00ec far\u00e0, fino alla fine dei tempi.\n\n\n"+
               "Run!\n\n"+
               "Un gioco scritto, disegnato e programmato da Pietro Martello.\n\n"+
               "Sviluppato con Processing 2, Paint, Microsoft Picture Manager.\n\n"+
               "Musica utilizzata:\n"+ 
               "'Dust Bowl', scritta e registrata da The Magnetic Fields, 1994.\n"+
               "'Oh, My Stars' scritta e registrata da Tanooki Suit, 2013.\n"+
               "'Once In A Lifetime' scritta e registrata da Luca Capitani.\n"+
               "'Epic' scritta e registrata da Calexico. 2012.\n"+
               "'Crank Heart', scritta e registrata da Xiu Xiu, 2004.\n"+
               "'A Warm Place' scritta e registrata da Nine Inch Nails, 1994.\n"+
               "'I Nearly Married A Human' scritta e registrata da The Tubeway Army, 1979.\n"+
               "Sono state utilizzate immagini tratte dai videoclip di 'Jerry Was A Racecar Driver' dei Primus e 'I Do What I Want, When I Want' degli Xiu Xiu.\n\n"+
               "Il Team (che sono io) desidera ringraziare:\n"+
               "Tutti quelli che hanno reso ci\u00f2 possibile, tra cui:\n"+
               "Zappi\n"+
               "Pietro 'Da Motafakka'\n"+
               "Du Tano\n"+
               "Lallario e Fabiana\n"+
               "Musco"+
               "Altri tizi simpatici\n";
    String n = "Uuuuuuh sembra che qualcuno abbia attivato la God Mode per completare il gioco!\n"+
               "E allora io il vero testo finale non te lo faccio vedere.\n\n"+
               "Ecco.\n\n\n"+
               "Cos\u00ec impari come ci si sente quando qualcuno usa un trucchetto da due soldi buttando al vento tutto il tuo lavoro.\n"+
               "Ti \u00e8 piaciuto il giretto? Completa il gioco in una delle altre difficolt\u00e0 per avere il vero finale.\n\n\n"+
               "Per\u00f2 ti dir\u00f2 che...\n"+
               "Sei stato colpito " + colpito + " volte.\n";
    if(colpito < 10)  n += "B\u00e9, se il signorino \u00e8 cos\u00ec bravo perch\u00e9 non ha abbastanza coraggio da abilitare il Game Over?";
    else if(colpito < 30)  n+= "Tieni duro ragazzo, un giorno ce la farai.";
    else              n += "Sai, accendere un gioco ed andarsene subito dopo lasciando il computer a giocare da solo \u00e8 molto scortese.";
    textFont(font);
    textSize(17);
    fill(c);
    text("Men\u00f9 Principale",startX,startY,ingombroX,20);
    textSize(20);
    fill(0);
    if(diff != 0)  text(s,200,credits,500,2200);  // A seconda della difficolt\u00e0 solo una delle stringhe viene mostrata.
    else           text(n,200,credits,500,2200);
    creditime++;
    if(creditime == 4){
      credits -= 1;
      creditime = 0;
    }
  }
  
  public void displayGameOver(){
    int startX = width/2 - 120, startY = 100, ingombroX = 240, ingombroY = 72;
    int c = color(0);
    textFont(gameo);
    textSize(72);
    fill(c);
    text("Game Over",startX,startY,ingombroX,ingombroY);
    
    // Pulsante per tornare al men\u00f9 principale.
    if(mouseX > startX-20 && mouseX < startX+ingombroX/2 && mouseY > startY+ingombroY && mouseY < startY+ingombroY+17){
      c = color(0,0,255);
      if(mousePressed){
        pano.setNewGame();
        h.reset();
        resetEnemies();
        punteggio = 0;
        stato = 1;
      }
    }
    textFont(font);
    textSize(17);
    fill(c);
    text("Men\u00f9 Principale",startX-20,startY+ingombroY,ingombroX,17);
    fill(255,0,0);
    text("Punteggio: "+punteggio/10,startX+ingombroX/2+30,startY+ingombroY,ingombroX,17);  // Mostra il tuo punteggio al momento della morte.
  }
  
  public void resetEnemies(){  // Questo metodo viene chiamato ad ogni passaggio di livello. Resetta tutti i nemici in modo che possano venire riutilizzati in un livello successivo senza spuntare d'improvviso.
    for(int i=1; i<nemici.length; i++){
      nemici[i].reset();
    }
    if(pano.whereAreWeNow() == 1){  // Le spine sono l'unico nemico che non viene resettato quasi mai perch\u00e9 compaiono in 3 livelli su 4.
      nemici[nemici.length-1].coorX = -nemici[nemici.length-1].lun;  // La pozione deve essere resettata all'inizio del gioco perch\u00e9 nel primo livello non compare.
      nemici[0].reset();
    }
    if(pano.whereAreWeNow() == 3 && diff == 1){
      nemici[0].reset();
    }
  }
  
  public void commandEnemies(){  // Questo metodo dice quali nemici muovere a seconda del livello.
    if(!pano.getCambio()){  // I nemici non compaiono mentre il livello cambia.
      if(pano.whereAreWeNow() == 1){
        nemici[1].disegna();
        nemici[2].disegna();
        if(diff == 2)  nemici[5].disegna();
      }
      else if(pano.whereAreWeNow() == 2){
        nemici[3].disegna();
        if(diff == 2) nemici[4].disegna();
        nemici[6].disegna();
        nemici[7].disegna();
      }
      else if(pano.whereAreWeNow() == 3){
        if(diff != 1) nemici[8].disegna();
        nemici[9].disegna();
        if(diff != 2) nemici[10].disegna();
        nemici[5].disegna();
      }
      else if(pano.whereAreWeNow() == 4){
        nemici[11].disegna();
        nemici[12].disegna();
        nemici[13].disegna();
      }
    }
  }
  
  
}
class Mummia extends Nemico{
  int ind,cont; // Indicatore d'animazione
  int tele, trick;  // Questo nemico si teletrasporta, queste variabili servono a questo.
  Mummia(){
    super(22,31,PApplet.parseInt(random(height-300,height-55)),32,40,2);
    for(int i=0; i<2; i++){
      I[i] = loadImage("Mummy "+(i+1)+".png"); 
    }
    ind = cont = 0;
    trick = PApplet.parseInt(random(2,3));   // Mummia si teletrasporta se trick \u00e8 uguale a 2. Ora come ora \u00e8 sempre uguale a 2, perch\u00e9 ho pensato sarebbe stato pi\u00f9 bello.
    tele = 36;
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt); 
      if(coll != 2){
        if(tele%4 == 0)  image(I[ind],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
        cont++;
        if(cont == 7){
          ind = (++ind)%(I.length);
          cont = 0;
        }
      }
      else{
        coorY = height-60;
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
      if(trick == 2 && tele > 0 && coorX < width*2/3)  tele--; 
      else if(tele == 0){
        trick = 0;  // trick viene riassegnato ad 1 cos\u00eccch\u00e9 non si teletrasporti pi\u00f9 di una volta per apparizione.
        tele = 36;
        coorY = PApplet.parseInt(random(height-300,height-55));   // Il luogo in cui il nemico si trasporter\u00e0 varia tra questi due valori.
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();
        coorY = PApplet.parseInt(random(height-300,height-55));
        trick = PApplet.parseInt(random(2,3));
      }
    }
    collisione(m.h);
  }
}
// Classe madre di tutti i nemici, raccoglie tutte le caratteristiche che hanno in comune.
class Nemico{
  int lun, alt, coorX, coorY, lunIm, altIm; // Variabili di identico scopo a quelle in Hero con lo stesso nome
  int coll, count;  // gestione della collisione e del reset.
  PImage I[]; // Array di immagini del nemico. A volte ne basta una, altre pi\u00f9 di una perch\u00e9 c'\u00e8 un'animazione.
  
  Nemico(int l, int a, int y, int li, int ai, int ni){  // Al metodo costruttore della superclasse serve sapere: la grandezza dell'area di collisione e dell'immagine, l'ordinata in cui disegnare il nemico, il numero delle immagini da caricare.
    lun = l;
    alt = a;
    coorY = y; 
    lunIm = li;
    altIm = ai;
    coorX = -lunIm; 
    coll = 0;
    count = PApplet.parseInt(random(100,1000));
    I = new PImage[ni];
  }
  
  public void disegna(){}  // Metodo virtuale che viene sovrascritto diverso in ogni classe figlia a seconda delle loro funzioni
  
  // Il ragionamento \u00e8 questo: tutti i nemici di un livello sono inizializzati dietro il protagonista, dove non possono essere visti. La variabile count comincia a decrementarsi.
  // Quando la loro variabile count arriva a 0, allora viene trasportato dal metodo reset ad una certa distanza del margine destro della finestra, da cui comincer\u00e0 ad avanzare verso il protagonista.
  // L'impirgo di due distanze random, impedisce tranne rari casi che i nemici siano cos\u00ec vicini da non poter essere schivati, e se questo succede si fa in modo che almeno uno di essi possa essere
  // eliminato con un calcio volante. Nelle classi nemico pi\u00f9 semplici, come Poppo, Spine o Missile queste zone di codice sono evidenti.
  
  public void reset(){
    coorX = width + PApplet.parseInt(random(0,1000));
    coll = 0;
    count = PApplet.parseInt(random(0,250));
  }
  
  public void collisione(Hero h){  // Il metodo collisione tranne rare eccezioni \u00e8 uguale per tutti i nemici. Controlla se il rettangolo che forma l'area di collisione del nemico e quello del giocatore si sono incrociati e in caso affermativo ferisce il nemico.
    float abbcoef;
    if(h.posizione == 6) abbcoef = 0;
    else                 abbcoef = h.alt;
    if(coorX-lun <= h.coorX+h.lun && coorX+lun >= h.coorX-h.lun
        && coorY-alt <= h.coorY+abbcoef && coorY+alt >= h.coorY-h.alt){
      if(h.posizione != 4 && coll == 0){
        h.ferisci();
        coll = 1;  // Ogni nemico pu\u00f2 ferire il protagonista una sola volta. Pertanto se l'ho colpito coll assume valore 1 e non riesco pi\u00f9 a colpirlo.
      }
      else if(coll == 0){  // Se per\u00f2 il giocatore stava sferrando un calcio volante il nemico viene sconfitto e non pu\u00f2 infliggere pi\u00f9 danni.
        h.posizione = 2;
        h.altsalto += height-coorY;   // il personaggio spicca un salto sconfiggendo il nemico
        coll = 2;  // coll assume valore 2 per permettere di inserire immagini differenti se il nemico viene colpito.
      }
    }
  }
  // Il metodo collisione deve essere chiamato al termine di ogni metodo disegna nelle classi figlie.
}
class Palla extends Nemico{  // Un semplicissimo nemico mobile che d\u00e0 tanta atmosfera.
  int ind,cont;  // Indicatore d'animazione
  Palla(){
    super(31,31,height-50,35,35,4);
    for(int i=0; i<4; i++){
      I[i] = loadImage("Palla di Polvere "+i+".png"); 
    }
    ind = cont = 0;
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed+2;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coll != 2){
        image(I[ind],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
        cont++;
        if(cont == 5){
          ind = (++ind)%(I.length);
          cont = 0;
        }
      }
      else{
        coorX += 2;
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();
      }
    }
    collisione(m.h);
  }
}
// La classe Panorama gestisce gli sfondi e la musica.
class Panorama{
  PImage sfondo[], prec, spec[];  // Array degli sfondi, immagine ausiliaria, immagine speciale
  int ind, pos, t, num;  // indicatore dello sfondo, altre variabili necessarie
  int ind2, cont;  // variabili per gestire il quarto livello, in cui il personaggio distrugge il tessuto stesso della realt\u00e0.
  int end;  // Variabile che serve per suonare la musichetta del game-over, la splendida "A Warm Place" dei Nine Inch Nails, e resettarla quando ricomincia il gioco.
  AudioPlayer play[];  //MUSICA!!!!11!1
  boolean cambio;  // boolean che interrompe il livello in corso per iniziare il successivo.
  
  Panorama(){
    ind = 0;
    pos = 0;
    num = -5;
    t = 255;
    ind2 = 0;
    cont = 0;
    sfondo = new PImage[6];  // Caricamento degli sfondi.
    sfondo[0] = loadImage("Scan52.png"); // Men\u00f9 Principale
    sfondo[1] = loadImage("Scan52.png"); // Livello 1
    sfondo[2] = loadImage("Scan53.png"); // Livello 2
    sfondo[3] = loadImage("Scan54.png"); // Livello 3
    sfondo[4] = loadImage("Blank.png");  // Livello 4
    sfondo[5] = loadImage("Blank.png");  // Crediti
    spec = new PImage[10];  // Immagini aggiuntive per il livello 4.
    spec[0] = loadImage("Les.png");
    spec[1] = loadImage("I&M.jpg");
    spec[2] = loadImage("Lallario.jpg");
    spec[3] = loadImage("Glitter.png");
    spec[4] = loadImage("Puglia 236.JPG");
    spec[5] = loadImage("SAM_0144.JPG");
    spec[6] = loadImage("Java.png");
    spec[7] = loadImage("Mac.jpg");
    spec[8] = loadImage("scimmia.png");
    spec[9] = loadImage("Musco.JPG");
    for(int j=0; j<4; j++){
      sfondo[j].resize(sfondo[j].width-1000,height);  // Ridimensiona gli sfondi per rendergli adattabili alla finestra
    }
    play = new AudioPlayer[7];  // Caricamento della musica.
    play[0] = minim.loadFile("Dust Bowl.mp3",2048);  // Men\u00f9 Principale.
    play[1] = minim.loadFile("Oh, My Stars.mp3",2048);  // Livelli dall'1 al 4
    play[2] = minim.loadFile("Once In A Lifetime.mp3",2048);
    play[3] = minim.loadFile("Epic.mp3",2048);
    play[4] = minim.loadFile("Crank Heart.mp3",2048);
    play[5] = minim.loadFile("I Nearly Married A Human.mp3",2048);  // Bah e pensare che invitano a Sanremo gli Spandau Ballet e di lui non si ricorda pi\u00f9 nessuno. Va B\u00e9 questi sono i crediti.
    play[6] = minim.loadFile("A Warm Place.mp3",2048);  // Musica del Game Over
    play[0].loop();  // Manda in loop la prima canzone
  }
  
  
  public void scorri(){  // Questo men\u00f9 serve per far scorrere lo sfondo. In realt\u00e0 il protagonista non si muove, \u00e8 lo sfondo a farlo creando l'illusione del movimento.
    tint(t);  // Il valore t viene cambiato dal metodo cambia per creare una dissolvenza tra i livelli.
    image(sfondo[ind],pos,0);  // Sistema lo sfondo. La variabile pos, settata a 0, viene diminuita facendo scorrere l'immagine verso sinistra.
    if(pos <= -sfondo[ind].width+width+5){  // Quando non \u00e8 pi\u00f9 possibile spostare lo sfondo viene segnalato. Il +5 c'\u00e8 perch\u00e9 durante lo scan le immagini sono venute di poco ruotate e ho dovuto sistemarle al computer. In questo modo si maschera il risultato.
      pos = width;    // pos viene sistemata in modo da ricominciare a stampare l'immagine dall'inizio quando essa finisce.
      prec = get(0,0,width,height);  // Si usa un'immagine ausiliaria per evitare che quella vecchia resti ferma.
    }
    if(pos > 0){
      image(prec,pos-width,0);  // Finch\u00e9 non si \u00e8 nella posizione originale, l'immagine prec scorre insieme a sfondo.
    }
    if(ind == 4 && !cambio){   // Nel quarto livello non c'\u00e8 sfondo (in realt\u00e0 c'\u00e8 solo per evitare di dover creare un metodo a parte per questo livello), ma ci sono delle immagini che vengono visualizzate in modo inquietante.
        tint(0xffBBFFFF,50);
        image(spec[ind2],0,0,width,height);
        cont++;
        if(cont == 50){
        ind2 = (++ind2)%(spec.length);
        cont = 0;
        }
    }
    pos-=m.speed;  // diminuisce pos del valore speed.
    noTint();
    if(cambio){
      cambia();  // Una serie d'operazione innescate dal metodo cambiaLivello che, ecco, cambieranno il livello.
    }
  }
  
  
  public void cambiaLivello(){  // Questo metodo viene richiamato dal motore. Inizia il processo di dissolvenza che cambier\u00e0 il livello settando la variabile booleana corrispondente e fermando la musica.
    cambio = true;
    play[ind].pause();
    play[ind].rewind();
  }
  
  
  public void cambia(){  // Questo metodo legge
    t = constrain(t+num,0,255);
      if(t==0){
        ind = (++ind)%sfondo.length;
        if(ind == 5) end = ind;
        num = 5;
        pos = 0;
      }
      if(t==255){
        num = -5;
        cambio = false;
        play[ind].loop();
        m.resetEnemies();
      }
  }
  
  public void setGameOver(){  // Richiamato quando il personaggio muore, blocca la musica del livello e fa partire quella del Game Over.
    play[ind].pause();
    play[ind].rewind();
    end = 6;
    play[end].play();
  }
  
  
  public void setNewGame(){  // Ruchiamato sia dopo il game over che alla fine dei crediti, resetta il contatore ind che indicava il livello e blocca la musica corrispondente.
    play[end].pause();  // end pu\u00f2 essere uguale a 6, se ho perso, o a 5, se ho vinto.
    play[end].rewind();
    ind = 0;
    play[ind].loop();
  }
  
  public int whereAreWeNow(){  // Dice in che livello ci troviamo
    return ind;
  }
  
  public boolean getCambio(){  // Dice se siamo in una fase di transizione.
    return cambio;
  }
  
}

class Poppo extends Nemico{   // Nemico terrestre di base
  Poppo(){
    super(25,26,height-55,42,43,2);
    for(int i=0; i<2; i++){
      I[i] = loadImage("Poppo"+(i+1)+".png"); 
    }
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed; // Cos\u00ec ci si avvicina al protagonista.
//      stroke(0);      // Questa parte commentata in ogni metodo draw permette di visualizzare l'area di collisione dei nemici
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coorX-lun <= m.h.coorX+m.h.lun && coll == 0)  
        image(I[1],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);  // Il poppo se non lo schiacci ti guarder\u00e0 sognante.
      else if(coll != 2){
        image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else{
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);    // Un po' tutti i nemici vulnerabili vengono spiaccicati quando colpiti.
      }
    }
    else{  // Qui avviene il decremento di count e il reset quando l'oggetto \u00e8 fuori dalla vista del ninja.
      count--;
      if(count <= 0){
        reset();
      }
    }
    collisione(m.h);
  }
  // Nota bene: Tutti i metodi disegna sono organizzati in modo simile a questo.
}
// Popponaso \u00e8 un nemico speciale formato dall'unione indissolubile tra due entit\u00e0, il Poppo e il Naso.

class Naso extends Nemico{
  int start;  // Questa variabile indica quando il naso si stacca e comincia a volare per conto suo.
  int cont, ind;  // Indicatore d'animazione
  Naso(int cY){  // Il naso viene inizializzato nella giusta posizione per apparire attaccato al Poppo, poi si discosta quando start < di un valore random.
    super(17,16,cY+1,19,18,3);
    I[0] = loadImage("Naso.png");
    for(int i=1; i<3; i++){
      I[i] = loadImage("Fuoco "+i+".png");
    }
    start = width-PApplet.parseInt(random(100,500));
    ind = cont = 0;
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coorX < start){
        coorX -= m.speed;
        if(coll != 2)  image(I[ind+1],coorX+lunIm/2,coorY-altIm,lunIm*2,altIm*2);  // Animazione della fiamma
      }
      if(coll != 2)  image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      //Il Naso non richiama mai esplicitamente il suo metodo reset, perch\u00e9 \u00e8 il Poppo che la chiama per lui.
      cont++;
      if(cont == 5){
        ind = (++ind)%(I.length-1);
        cont = 0;
      }
      
      collisione(m.h);
    }
  }
  public void reset(int coor){  // Nel metodo reset devo passare la nuova coordinata del Poppo, in modo che il naso possa essere risistemato a dovere.
    coorX = coor;
    coll = 0;
    start = width-PApplet.parseInt(random(100,500));
  }
}

class Popponaso extends Nemico{  // Il Popponaso \u00e8 uguale in tutto e per tutto al Poppo, ma contiene al suo interno un naso mobile, che viene inizializzato e controllato da lui.
  Naso n;
  Popponaso(){
    super(25,26,height-50,42,43,1);
    I[0] = loadImage("Popponaso.png");
    n = new Naso(coorY);
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coll != 2){
        image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else{
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
      n.disegna();
    }
    else{
      count--;
      if(count <= 0){
        reset();
      }
    }
    collisione(m.h);
  }
  
  public void reset(){
    coorX = width + PApplet.parseInt(random(0,1000));
    coll = 0;
    count = PApplet.parseInt(random(0,250));
    n.reset(coorX-13);
  }
}
class Pozione extends Nemico{  // Pozione non \u00e8 un nemico, ma appare e funziona nello stesso modo, tranne che cura al posto di ferire.
  Pozione(){
    super(10,20,PApplet.parseInt(random(height-200,height-55)),13,24,1);
    I[0] = loadImage("Pozione.png");
    count = -1;
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coll == 0) image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);;
    }
    else{
      if(m.punteggio == 2500 || m.punteggio == 7500 || m.punteggio == 12500 || m.punteggio == 17500)  count = PApplet.parseInt(random(0,500));  // Pozione appare soltanto in momenti prefissati
      if(count >= 0){
        count--;
        if(count == 0){
          reset();
        }
      }
    }
    collisione(m.h);
  }
  
  public void reset(){  // Il metodo reset \u00e8 modificato perch\u00e9 Pozione appare solo in certi momenti.
    coorX = sizelun + PApplet.parseInt(random(100,1000));
    coorY = PApplet.parseInt(random(height-200,height-55));
    coll = 0;
    count = -1;
  }
  
  public void collisione(Hero h){  // Uguale a quello di un nemico appuntito, solo che cura al posto di ferire.
    float abbcoef;
    if(h.posizione == 6) abbcoef = 0;
    else                 abbcoef = h.alt;
    if(coorX-lun <= h.coorX+h.lun && coorX+lun >= h.coorX-h.lun
        && coorY-alt <= h.coorY+abbcoef && coorY+alt >= h.coorY-h.alt){
      if(coll == 0){
        h.cura();
        coll = 1;
      }
    }
  }
}
class Ragnaccio extends Nemico{
  int realY, posizione, timer, base;  //Questo nemico saltella, queste variabili servono per gestire il salto
  // 1 -> salto
  // 2 -> atterraggio
  // 0 -> nulla
  Ragnaccio(){
    super(18,18,height-40,35,28,1);
    base = coorY;
    I[0] = loadImage("Ragnetto azzurro.png");
    realY = PApplet.parseInt(random(height-150, height-75));  // Questa variabile rappresenta la massima altezza raggiungibile dal salto.
    posizione = 0;  // Questa variabile indica se il ragno sta saltando(1), stando fermo(0) o cadendo(2).
    timer = PApplet.parseInt(random(0,100));   // Questa variabile indica il tempo fra un salto e l'altro. Inizializzata random ad ogni reset.
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(timer == 0){  // Quando il timer arriva a 0 inizia il salto e il timer \u00e8 re-inizializzato.
        timer = PApplet.parseInt(random(0,100));
        posizione = 1;
      }
      else if(posizione == 0){  // decrementa il timer
        timer--;
      }
      if(posizione == 1){  // Questi if gestiscono il salto in modo simile a quanto avviene nella classe Hero
        coorY-=3;
        if(coorY <= realY)  posizione = 2;
      }
      if(posizione == 2){
        coorY+=3;
        if(coorY >= base){
          coorY = base;
          posizione = 0;
        }
      }
      if(coll != 2){
        image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else{
        coorY = height-40;
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();
        realY = PApplet.parseInt(random(height-150, height-75));
        timer = PApplet.parseInt(random(0,100));
      }
    }
    collisione(m.h);
  }
}
class Ragnetto extends Nemico{
  int realY, tricky, vel;  // Questo nemico si lascia pendere dal soffitto e a volte casca, queste variabili gestiscono questo.
  Ragnetto(){
    super(16,18,20,35,28,1);
    I[0] = loadImage("Ragnetto verde.png");
    realY = PApplet.parseInt(random(height-200,height-55));  // Rappresenta il punto esatto dove il ragnetto rester\u00e0 a pendere.
    tricky = PApplet.parseInt(random(1,5));   // Se tricky == 4 il ragnetto si lascia cadere ad una coordinata fissa (210)
    vel = 2;   // Velocit\u00e0 con cui il ragno si abbassa
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;
//      stroke(1);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coorY < realY && coorX < width-50){  // Questo if gestisce il lento scendere del ragno.
        coorY = constrain(coorY+vel,0,realY);
      }
      if(tricky == 4 && coorX < width/2){   // Questo if gestisce la caduta
        realY =  height-30;
        tricky = 0;
        vel = 6;
      }
      if(coll != 2){
        stroke(2);
        if(tricky != 0)line(coorX,0,coorX,coorY);
        image(I[0],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else{
        coorY = height-40;
        image(I[0],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();
        coorY = 0;
        realY = PApplet.parseInt(random(height-200,height-55));
        tricky = PApplet.parseInt(random(1,5));
        vel = 2;
      }
    }
    collisione(m.h);
  }
}
class Ragno extends Nemico{  // Nemico veloce ma vulnerabile. Come il Poppo ma va pi\u00f9 veloce ed \u00e8 un po' pi\u00f9 grande.
  int cont, ind; //Indicatori d'animazione
  Ragno(){
    super(31,22,height-50,40,33,2);
    for(int i=0; i<2; i++){
      I[i] = loadImage("Ragno "+(i+1)+".png");
    }
    ind = 0;
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed*2;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      if(coll != 2){
        image(I[ind],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      }
      else{
        coorX += m.speed;
        image(I[1],coorX-lunIm,coorY+alt+5,lunIm*2,altIm/3);
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();
      }
    }
    cont++;
    if(cont == 6){
      ind = (++ind)%(I.length);
      cont = 0;
    }
    collisione(m.h);
  }
}
class Robi extends Nemico{  // Nemico mobile appuntito di base
  int ind,cont;
  Robi(){
    super(25,14,height-50,32,40,3);
    I[2] = loadImage("Robi.png");
    for(int i=0; i<2; i++){
      I[i] = loadImage("Polvere "+(i+1)+".png");
    }
    ind = 0;
    cont = 0;
  }
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed+2;
//      stroke(0);
//      fill(255,0,0);
//      rectMode(RADIUS);
//      rect(coorX,coorY,lun,alt);
      image(I[2],coorX-lunIm,coorY-altIm,lunIm*2,altIm*2);
      image(I[ind],coorX+lunIm,coorY+altIm/2+5,16,9);
      cont++;
      if(cont == 5){
        ind = (++ind)%2;
        cont = 0;
      }
    }
    else{
      count--;
      if(count <= 0){
        reset();
      }
    }
    collisione(m.h);
  }
  
  public void collisione(Hero h){  // Questo nemico fa l'overwriting di collisione perch\u00e9 non pu\u00f2 essere sconfitto, ma danneggia sempre il personaggio.
    float abbcoef;
    if(h.posizione == 6) abbcoef = 0;
    else                 abbcoef = h.alt;
    if(coorX-lun <= h.coorX+h.lun && coorX+lun >= h.coorX-h.lun
        && coorY-alt <= h.coorY+abbcoef && coorY+alt >= h.coorY-h.alt){
      h.ferisci();
      coll = 1;
    }
  }
}
class Spine extends Nemico{  // Un nemico speciale che ti colpisce solo quando tocchi terra
  Spine(){
    super(36,24,height-40,36,24,1);
    I[0] = loadImage("Spine.png");
  }
  
  public void disegna(){
    if(coorX >= -lun){
      coorX -= m.speed;  // Cos\u00ec ci si avvicina al protagonista.
//      stroke(0);
//     fill(255,0,0);
//     rectMode(RADIUS);
//     rect(coorX,coorY,lun,alt);
      image(I[0],coorX-lunIm,coorY+10-altIm,lunIm*2,altIm*2);
    }
    else{  // Qui avviene il decremento di count e il reset quando l'oggetto \u00e8 fuori dalla vista del ninja.
      count--;
      if(count <= 0){
        reset();
      }
    }
    collisione(m.h);
  }
  
  public void collisione(Hero h){  // Questo metodo collisione particolare \u00e8 venuto fuori mentre testavo il metodo collisione \u00e8 l'ho trovato utile. Funziona solo quando l'area di collisione del personaggio \u00e8 sullo stesso livello di quella del nemico. 
    if(coorX-lun <= h.coorX+h.lun && coorX+lun >= h.coorX-h.lun && coorY+alt <= h.coorY+h.alt+1){
      h.ferisci();
    }
    // Le spine sono appuntite e non possono essere distrutte.
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Run" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
